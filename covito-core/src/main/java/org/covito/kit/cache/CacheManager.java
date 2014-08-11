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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.covito.kit.cache.common.AbsCacheImpl;
import org.covito.kit.cache.common.CacheWrp;
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

	private Set<String> cacheNames = new LinkedHashSet<String>(16);

	@SuppressWarnings("rawtypes")
	private ConcurrentMap<String, CacheWrp> cacheMap = new ConcurrentHashMap<String, CacheWrp>(16);

	private CacheMonitor monitor = new DefaultCacheMonitor();

	private Thread cleanUpThread = new Thread() {
		public void run() {
			cleanUp();
		}
	};

	private static CacheManager instance = new CacheManager();

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

	/**
	 * @param cache 缓存实现
	 */
	@SuppressWarnings("rawtypes")
	public static <K, V> void addCache(Cache<K, V> cache) {
		if(instance.cacheNames.contains(cache.getName())){
			throw new CacheException("cache ["+cache.getName()+"] exist!");
		}
		CacheWrp cacheWrp=new CacheWrp<K, V>(cache);
		instance.cacheMap.put(cache.getName(), cacheWrp);
		instance.cacheNames.add(cache.getName());
	}

	/**
	 * 根据缓存名称获取缓存
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Cache<K, V> getCache(String name) {
		CacheWrp<K, V> cw = instance.cacheMap.get(name);
		if (cw == null) {
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

		return new ArrayList<CacheWrp>();
	}

	
	/**
	 * 新增关联关系
	 * 当关联缓存删除时级联删除本身
	 * 
	 * @param cacheName 
	 * @param key
	 * @param relCacheName 关联缓存名称
	 * @param relKey 关联缓存
	 */
	public static void setCacheRel(String cacheName, Object key, String relCacheName,String relKey) {
		Cache ca=getCache(relCacheName);
		if(ca instanceof AbsCacheImpl){
			AbsCacheImpl absCa=(AbsCacheImpl)ca;
			absCa.addRel(relKey, cacheName, key);
		}
	}
	
	
	/**
	 * 删除关联关系
	 * @param cacheName
	 * @param key
	 * @param relCacheName
	 * @param relKey
	 */
	public static void removeCacheRel(String cacheName, Object key, String relCacheName,String relKey) {
		Cache ca=getCache(cacheName);
		if(ca instanceof AbsCacheImpl){
			AbsCacheImpl absCa=(AbsCacheImpl)ca;
			absCa.removeRel(key, relCacheName, relKey);
		}
	}


}
