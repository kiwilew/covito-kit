package org.covito.kit.cache.monitor;

import java.util.List;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCacheMonitor implements CacheMonitor {

	private static final Logger logger = LoggerFactory.getLogger("cacheMonitor");
	
	private Thread monitorThread;
	
	private static final int monitorInterval = 5 * 60 * 1000;// 5min
	
	@Override
	public void log() {
		
		for(String name:CacheManager.getCacheNames()){
			Cache<?, ?> c = CacheManager.getCache(name);
			logger.info("\t name \t|\t impclassName \t|\t size \t|\t  hitCount \t|\t  queryCount  \t|\t  memory \t|\t  else");
			if (c instanceof Visitor) {
				Visitor v=((Visitor) c);
				long hitCount = v.getHitCount();
				long size = v.size();
				long queryCount = v.getQueryCount();
				long memory = v.getMemoryUsage();
				List<MonitorItem> items=v.getMonitorItem();
				String is="";
				if(items!=null&&items.size()>0){
					for(MonitorItem i:items){
						is+="\t"+i.getName()+":"+i.getValue();
					}
				}
				logger.info("\t" + name + "\t" + c.getClass().getName() + "\t" + size + "\t" + hitCount + "\t" + queryCount + "\t" + memory + is);
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
