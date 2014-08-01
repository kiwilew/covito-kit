/*
 * Copyright 2010-2014  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */
package org.covito.kit.cache.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.EliminateHandler;
import org.covito.kit.cache.KeyNotFoundHandler;
import org.covito.kit.cache.monitor.MonitorItem;
import org.covito.kit.cache.monitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象缓存实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public abstract class AbsCacheImpl<K, V> implements Cache<K, V>, Visitor {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	Lock l = new ReentrantLock(); // 锁

	protected LinkedList<K> keySet = new LinkedList<K>();

	protected AtomicLong queryCount = new AtomicLong(); // 查询次数

	protected AtomicLong hitCount = new AtomicLong(); // 命中次数

	private EliminateHandler<K, V> eliminateHandler; // 淘汰机制Handler

	private KeyNotFoundHandler<K, V> keyNotFound; // key没有找到处理

	private long timeout = -1; // 超时时间（毫秒）,当为负数，则表示不超时
	
	private long visitTimeout = -1; // 最后访问超时时间（毫秒）,当为负数，则表示不超时

	private int maxSize = -1; // 缓存允许最大的个数 当为负数时，表示无上限

	private double cleanupRate = 0.3; // 清除率，清理时清除的百分比

	private volatile long reflushTime = 0; // 刷新时间

	@Override
	public long getHitCount() {
		return hitCount.get();
	}

	@Override
	public long getQueryCount() {
		return queryCount.get();
	}

	@Override
	public long size() {
		return keySet.size();
	}

	@Override
	public V get(K key) {
		queryCount.incrementAndGet();
		l.lock();
		try {
			Node<K, V> n = getNode(key);
			if (n != null) {
				hitCount.incrementAndGet();
				n.setLastVisitTime(System.currentTimeMillis());
				return n.getValue();
			} else {
				if (keyNotFound != null) {
					V value = keyNotFound.onKeyNotFound(key);
					this.put(key, value);
					return get(key);
				}
			}
		} finally {
			l.unlock();
		}
		return null;
	}

	protected abstract Node<K, V> getNode(K key);

	@Override
	public void put(K key, V value) {
		l.lock();
		try {
			Node<K, V> n = new Node<K, V>(key, value);
			putNode(n);
			keySet.add(key);
		} finally {
			l.unlock();
		}
	}

	protected abstract void putNode(Node<K, V> n);

	@Override
	public void evict(K key) {
		l.lock();
		try {
			for (CacheNameItem item : getNode(key).getItemList()) {
				if (logger.isDebugEnabled()) {
					logger.debug("Evict from cache {} by key {}", item.getCacheName(),
							item.getKey());
				}
				Cache<Object, ?> tempCache = CacheManager.getCache(item.getCacheName());
				tempCache.evict(item.getKey());
			}
			removeNode(key);
			keySet.remove(key);
		} finally {
			l.unlock();
		}
	}

	protected abstract void removeNode(K key);

	/**
	 * 新增关联对象
	 * 
	 * @param key
	 * @param relCacheName
	 * @param relKey
	 */
	public void addRel(K key, String relCacheName, Object relKey) {
		l.lock();
		try {
			Node<K, V> n = getNode(key);
			if (n != null) {
				n.addRelObject(relCacheName, relKey);
			} else {
				logger.warn("not found cache key[?]", key);
			}
		} finally {
			l.unlock();
		}
	}

	/**
	 * 删除关联对象
	 * 
	 * @param key
	 * @param relCacheName
	 * @param relKey
	 */
	public void removeRel(K key, String relCacheName, Object relKey) {
		l.lock();
		try {
			Node<K, V> n = getNode(key);
			if (n != null) {
				n.removeRelObject(relCacheName, relKey);
			} else {
				logger.warn("not found cache key[?]", key);
			}
		} finally {
			l.unlock();
		}
	}

	@Override
	public List<MonitorItem> getMonitorItem() {
		return null;
	}

	@Override
	public long cleanUp() {
		logger.info("cleanUp begin...");
		long now = System.currentTimeMillis();
		int c = cleanUp(eliminateHandler);
		reflushTime = System.currentTimeMillis() - now;
		return c;
	}

	private int cleanUp(EliminateHandler<K, V> h) {
		// 删除超时的元素
		int c1 = 0;
		try {
			if (timeout > 0||visitTimeout>0) {
				Iterator<K> it = keySet.iterator();
				while (it.hasNext()) {
					K key = it.next();
					Node<K, V> node = getNode(key);
					long now = System.currentTimeMillis();
					if(timeout>0){
						if (now - node.getCreateTime() >= timeout) {
							if (h == null) {
								evict(node.getKey());
								++c1;
							} else {
								if (h.onTimeOut(node)) {
									evict(node.getKey());
									++c1;
								}
							}
						}
					}
					if(visitTimeout>0){
						if (now - node.getLastVisitTime() >= visitTimeout) {
							if (h == null) {
								evict(node.getKey());
								++c1;
							} else {
								if (h.onVisitTimeOut(node)) {
									evict(node.getKey());
									++c1;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdvCache.cleanUp, clean timeout node error.", e);
		}

		// 按照最后访问时间淘汰
		int c2 = 0;
		try {
			long n = size();
			if (n > maxSize) {
				final int newSize = (int) (((double) maxSize) * (1.0 - cleanupRate));
				while (n - c2 > newSize){
					Node<K, V> node =null;//= //nl.tail;
					if (node == null){
						break;
					}
					if (h.onCleanUpRate(node)) {
						evict(node.getKey());
						++c2;
					}
				}
			}
		} catch (Exception e) {
			logger.error("AdvCache.cleanUp, clean node by last access time error.", e);
		}
		return c1+c2;
	}

	@Override
	public void clear() {
		this.queryCount.set(0);
		this.hitCount.set(0);
		this.keySet.clear();
		reflushTime=0;
		l.lock();
		try {
			removeAll();
		} finally {
			l.unlock();
		}
	}

	protected abstract void removeAll();

	public void setKeyNotFound(KeyNotFoundHandler<K, V> keyNotFound) {
		this.keyNotFound = keyNotFound;
	}

	public void setEliminateHandler(EliminateHandler<K, V> eliminateHandler) {
		this.eliminateHandler = eliminateHandler;
	}

}
