package org.covito.kit.cache.common;

import org.covito.kit.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheWrp<K, V> {
	
	final Logger logger = LoggerFactory.getLogger(CacheWrp.class);

	private Cache<K, V> cache;

	private long lastcleanUpTime = System.currentTimeMillis();
	
	private long lastSaveTime = System.currentTimeMillis();
	
	private long lastRefreshTime = System.currentTimeMillis();
	
	/**
	 * @param cache
	 * @param checkInterval 检查间隔
	 */
	public CacheWrp(Cache<K, V> cache) {
		if (cache == null) {
			throw new RuntimeException("cache can't be null");
		}
		this.cache = cache;
	}

	public Cache<K, V> getCache() {
		return cache;
	}

	void setCache(Cache<K, V> c) {
		cache = c;
	}

	public Runnable cleanUp() {
		if(cache instanceof AbsCacheImpl){
			final AbsCacheImpl<K, V> ct=(AbsCacheImpl<K, V>)cache;
			if (System.currentTimeMillis() - this.lastcleanUpTime < ct.getCheckInterval()) {
				return null;
			}
			try {
				return new Runnable() {
					@Override
					public void run() {
						ct.cleanUp();
					}
				};
			} finally {
				lastcleanUpTime = System.currentTimeMillis();
			}
		}
		return null;
	}
	
	public Runnable saveCheck() {
		if(cache instanceof AbsCacheImpl){
			final AbsCacheImpl<K, V> ct=(AbsCacheImpl<K, V>)cache;
			if(ct.getAutoSaveHandler()==null){
				return null;
			}
			if (System.currentTimeMillis() - this.lastSaveTime < ct.getAutoSaveHandler().getAutoSaveTime()) {
				return null;
			}
			try {
				return new Runnable() {
					@Override
					public void run() {
						ct.autoSaveHandler();
					}
				};
			} finally {
				lastSaveTime = System.currentTimeMillis();
			}
		}
		return null;
	}
	
	public Runnable refreshCheck() {
		if(cache instanceof AbsCacheImpl){
			final AbsCacheImpl<K, V> ct=(AbsCacheImpl<K, V>)cache;
			if(ct.getAutoRefreshHandler()==null){
				return null;
			}
			if (System.currentTimeMillis() - this.lastRefreshTime < ct.getAutoRefreshHandler().getAutoRefreshTime()) {
				return null;
			}
			try {
				return new Runnable() {
					@Override
					public void run() {
						ct.autoRefreshHandler();
					}
				};
			} finally {
				lastRefreshTime = System.currentTimeMillis();
			}
		}
		return null;
	}

}
