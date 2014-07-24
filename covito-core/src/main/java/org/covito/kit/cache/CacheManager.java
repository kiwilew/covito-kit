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
package org.covito.kit.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.covito.kit.cache.monitor.CacheMonitor;
import org.covito.kit.cache.monitor.DefaultCacheMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存管理者
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public class CacheManager {

	private final static Logger logger = LoggerFactory.getLogger(CacheManager.class);

	@SuppressWarnings("rawtypes")
	private ConcurrentMap<String, CacheWrp> cacheMap = new ConcurrentHashMap<String, CacheWrp>(16);

	private static CacheMonitor monitor = new DefaultCacheMonitor();

	private Set<String> cacheNames = new LinkedHashSet<String>(16);

	private static CacheManager instance = new CacheManager();

	/**
	 * 缓存关联关系维护缓存
	 */
	private String relCacheName = "SYS_REL_CACHE";

	private static Thread cleanUpThread = new Thread() {
		public void run() {
			cleanUp();
		}
	};

	private CacheManager() {
		init();
		monitor.start();
		cleanUpThread.start();
	}

	public void init() {
		Collection<? extends CacheWrp> caches = loadCaches();
		cacheMap.clear();
		cacheNames.clear();
		for (CacheWrp cache : caches) {
			cacheMap.put(cache.getCache().getName(), cache);
			cacheNames.add(cache.getCache().getName());
		}
	}

	private static void cleanUp() {
		while (true) {
			try {
				Collection<CacheWrp> vs = instance.cacheMap.values();
				for (CacheWrp o : vs) {
					try {
						o.cleanUp();
					} catch (Exception e) {
						logger.error("cleanUp " + o, e);
					}
				}
				Thread.sleep(10000);
			} catch (Exception e) {
				logger.error("cleanUp: ", e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static <K,V> void addCache(CacheWrp<K, V> cache) {
		instance.cacheMap.put(cache.getCache().getName(), cache);
		instance.cacheNames.add(cache.getCache().getName());
	}

	/**
	 * 根据缓存名称获取缓存
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Cache<K, V> getCache(String name) {
		CacheWrp<K, V> cw=instance.cacheMap.get(name);
		if(cw==null){
			return null;
		}
		return cw.getCache();
	}

	public static Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(instance.cacheNames);
	}

	/**
	 * Load the caches for this cache manager. Occurs at startup. The returned
	 * collection must not be null.
	 */
	private Collection<? extends CacheWrp> loadCaches() {

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param cacheName
	 * @param key
	 * @param relCache
	 */
	public static void setCacheRel(String cacheName, Object key, CacheNameItem item) {
		Cache<Object, KeyListRel> cache = getCache(instance.relCacheName);
		if (cache == null) {
			logger.error("relCache [name:{}] is not exist!", instance.relCacheName);
			throw new CacheException("relCache [name:" + instance.relCacheName + "] is not exist!");
		}
		Object relKey = new CacheNameItem(item.getCacheName(), item.getKey());
		KeyListRel rel = cache.get(relKey);
		if (rel == null) {
			rel = new KeyListRel(item.getKey());
		}
		if (rel.addRelObject(cacheName, key)) {
			if (logger.isDebugEnabled()) {
				logger.debug("put into rel cache {} by key {}, value {}", new Object[] { relKey,
						relKey, rel });
			}
			cache.put(relKey, rel);
		}
	}

	/**
	 * 根据key删除缓存
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param key
	 */
	public static void checkEvictRel(Cache<?, ?> cache, Object key) {
		Cache<Object, KeyListRel> relCache = getCache(instance.relCacheName);
		if (relCache == null) {
			logger.error("relCache is not exist!");
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Evict by key {}", key);
		}
		if ((!(cache.getName().equals(relCache.getName())))) {
			Object str = new CacheNameItem(cache.getName(), key);
			KeyListRel rel = relCache.get(str);
			if (rel != null) {
				if ((rel != null) && (rel.getRelObject() != null)) {
					/* 删除与之关联的缓存 */
					for (CacheNameItem item : rel.getRelObject()) {
						if (logger.isDebugEnabled()) {
							logger.debug("Evict from cache {} by key {}", item.getCacheName(),
									item.getKey());
						}
						Cache<Object, ?> tempCache = CacheManager.getCache(item.getCacheName());
						tempCache.evict(item.getKey());
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Evict key {}  from cache {}", str, relCache.getName());
			}
			relCache.evict(str);
		}
	}

}
