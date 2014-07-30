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

import org.covito.kit.BaseSpringTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 一句话功能简述
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年6月18日]
 */
public class CacheTest extends BaseSpringTest {
	
	

	@Test
	@Ignore
	public void ehcache(){
		CacheManager cm=(CacheManager)getBean("cacheManager");
		
		System.out.println(cm.getCacheNames());
		
		Cache roleCache =cm.getCache("role");
		Cache userCache =cm.getCache("user");
		
		roleCache.put("1", "3434");
		
		userCache.put("u1", "u1");
		
//		cm.setCacheRel("user", "u1", new CacheNameItem("role", "1"));
		
		System.out.println(roleCache.get("1"));
		System.out.println(userCache.get("u1"));
		roleCache.evict("1");
		System.out.println(userCache.get("u1"));
	}
	
	@Test
	public void memcache(){
		CacheManager cm=(CacheManager)getBean("memCacheManager");
		
		System.out.println(cm.getCacheNames());
		
		Cache roleCache =cm.getCache("role");
		Cache userCache =cm.getCache("user");
		
		roleCache.put("1", "3434");
		userCache.put("u1", "u1");
//		cm.setCacheRel("user", "u1", new CacheNameItem("role", "1"));
		
		System.out.println(roleCache.get("1"));
		System.out.println(userCache.get("u1"));
		roleCache.evict("1");
		System.out.println(userCache.get("u1"));
	}
	

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @return
	 */
	@Override
	protected String[] getXmlPath() {
		return new String[]{"classpath*:site-cache.xml"};
	}
}
