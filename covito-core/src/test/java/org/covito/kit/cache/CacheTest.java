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

import java.text.MessageFormat;
import java.util.Date;

import org.covito.kit.BaseSpringTest;
import org.covito.kit.cache.monitor.CacheMonitor;
import org.covito.kit.cache.monitor.DefaultCacheMonitor;
import org.covito.kit.cache.monitor.Visitor;
import org.covito.kit.cache.support.MapCache;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014年6月18日]
 */
public class CacheTest extends BaseSpringTest {

	@Test
	@Ignore
	public void ehcache() {
		CacheManager cm = (CacheManager) getBean("cacheManager");

		System.out.println(cm.getCacheNames());

		Cache roleCache = cm.getCache("role");
		Cache userCache = cm.getCache("user");

		roleCache.put("1", "3434");

		userCache.put("u1", "u1");

		// cm.setCacheRel("user", "u1", new CacheNameItem("role", "1"));

		System.out.println(roleCache.get("1"));
		System.out.println(userCache.get("u1"));
		roleCache.evict("1");
		System.out.println(userCache.get("u1"));
	}

	@Test
	@Ignore
	public void memcache() {
		CacheManager cm = (CacheManager) getBean("memCacheManager");

		System.out.println(cm.getCacheNames());

		Cache roleCache = cm.getCache("role");
		Cache userCache = cm.getCache("user");

		roleCache.put("1", "3434");
		userCache.put("u1", "u1");
		// cm.setCacheRel("user", "u1", new CacheNameItem("role", "1"));

		System.out.println(roleCache.get("1"));
		System.out.println(userCache.get("u1"));
		roleCache.evict("1");
		System.out.println(userCache.get("u1"));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	protected String[] getXmlPath() {
		return new String[] { "classpath*:site-cache.xml" };
	}
	
	@Test
	public void testString(){
		System.out.println(String.format("%1$-18d%2$-18s", -31,45));
		
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
		// ca.setTimeout(1000*2);
		// ca.setVisitTimeout(1000*1);

		CacheManager.addCache(ca, 1000 * 5);

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
