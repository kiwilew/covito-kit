package org.covito.kit.cache;

import java.util.concurrent.ConcurrentHashMap;

public interface AutoSaveHandler<K, V> {

	long getAutoSaveTime();
	
	void save(ConcurrentHashMap<K, V> map);
}
