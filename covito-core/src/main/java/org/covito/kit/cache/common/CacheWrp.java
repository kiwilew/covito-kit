package org.covito.kit.cache.common;

import java.util.Random;

import org.covito.kit.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheWrp<K, V> {
	private static final Logger logger = LoggerFactory.getLogger(CacheWrp.class);

	private Cache<K, V> cache;

	private long lastCheckTime = System.currentTimeMillis();

	/**
	 * @param cache
	 * @param checkInterval 检查间隔 
	 */
	public CacheWrp(Cache<K, V> cache, long checkInterval) {
		if (cache == null) {
			throw new RuntimeException("cache can't be null");
		}
		this.cache = cache;
	}
	
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

	/**
	 * @param cache
	 * @param checkInterval
	 * @param peakShiftingRange 随机区间  检查时间推迟？秒
	 */
	public CacheWrp(Cache<K, V> cache, long checkInterval, int peakShiftingRange) {
		this(cache, checkInterval);
		Random r = new Random();
		int shift = r.nextInt(peakShiftingRange);
		logger.debug("peakShifting: " + shift);
		lastCheckTime += shift * 1000;
	}

	public Cache<K, V> getCache() {
		return cache;
	}

	void setCache(Cache<K, V> c) {
		cache = c;
	}

	public boolean cleanUp() {
		if(cache instanceof AbsCacheImpl){
			AbsCacheImpl<K, V> ct=(AbsCacheImpl<K, V>)cache;
			if (System.currentTimeMillis() - this.lastCheckTime < ct.getCheckInterval()) {
				return false;
			}
			long n = 0;
			try {
				n = cache.cleanUp();
			} finally {
				lastCheckTime = System.currentTimeMillis();
				logger.debug("cleanUp, remove " + n + " nodes.");
			}
		}
		return true;
	}

}
