package org.covito.kit.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 维护key与其它缓存的关联
 * <p>当删除此key时会删除所有的关联缓存</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年6月17日]
 */
public class KeyListRel implements Serializable {
	private static final long serialVersionUID = 1299645041971703402L;
	
	private final Object key;
	
	private List<CacheNameItem> itemList = null;

	public KeyListRel(Object key) {
		this.key = key;
	}

	public Object get() {
		return this.key;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.key);
		if (this.itemList != null) {
			for(CacheNameItem item:itemList){
				sb.append("[").append(item.getCacheName()).append(",").append(item.getKey())
				.append("]");
			}
		}
		return sb.toString();
	}

	public List<CacheNameItem> getRelObject() {
		return this.itemList;
	}

	/** 
	 * 新增关联对象
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public boolean addRelObject(String cacheName, Object key) {
		if (this.itemList == null){
			this.itemList = new ArrayList<CacheNameItem>(5);
		}
		CacheNameItem item = new CacheNameItem(cacheName, key);
		if (!(this.itemList.contains(item))) {
			this.itemList.add(item);
			return true;
		}
		return false;
	}

	public static void main(String[] paramArrayOfString) {
		CacheNameItem i1 = new CacheNameItem("a", "b");
		CacheNameItem i2 = new CacheNameItem("a", "b");
		CacheNameItem i3 = new CacheNameItem("a1", "c");
		
		System.out.println(i1.equals(i2));
		System.out.println(i1.equals(i3));
		System.out.println(i1.equals(null));
		
		KeyListRel kr=new KeyListRel("code");
		kr.addRelObject("a", "b");
		kr.addRelObject("a", 2);
		System.out.println(kr);
		
	}
}