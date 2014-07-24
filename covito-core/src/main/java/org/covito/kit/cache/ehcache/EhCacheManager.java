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
package org.covito.kit.cache.ehcache;

import java.util.Collection;
import java.util.LinkedHashSet;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;

import org.covito.kit.cache.Cache;
import org.springframework.util.Assert;

/**
 * EhCacheCache Manager实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public class EhCacheManager  {

	private CacheManager cacheManager;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	protected Collection<? extends Cache> loadCaches() {
		Assert.notNull(this.cacheManager, "A backing EhCache CacheManager is required");
		Status status = this.cacheManager.getStatus();
		Assert.isTrue(
				Status.STATUS_ALIVE.equals(status),
				"An 'alive' EhCache CacheManager is required - current cache is "
						+ status.toString());
		String[] names = this.cacheManager.getCacheNames();
		LinkedHashSet<Cache> haset = new LinkedHashSet<Cache>(names.length);
		int i = names.length;
		for (int j = 0; j < i; ++j) {
			String str = names[j];
			Ehcache ehcache=this.cacheManager.getEhcache(str);
			haset.add(new EhCacheWrp(ehcache));
		}
		return haset;
	}


	public Cache getCache(String name) {
		//Cache cache = getCache(name);
//		if (cache == null) {
//			Ehcache ehcache = this.cacheManager.getEhcache(name);
//			if (ehcache != null) {
//				cache = new EhCacheWrp(this, ehcache);
//				addCache(cache);
//			}
//		}
//		return cache;
		
		return null;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}
