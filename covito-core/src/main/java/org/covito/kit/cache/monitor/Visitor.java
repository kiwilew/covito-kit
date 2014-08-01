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
