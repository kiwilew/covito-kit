package org.covito.kit.cache;

public interface AutoSaveCache<K, V> extends Cache<K, V> {

	long getSaveTime();
	
	void save();
	
}
