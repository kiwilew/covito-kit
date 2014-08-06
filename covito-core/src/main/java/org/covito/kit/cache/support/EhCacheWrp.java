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
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.common.AbsCacheImpl;
import org.covito.kit.cache.common.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * EhCache包装类
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014年6月10日]
 */
public class EhCacheWrp<K,V> extends AbsCacheImpl<K,V> {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final Ehcache cache;
	
	/**
	 * Constructor
	 * 
	 * @param cacheManager
	 * @param cache
	 */
	public EhCacheWrp( Ehcache cache) {
		Assert.notNull(cache, "Ehcache must not be null");
		Status status = cache.getStatus();
		Assert.isTrue(Status.STATUS_ALIVE.equals(status),
				"An 'alive' Ehcache is required - current cache is " + status.toString());
		this.cache = cache;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	public String getName() {
		return cache.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@Override
	public Object getNativeCache() {
		return this.cache;
	}


	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 */
	@Override
	public void removeAll() {
		this.cache.removeAll();
	}

	@Override
	protected Node<K, V> getNode(K key) {
		Element ele = this.cache.get(key);
		return (Node<K, V>)ele.getObjectValue();
	}

	@Override
	protected void putNode(Node<K, V> n) {
		this.cache.put(new Element(n.getKey(), n));
	}

	@Override
	protected void removeNode(K key) {
		this.cache.remove(key);
	}

}
