package org.covito.kit.cache.monitor;

/**
 * 缓存监控者
 *
 */
public interface CacheMonitor {

	/**
	 * 记录日志
	 */
	void log();

	/**
	 * 启动
	 */
	void start();
}
