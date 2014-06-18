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

import org.covito.kit.cache.CacheException;
import org.covito.kit.cache.ICacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.support.AbstractCacheManager;

/**
 * 抽象缓存管理者实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public abstract class AbstractCacheManagerImpl extends AbstractCacheManager implements
		ICacheManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 是否支持查询缓存
	 */
	protected boolean supportQueryCache=true;

	/**
	 * 缓存关联关系维护缓存
	 */
	protected String relCacheName = "SYS_REL_CACHE";

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	public boolean isSupportQueryCache() {
		return supportQueryCache;
	}

	/**
	 * Set supportQueryCache
	 * 
	 * @param supportQueryCache
	 *            the supportQueryCache to set
	 */
	public void setSupportQueryCache(boolean supportQueryCache) {
		this.supportQueryCache = supportQueryCache;
	}

	/**
	 * Set relCacheName
	 * 
	 * @param relCacheName
	 *            the relCacheName to set
	 */
	public void setRelCacheName(String relCacheName) {
		this.relCacheName = relCacheName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param cacheName
	 * @param key
	 * @param relCache
	 */
	@Override
	public void setCacheRel(String cacheName, String key, CacheNameItem item) {
		Cache cache = getRelCache();
		if (cache == null) {
			logger.error("relCache [name:{}] is not exist!",relCacheName);
			throw new CacheException("relCache [name:"+relCacheName+"] is not exist!");
		}
		if (!isSupportQueryCache()) {
			return;
		}
		String str = generateRelCacheKey(item.getCacheName(), item.getKey());
		ValueWrapper svw = cache.get(str);
		KeyListRel rel = null;
		if (svw == null) {
			rel = new KeyListRel(item.getKey());
		} else {
			rel = (KeyListRel) svw.get();
		}
		if (rel.addRelObject(cacheName, key)) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("put into rel cache {} by key {}, value {}", new Object[] {
						str, str, svw });
			}
			cache.put(str, rel);
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param cacheName
	 * @param key
	 * @return
	 */
	@Override
	public String generateRelCacheKey(String cacheName,Object key){
		return cacheName+"_"+key;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @return
	 */
	@Override
	public Cache getRelCache() {
		return getCache(relCacheName);
	}

}
