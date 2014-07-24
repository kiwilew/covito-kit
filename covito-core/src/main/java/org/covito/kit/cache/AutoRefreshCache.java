package org.covito.kit.cache;

public interface AutoRefreshCache<K, V> extends Cache<K, V> {

	long getReflushTime();
	
}
