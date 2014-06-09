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

import org.covito.kit.cache.ICacheManager;
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

	protected boolean supportQueryCache;

	protected boolean removeAllEntries;

	protected String relCacheName = "COVITO_CACHE";

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
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	public boolean isRemoveAllEntries() {
		return removeAllEntries;
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
	 * Set removeAllEntries
	 * 
	 * @param removeAllEntries
	 *            the removeAllEntries to set
	 */
	public void setRemoveAllEntries(boolean removeAllEntries) {
		this.removeAllEntries = removeAllEntries;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	public String getRelCacheName() {
		return relCacheName;
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

}
