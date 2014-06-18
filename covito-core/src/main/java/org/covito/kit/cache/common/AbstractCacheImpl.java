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

import java.io.Serializable;

import org.covito.kit.cache.ICacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * 抽象缓存实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
/**
 * 一句话功能简述
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年6月18日]
 */
public abstract class AbstractCacheImpl implements Cache {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected ICacheManager cacheManager;
	
	
	private final Object NULL_HOLDER = new NullHolder();
	
	/**
	 * 是否允许空值
	 */
	private boolean allowNullValues = true;

	/**
	 * 根据key删除缓存
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param key
	 */
	protected void evictObject(Object key) {
		Cache relCache = this.cacheManager.getRelCache();
		if (relCache == null) {
			logger.error("relCache is not exist!");
			return;
		}
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Evict by key {}", key);
		}
		if ((this.cacheManager.isSupportQueryCache()) && (!(getName().equals(relCache.getName())))) {
			String str = this.cacheManager.generateRelCacheKey(getName() , key);
			ValueWrapper valueWrapper = relCache.get(str);
			if (valueWrapper != null) {
				KeyListRel rel = (KeyListRel) valueWrapper.get();
				if ((rel != null) && (rel.getRelObject() != null)) {
					/* 删除与之关联的缓存 */
					for (CacheNameItem item : rel.getRelObject()) {
						if (this.logger.isDebugEnabled()) {
							this.logger.debug("Evict from cache {} by key {}", item.getCacheName(),
									item.getKey());
						}
						Cache tempCache = this.cacheManager.getCache(item.getCacheName());
						tempCache.evict(item.getKey());
					}
				}
			}
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Evict key {}  from cache {}", str, relCache.getName());
			}
			relCache.evict(str);
		}
	}
	
	/** 
	 * 空对象解包
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param storeValue
	 * @return
	 */
	protected Object fromStoreValue(Object storeValue) {
		if (this.allowNullValues && (storeValue instanceof NullHolder)) {
			return null;
		}
		return storeValue;
	}
	
	/** 
	 * 空对象包装
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param userValue
	 * @return
	 */
	protected Object toStoreValue(Object userValue) {
		if ((this.allowNullValues) && (userValue == null)){
			return NULL_HOLDER;
		}
		return userValue;
	}

	/**
	 * 新增缓存
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param key
	 * @param value
	 */
	protected void putObject(Object key, Object value) {
		
	}

	public void setCacheManager(ICacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public static class NullHolder implements Serializable {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

	}
}
