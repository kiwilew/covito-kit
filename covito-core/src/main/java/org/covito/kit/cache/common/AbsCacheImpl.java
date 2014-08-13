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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.covito.kit.cache.AutoRefreshHandler;
import org.covito.kit.cache.AutoSaveHandler;
import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.EliminateHandler;
import org.covito.kit.cache.KeyNotFoundHandler;
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

	protected LinkedKeySet<K> keySet = new LinkedKeySet<K>();

	protected AtomicLong queryCount = new AtomicLong(); // 查询次数

	protected AtomicLong hitCount = new AtomicLong(); // 命中次数

	private EliminateHandler<K, V> eliminateHandler; // 淘汰机制Handler

	private AutoRefreshHandler<K, V> autoRefresh; // 自动刷新Handler
	
	private AutoSaveHandler<K, V> autoSave; // 自动保存Handler
	
	private KeyNotFoundHandler<K, V> keyNotFound; // key没有找到处理

	private long timeout = -1; // 超时时间（毫秒）,当为负数，则表示不超时
	
	private long visitTimeout = -1; // 最后访问超时时间（毫秒）,当为负数，则表示不超时

	private int maxSize = -1; // 缓存允许最大的个数 当为负数时，表示无上限

	private double cleanupRate = 0.3; // 清除率，清理时清除的百分比

	private volatile long reflushTime = 0; // 刷新时间
	
	private long checkInterval = 1000 * 60 * 60;//检查间隔，默认1小时
	
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
				keySet.moveToTop(key);
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
			if(getNode(key)!=null&&getNode(key).getItemList()!=null){
				for (CacheNameItem item : getNode(key).getItemList()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Evict from cache {} by key {}", item.getCacheName(),
								item.getKey());
					}
					Cache<Object, ?> tempCache = CacheManager.getCache(item.getCacheName());
					tempCache.evict(item.getKey());
				}
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
	public long getReflushTime() {
		return reflushTime;
	}

	public long cleanUp() {
		logger.info("cleanUp begin...");
		long now = System.currentTimeMillis();
		int c = cleanUp(eliminateHandler);
		reflushTime = System.currentTimeMillis() - now;
		return c;
	}
	
	/** 
	 * 自动保存
	 * <p>功能详细描述</p>
	 *
	 */
	public void autoSaveHandler(){
		if(autoSave==null){
			return;
		}
		for(K key:this.keySet()){
			boolean success=false;
			try {
				if(this.getNode(key)==null){
					continue;
				}
				success=autoSave.save(key,this.getNode(key).getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(success){
				this.evict(key);
			}
		}
	}
	
	/** 
	 * 自动刷新
	 * <p>功能详细描述</p>
	 *
	 */
	public void autoRefreshHandler(){
		if(autoRefresh==null){
			return;
		}
		Map<K,V> remap=autoRefresh.refresh();
		if(remap==null){
			return;
		}
		this.clear();
		for(Entry<K,V> e:remap.entrySet()){
			this.put(e.getKey(), e.getValue());
		}
	}
	
	@Override
	public Set<K> keySet() {
		return Collections.unmodifiableSet(keySet.keySet());
	}

	private int cleanUp(EliminateHandler<K, V> h) {
		// 删除超时的元素
		int c1 = 0;
		
		try {
			if (timeout > 0||visitTimeout>0) {
				Set<Node<K, V>> waitDel=new HashSet<Node<K,V>>();
				Iterator<K> it = keySet.iterator();
				while (it.hasNext()) {
					K key = it.next();
					Node<K, V> node = getNode(key);
					if(node==null){
						continue;
					}
					long now = System.currentTimeMillis();
					if(timeout>0){
						if (now - node.getCreateTime() >= timeout) {
							if (h == null) {
								waitDel.add(node);
							} else {
								if (h.onTimeOut(node)) {
									waitDel.add(node);
								}
							}
						}
					}
					if(visitTimeout>0){
						if (now - node.getLastVisitTime() >= visitTimeout) {
							if (h == null) {
								waitDel.add(node);
							} else {
								if (h.onVisitTimeOut(node)) {
									waitDel.add(node);
								}
							}
						}
					}
				}
				for(Node<K, V> n: waitDel){
					evict(n.getKey());
					++c1;
				}
			}
		} catch (Exception e) {
			logger.error("AdvCache.cleanUp, clean timeout node error.", e);
		}

		// 按照最后访问时间淘汰
		int c2 = 0;
		try {
			long n = size();
			if (maxSize>0&&n > maxSize) {
				final int newSize = (int) (((double) maxSize) * (1.0 - cleanupRate));
				while (n - c2 > newSize){
					if (keySet.tail == null){
						break;
					}
					K k = keySet.tail.value;
					Node<K, V> node=getNode(k);
					if(h==null){
						evict(node.getKey());
						++c2;
					}else{
						if (h.onCleanUpRate(node)) {
							evict(node.getKey());
							++c2;
						}
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

	/**
	 * 缓存获取时Key没有找到Handler
	 * @param keyNotFound
	 */
	public void setKeyNotFound(KeyNotFoundHandler<K, V> keyNotFound) {
		this.keyNotFound = keyNotFound;
	}

	/**
	 * 淘汰机制Handler，用于定制在特定业务中某些实体属性特殊时排除清理范围
	 * @param eliminateHandler
	 */
	public void setEliminateHandler(EliminateHandler<K, V> eliminateHandler) {
		this.eliminateHandler = eliminateHandler;
	}

	/**
	 * 清理时的超时时间[毫秒]（默认-1 不超时）
	 * @param timeout
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * 多久没有访问的超时间[毫秒]（默认-1 不超时）
	 * @param visitTimeout
	 */
	public void setVisitTimeout(long visitTimeout) {
		this.visitTimeout = visitTimeout;
	}

	/**
	 * 缓存元素个数最大值（默认无上限）
	 * @param maxSize
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * 设置达到允许最大值时的清除率(默认0.3)
	 * @param cleanupRate
	 */
	public void setCleanupRate(double cleanupRate) {
		this.cleanupRate = cleanupRate;
	}

	public long getCheckInterval() {
		return checkInterval;
	}

	/**
	 * 获取检查间隔
	 * @param checkInterval
	 */
	public void setCheckInterval(long checkInterval) {
		this.checkInterval = checkInterval;
		if (this.checkInterval <= 0){
			this.checkInterval = Long.MAX_VALUE;
		}
	}

	public AutoRefreshHandler<K, V> getAutoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(AutoRefreshHandler<K, V> autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public AutoSaveHandler<K, V> getAutoSave() {
		return autoSave;
	}

	public void setAutoSave(AutoSaveHandler<K, V> autoSave) {
		this.autoSave = autoSave;
	}

}
