package org.covito.kit.cache;

/**
 * 缓存获取时Key没有找到Handler
 *
 * @param <K>
 * @param <V>
 */
public interface KeyNotFoundHandler<K, V> {

	V onKeyNotFound(K key);
	
}
