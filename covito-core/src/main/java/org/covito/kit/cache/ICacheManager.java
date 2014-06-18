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

import org.covito.kit.cache.common.CacheNameItem;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 缓存管理者
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-9]
 */
public interface ICacheManager extends CacheManager {

	/** 
	 * 是否支持缓存查询
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @return
	 */
	boolean isSupportQueryCache();

	
	/** 
	 * 设置缓存关联关系
	 * <p>当relCache被删除时，级联删此缓存</p>
	 *
	 * @author  covito
	 * @param cacheName
	 * @param key
	 * @param relCache
	 */
	void setCacheRel(String cacheName,String key,CacheNameItem item);


	/** 
	 * 获取关联缓存
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @return
	 */
	Cache getRelCache();


	/** 
	 * 生成关联缓存Key
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param cacheName
	 * @param key
	 * @return
	 */
	String generateRelCacheKey(String cacheName, Object key);
}
