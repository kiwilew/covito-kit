package org.covito.kit.cache.monitor;

import java.util.List;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.KeyNotFoundHandler;
import org.covito.kit.cache.monitor.Visitor.MonitorItem;
import org.covito.kit.cache.support.MapCache;
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
		String format="|%1$-30s|%2$-60s|%3$-12s|%4$-12s|%5$-12s|%6$-12s|%7$-30s";
		logger.info(String.format(format, new Object[]{"name","class","size","queryCount","hitCount","reflushTime","elseInfo"}));
		for(String name:CacheManager.getCacheNames()){
			Cache<?, ?> c = CacheManager.getCache(name);
			if (c instanceof Visitor) {
				Visitor v=((Visitor) c);
				long hitCount = v.getHitCount();
				long size = v.size();
				long queryCount = v.getQueryCount();
				long reflushTime = v.getReflushTime();
				List<MonitorItem> items=v.getMonitorItem();
				String is="";
				if(items!=null&&items.size()>0){
					for(MonitorItem i:items){
						is+="["+i.getName()+":"+i.getValue()+"]";
					}
				}
				logger.info(String.format(format, new Object[]{name,c.getClass().getName(),size,hitCount,queryCount,reflushTime,is}));
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
	
	public static void main(String[] args) {
		MapCache<String, String> ca = new MapCache<String, String>("cach");
		MapCache<String, String> ca1 = new MapCache<String, String>("cache");
		CacheManager.addCache(ca, 1000 * 5);
		CacheManager.addCache(ca1, 1000 * 5);
		new DefaultCacheMonitor().log();
	}
	
}
