package org.covito.kit.cache;

import java.util.Set;

import org.springframework.cache.Cache.ValueWrapper;

public interface Cache<K,V> {
	
	/**
	 * Return the cache name.
	 */
	String getName();

	/**
	 * Return the the underlying native cache provider.
	 */
	Object getNativeCache();

	/**
	 * Return the value to which this cache maps the specified key.
	 * <p>Returns {@code null} if the cache contains no mapping for this key;
	 * otherwise, the cached value (which may be {@code null} itself) will
	 * be returned in a {@link ValueWrapper}.
	 * @param key the key whose associated value is to be returned
	 * @return the value to which this cache maps the specified key,
	 * contained within a {@link ValueWrapper} which may also hold
	 * a cached {@code null} value. A straight {@code null} being
	 * returned means that the cache contains no mapping for this key.
	 */
	V get(K key);

	/**
	 * Associate the specified value with the specified key in this cache.
	 * <p>If the cache previously contained a mapping for this key, the old
	 * value is replaced by the specified value.
	 * @param key the key with which the specified value is to be associated
	 * @param value the value to be associated with the specified key
	 */
	void put(K key, V value);

	/**
	 * Evict the mapping for this key from this cache if it is present.
	 * @param key the key whose mapping is to be removed from the cache
	 */
	void evict(K key);

	/**
	 * Remove all mappings from the cache.
	 */
	void clear();

	/**
	 * 获取所有的key
	 * @return
	 */
	Set<K> keySet();
}
