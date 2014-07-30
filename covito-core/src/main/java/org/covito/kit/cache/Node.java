package org.covito.kit.cache;

import java.util.ArrayList;
import java.util.List;

public final class Node<K, V> {
	private K key;
	private V value;
	private List<CacheNameItem> itemList;
	private long createTime;

	public Node(K k, V v) {
		this(k, v, System.currentTimeMillis());
	}

	private Node(K k, V v, long createTime) {
		this.key = k;
		this.value = v;
		this.createTime = createTime;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		createTime = System.currentTimeMillis();
		this.value = value;
	}

	public long getCreateTime() {
		return createTime;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.key).append(":").append(value);
		if (this.itemList != null) {
			for(CacheNameItem item:itemList){
				sb.append(",Rel[").append(item.getCacheName()).append(",").append(item.getKey())
				.append("]");
			}
		}
		return sb.toString();
	}
	
	/** 
	 * 新增关联对象
	 * <p>功能详细描述</p>
	 *
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public void addRelObject(String cacheName, Object key) {
		if (this.itemList == null){
			this.itemList = new ArrayList<CacheNameItem>(5);
		}
		CacheNameItem item = new CacheNameItem(cacheName, key);
		if (!(this.itemList.contains(item))) {
			this.itemList.add(item);
		}
	}
	
	/** 
	 * 删除关联对象
	 * <p>功能详细描述</p>
	 *
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public void removeRelObject(String cacheName, Object key) {
		if (this.itemList == null){
			this.itemList = new ArrayList<CacheNameItem>(5);
		}
		CacheNameItem item = new CacheNameItem(cacheName, key);
		if ((this.itemList.contains(item))) {
			this.itemList.remove(item);
		}
	}

	public List<CacheNameItem> getItemList() {
		return itemList;
	}
	
}
