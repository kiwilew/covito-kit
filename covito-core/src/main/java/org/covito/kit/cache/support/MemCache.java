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

import org.covito.kit.cache.common.AbsCacheImpl;
import org.covito.kit.cache.common.Node;

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
public class MemCache<K, V> extends AbsCacheImpl<K, V> {

	private String name;
	private MemCachedClient memcachedClient;

	/**
	 * Constructor
	 */
	public MemCache(String name,MemCachedClient memcachedClient) {
		this.name=name;
		this.memcachedClient=memcachedClient;
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
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
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
	 */
	@Override
	public void removeAll() {
		this.memcachedClient.flushAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Node<K, V> getNode(K key) {
		Node<K, V> value = (Node<K, V>) this.memcachedClient.get(generateKey(key));
		return value;
	}

	@Override
	protected void putNode(Node<K, V> n) {
		this.memcachedClient.add(generateKey(n.getKey()), n);
	}

	@Override
	protected void removeNode(K key) {
		this.memcachedClient.delete(generateKey(key));
	}

}
