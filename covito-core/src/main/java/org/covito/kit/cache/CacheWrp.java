package org.covito.kit.cache;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CacheWrp<K, V> {
	private static final Logger logger = LoggerFactory.getLogger(CacheWrp.class);

	private Cache<K, V> cache;

	private long lastCheckTime = System.currentTimeMillis();

	private long checkInterval = 1000 * 60 * 60;//默认1小时

	public CacheWrp(Cache<K, V> cache, long checkInterval) {
		if (cache == null) {
			throw new RuntimeException("cache can't be null");
		}
		this.cache = cache;
		this.checkInterval = checkInterval;
		if (this.checkInterval <= 0){
			this.checkInterval = Long.MAX_VALUE;
		}
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

		if (System.currentTimeMillis() - this.lastCheckTime < this.checkInterval) {
			return false;
		}
		long n = 0;
		try {
			n = cache.cleanUp();
		} finally {
			lastCheckTime = System.currentTimeMillis();
			logger.debug("cleanUp, remove " + n + " nodes.");
		}
		return true;
	}

}
