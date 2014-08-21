package org.covito.kit.cache.monitor;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCacheMonitor implements CacheMonitor {

	private static final Logger logger = LoggerFactory.getLogger("cacheMonitor");
	
	private Thread monitorThread;
	
	private long monitorInterval = 5 * 60 * 1000;// 5min
	
	/**
	 * 打印log的间隔
	 * @param monitorInterval
	 */
	public DefaultCacheMonitor(long monitorInterval) {
		this.monitorInterval=monitorInterval;
	}
	
	public DefaultCacheMonitor() {
	}
	
	@Override
	public void log() {
		String format="|%1$-30s|%2$-60s|%3$-12s|%4$-12s|%5$-12s|%6$-12s";
		logger.info(String.format(format, new Object[]{"name","class","size","queryCount","hitCount","reflushTime"}));
		for(String name:CacheManager.getCacheNames()){
			Cache<?, ?> c = CacheManager.getCache(name);
			if (c instanceof Visitor) {
				Visitor v=((Visitor) c);
				long hitCount = v.getHitCount();
				long size = v.size();
				long queryCount = v.getQueryCount();
				long reflushTime = v.getReflushTime();
				logger.info(String.format(format, new Object[]{name,c.getClass().getName(),size,hitCount,queryCount,reflushTime}));
			} 
		}
		
		
	}

	@Override
	public void start() {
		monitorThread = new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(monitorInterval);
						log();
					} catch (Exception e) {
						logger.error("DefaultCacheMonitor run error: ", e);
					}
				}
			}
		};
		try {
			monitorThread.start();
		} catch (Exception e) {
			logger.error("DefaultCacheMonitor monitor thread start error: ", e);
		}
	}
	
}
