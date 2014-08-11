package org.covito.kit.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 隔一段时间全部刷新缓存中的元素
 *
 * @param <K>
 * @param <V>
 */
public interface AutoRefreshHandler<K, V> {

	/** 
	 * 获取刷新时间间隔
	 * @return
	 */
	long getAutoRefreshTime();
	
	/**
	 * 获取刷新元素
	 * @return
	 */
	ConcurrentHashMap<K, V> refresh();
}
