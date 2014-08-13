package org.covito.kit.cache;


public interface AutoSaveHandler<K, V> {

	/** 
	 * 获取保存间隔时间
	 * <p>功能详细描述</p>
	 *
	 * @return
	 */
	long getAutoSaveTime();
	
	/** 
	 * 保存
	 * <p>保存成功返回true，缓存将删除此元素，当返回false时不删除</p>
	 *
	 * @param key
	 * @param value
	 * @return 是否保存成功
	 */
	boolean save(K key,V value);
}
