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

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.monitor.MonitorItem;
import org.covito.kit.cache.monitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象缓存实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public abstract class AbsCacheImpl<K,V> implements Cache<K,V>,Visitor {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final Object NULL_HOLDER = new NullHolder();
	
	/**
	 * 是否允许空值
	 */
	private boolean allowNullValues = true;
	
	
	protected AtomicLong queryCount = new AtomicLong();					// 查询次数
	
	protected AtomicLong hitCount = new AtomicLong();	
	
	protected AtomicLong size = new AtomicLong();	
	
	@Override
	public long getHitCount() {
		return hitCount.get();
	}
	
	
	@Override
	public long getQueryCount() {
		return queryCount.get();
	}
	
	
	@Override
	public long getMemoryUsage() {
		return -1;
	}
	
	@Override
	public long size() {
		return size.get();
	}
	
	
	
	
	/** 
	 * 空对象解包
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param storeValue
	 * @return
	 */
	protected Object fromStoreValue(Object storeValue) {
		if (this.allowNullValues && (storeValue instanceof NullHolder)) {
			return null;
		}
		return storeValue;
	}
	
	/** 
	 * 空对象包装
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param userValue
	 * @return
	 */
	protected Object toStoreValue(Object userValue) {
		if ((this.allowNullValues) && (userValue == null)){
			return NULL_HOLDER;
		}
		return userValue;
	}
	
	public static class NullHolder implements Serializable {
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

	}
	
	@Override
	public List<MonitorItem> getMonitorItem() {
		return null;
	}
	
	@Override
	public long cleanUp() {
		// TODO Auto-generated method stub
		return 0;
	}
}
