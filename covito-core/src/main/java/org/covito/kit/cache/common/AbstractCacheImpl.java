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

import java.util.Collection;
import java.util.Iterator;

import org.covito.kit.cache.ICacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * 抽象缓存实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public abstract class AbstractCacheImpl implements Cache {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected String SUFFIX = "List";
	protected ICacheManager cacheManager;

	/** 
	 * 根据key删除缓存
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param key
	 */
	protected void evictObject(Object key) {
		Cache cache;
		if (this.logger.isDebugEnabled())
			this.logger.debug("Evict by key {}", key);
		if ((this.cacheManager.isSupportQueryCache()) && (!(getName().endsWith(this.SUFFIX)))
				&& (!(getName().equals(this.cacheManager.getRelCacheName())))) {
			cache = this.cacheManager.getCache(this.cacheManager.getRelCacheName());
			if (cache != null) {
				String str = generateRelCacheKey(getName() + key);
				SimpleValueWrapper valueWrapper = (SimpleValueWrapper) cache.get(str);
//				if (valueWrapper != null) {
//					IdListRel localIdListRel = (IdListRel) valueWrapper.get();
//					if ((localIdListRel != null) && (localIdListRel.getRelObject() != null)) {
//						Iterator localIterator = localIdListRel.getRelObject().iterator();
//						while (localIterator.hasNext()) {
//							CacheNameAndItemWrapper localCacheNameAndItemWrapper = (CacheNameAndItemWrapper) localIterator
//									.next();
//							if (this.logger.isDebugEnabled())
//								this.logger.debug("Evict from cache {} by key {}",
//										localCacheNameAndItemWrapper.getCacheName(),
//										localCacheNameAndItemWrapper.getKey());
//							Cache localCache2 = this.cacheManager
//									.getCache(localCacheNameAndItemWrapper.getCacheName());
//							localCache2.evict(localCacheNameAndItemWrapper.getKey());
//						}
//					}
//				}
				if (this.logger.isDebugEnabled())
					this.logger.debug("Evict key {}  from cache {}", str,
							this.cacheManager.getRelCacheName());
				cache.evict(str);
			}
		} else if (this.cacheManager.isRemoveAllEntries()) {
			cache = this.cacheManager.getCache(getName() + this.SUFFIX);
			if (cache != null) {
				if (this.logger.isDebugEnabled())
					this.logger.debug("Evict all cache from {}", cache.getName());
				cache.clear();
			}
		}
	}

	/** 
	 * 新增缓存
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param key
	 * @param value
	 */
	protected void putObject(Object key, Object value) {
		if ((this.cacheManager.isSupportQueryCache()) && (getName().endsWith(this.SUFFIX))
				&& (!(getName().equals(this.cacheManager.getRelCacheName())))
				&& (value != null)) {
			Object localObject = null;
			if (Collection.class.isAssignableFrom(value.getClass()))
				localObject = (Collection) value;
//			else if (PageSupport.class.isAssignableFrom(value.getClass()))
//				localObject = ((PageSupport) value).getResultList();
			if (localObject != null) {
				Iterator localIterator = ((Collection) localObject).iterator();
				while (localIterator.hasNext()) {
//					BaseEntity localBaseEntity = (BaseEntity) localIterator.next();
					Cache localCache = this.cacheManager.getCache(this.cacheManager
							.getRelCacheName());
//					String str = generateRelCacheKey(getName().substring(0, getName().length() - 4)
//							+ localBaseEntity.getId());
//					SimpleValueWrapper localSimpleValueWrapper = (SimpleValueWrapper) localCache
//							.get(str);
//					IdListRel localIdListRel = null;
//					if (localSimpleValueWrapper != null)
//						localIdListRel = (IdListRel) localSimpleValueWrapper.get();
//					if (localSimpleValueWrapper == null)
//						localIdListRel = new IdListRel(localBaseEntity.getId());
//					if (localIdListRel.addRelObject(getName(), key)) {
//						if (this.logger.isDebugEnabled())
//							this.logger.debug("put into rel cache {} by key {}, value {}",
//									new Object[] { str, str, localSimpleValueWrapper });
//						localCache.put(str, localIdListRel);
//					}
				}
			}
		}
	}

	/** 
	 * 生成缓存提供者ID
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param paramString
	 * @return
	 */
	public abstract String generateRelCacheKey(String paramString);

	public void setCacheManager(ICacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}
