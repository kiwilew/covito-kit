package org.covito.kit.cache.monitor;

import java.util.List;


public interface Visitor {
	
	/**
	 * 统计接口，获取查询次数
	 * @return int 
	 */
	long getQueryCount();
	
	/**
	 * 统计接口，获取命中次数
	 * @return int 
	 */
	long getHitCount();
	
	/**
	 * 统计接口，获取该cache占用的总内存大小
	 * @return	若支持，则返回内存大小，否则返回-1
	 */
	long getMemoryUsage();
	
	
	/**	
	 * 获取当前cache的大小
	 * @return int 
	 */
	long size();
	
	/**
	 * 获取其它监控项
	 * @return 
	 */
	List<MonitorItem> getMonitorItem();
	
}
