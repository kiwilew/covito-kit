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

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.CacheNameItem;
import org.covito.kit.cache.Node;
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

	Lock l = new ReentrantLock();

	protected AtomicLong queryCount = new AtomicLong(); // 查询次数

	protected AtomicLong hitCount = new AtomicLong();

	protected AtomicLong size = new AtomicLong();

	@Override
	public long getHitCount() {
		return hitCount.get();
	}

	@Override
	public long getQueryCount() {
		return queryCount.get();
	}

	@Override
	public long getMemoryUsage() {
		return -1;
	}

	@Override
	public long size() {
		return size.get();
	}

	@Override
	public V get(K key) {
		queryCount.incrementAndGet();
		l.lock();
		try {
			Node<K, V> n = getNode(key);
			if (n != null) {
				hitCount.incrementAndGet();
				return n.getValue();
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
			size.incrementAndGet();
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
					logger.debug("Evict from cache {} by key {}", item.getCacheName(),item.getKey());
				}
				Cache<Object, ?> tempCache = CacheManager.getCache(item.getCacheName());
				tempCache.evict(item.getKey());
			}
			removeNode(key);
			size.decrementAndGet();
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
		// TODO Auto-generated method stub
		return 0;
	}
}
