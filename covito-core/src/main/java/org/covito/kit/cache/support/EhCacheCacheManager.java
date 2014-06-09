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
package org.covito.kit.cache.support;

import java.util.Collection;
import java.util.LinkedHashSet;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;

import org.covito.kit.cache.common.AbstractCacheManagerImpl;
import org.springframework.cache.Cache;
import org.springframework.util.Assert;

import com.legendshop.core.cache.LegendCache;

/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public class EhCacheCacheManager extends AbstractCacheManagerImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	protected Collection<? extends Cache> loadCaches() {
		Assert.notNull(this._$1, "A backing EhCache CacheManager is required");
		Status localStatus = this._$1.getStatus();
		Assert.isTrue(
				Status.STATUS_ALIVE.equals(localStatus),
				"An 'alive' EhCache CacheManager is required - current cache is "
						+ localStatus.toString());
		String[] arrayOfString1 = this._$1.getCacheNames();
		LinkedHashSet localLinkedHashSet = new LinkedHashSet(arrayOfString1.length);
		String[] arrayOfString2 = arrayOfString1;
		int i = arrayOfString2.length;
		for (int j = 0; j < i; ++j) {
			String str = arrayOfString2[j];
			localLinkedHashSet.add(new LegendCache(this, this._$1.getEhcache(str)));
		}
		return localLinkedHashSet;
	}

	private CacheManager _$1;

	public Cache getCache(String paramString) {
		Object localObject = super.getCache(paramString);
		if (localObject == null) {
			Ehcache localEhcache = this._$1.getEhcache(paramString);
			if (localEhcache != null) {
				localObject = new LegendCache(this, localEhcache);
				addCache((Cache) localObject);
			}
		}
		return ((Cache) localObject);
	}

	public void setCacheManager(CacheManager paramCacheManager) {
		this._$1 = paramCacheManager;
	}
}
