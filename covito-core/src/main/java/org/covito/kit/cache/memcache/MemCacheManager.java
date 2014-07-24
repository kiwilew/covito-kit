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

import java.util.Collection;

import org.covito.kit.cache.Cache;

/**
 * MemCachedManager 
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年6月18日]
 */
public class MemCacheManager {

	private Collection<? extends MemCacheWrp> caches;
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @return
	 */
	protected Collection<? extends Cache> loadCaches() {
		return caches;
	}

//	public void setCaches(Collection<? extends MemCacheWrp> caches) {
//		this.caches = caches;
//		for(MemCacheWrp cache:caches){
//			cache.setCacheManager(this);
//			addCache(cache);
//		}
//	}

}
