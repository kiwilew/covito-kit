package org.covito.kit.cache.monitor;

import java.util.List;

public interface Visitor {

	/**
	 * 统计接口，获取查询次数
	 * 
	 * @return int
	 */
	long getQueryCount();

	/**
	 * 统计接口，获取命中次数
	 * 
	 * @return int
	 */
	long getHitCount();

	/**
	 * 获取当前cache的大小
	 * 
	 * @return int
	 */
	long size();

	/**
	 * 获取其它监控项
	 * 
	 * @return
	 */
	List<MonitorItem> getMonitorItem();

	/**
	 * 获取进行一次清理所需要的时间
	 * 
	 * @return
	 */
	long getReflushTime();

	/**
	 * 其它配置项
	 * @author 
	 */
	public static class MonitorItem {

		private String name;

		private String description;

		private String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}
}
