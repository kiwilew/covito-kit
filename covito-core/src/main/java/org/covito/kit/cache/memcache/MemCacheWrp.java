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
package org.covito.kit.cache.memcache;

import java.util.Date;

import org.covito.kit.cache.common.AbstractCacheImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.SimpleValueWrapper;

import com.whalin.MemCached.MemCachedClient;

/**
 * MemcachedCache 包装类
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014年6月18日]
 */
public class MemCacheWrp extends AbstractCacheImpl {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private String name;
	private MemCachedClient memcachedClient;
	private int expiredDuration = 2592000;

	/** 
	 * Constructor
	 */
	public MemCacheWrp() {
	}
	
	/**
	 * Constructor
	 */
	public MemCacheWrp(MemCacheManager manager) {
		this.cacheManager =manager;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	public Object getNativeCache() {
		return this.memcachedClient;
	}

	/** 
	 * 生成key
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param key
	 * @return
	 */
	protected String generateKey(Object key) {
		return this.name + "#" + key;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param key
	 * @return
	 */
	@Override
	public ValueWrapper get(Object key) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Get cache by name {}, key {}", getName(), key);
		}
		Object value = this.memcachedClient.get(generateKey(key));
		return ((value != null) ? new SimpleValueWrapper(fromStoreValue(value)) : null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param key
	 * @param value
	 */
	@Override
	public void put(Object key, Object value) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("put into cache {} by key {}, value {}", new Object[] { getName(),
					key, value });
		}
//		this.memcachedClient.add(generateKey(key), toStoreValue(value), new Date(new Date().getTime()+this.expiredDuration));
		this.memcachedClient.add(generateKey(key), toStoreValue(value));
		putObject(key, value);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param key
	 */
	@Override
	public void evict(Object key) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Evict from cache {} by key {}", getName(), key);
		}
		evictObject(key);
		this.memcachedClient.delete(generateKey(key));

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 */
	@Override
	public void clear() {
		this.memcachedClient.flushAll();
	}

	/**
	 * Set name
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set memcachedClient
	 *
	 * @param memcachedClient the memcachedClient to set
	 */
	public void setMemcachedClient(MemCachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	/**
	 * Set expiredDuration
	 *
	 * @param expiredDuration the expiredDuration to set
	 */
	public void setExpiredDuration(int expiredDuration) {
		this.expiredDuration = expiredDuration;
	}
	
	

}
