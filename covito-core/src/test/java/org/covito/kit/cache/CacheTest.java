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

import org.covito.kit.cache.monitor.CacheMonitor;
import org.covito.kit.cache.monitor.DefaultCacheMonitor;
import org.covito.kit.cache.monitor.Visitor;
import org.covito.kit.cache.support.MapCache;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014年6月18日]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:site-cache.xml")
public class CacheTest {
	

	@Test
	public void spring() {

		System.out.println(CacheManager.getCacheNames());

		Cache<String,String> roleCache = CacheManager.getCache("role");
		Cache<String,String> userCache = CacheManager.getCache("user");
		Cache<String,String> roleUser = CacheManager.getCache("roleUser");

		roleCache.put("1", "3434");

		userCache.put("u1", "u1");
		
		roleUser.put("aa", "bb");
		
		CacheManager.setCacheRel(roleUser.getName(), "aa", userCache.getName(), "u1");

		System.out.println(roleCache.get("1"));
		System.out.println(userCache.get("u1"));
		System.out.println(roleUser.get("aa"));
		userCache.evict("u1");
		System.out.println(userCache.get("u1"));
		System.out.println(roleUser.get("aa"));
	}

	

	
	@Test
	@Ignore
	public void testHashMap() {
		MapCache<String, String> ca = new MapCache<String, String>("cache_testA");

		ca.setKeyNotFound(new KeyNotFoundHandler<String, String>() {
			@Override
			public String onKeyNotFound(String key) {
				return "unkown";
			}
		});

		ca.setCleanupRate(0.5);
		ca.setMaxSize(4);
		ca.setCheckInterval(5*1000);
		// ca.setTimeout(1000*2);
		// ca.setVisitTimeout(1000*1);

		CacheManager.addCache(ca);

		Cache<String, String> c = CacheManager.getCache("cache_testA");
		c.put("aa", "aa");
		c.put("bb", "bb");
		c.put("cc", "cc");
		c.put("dd", "dd");
		c.put("ee", "ee");

		System.out.println(c.get("bb"));

		System.out.println(((Visitor) c).size());

		// c.evict("aa");

		// System.out.println(((Visitor)c).size());

		CacheMonitor cmonitor = new DefaultCacheMonitor(1000 * 10);

		cmonitor.start();

		try {
			Thread.sleep(1000 * 90);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(((Visitor) c).size());

		System.out.println(c.get("aa"));
	}
}
