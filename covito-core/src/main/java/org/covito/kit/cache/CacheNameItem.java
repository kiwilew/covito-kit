package org.covito.kit.cache;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 缓存名称和Key包装类
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014年6月17日]
 */
public class CacheNameItem implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5352670791851271569L;

	private String cacheName;
	private Object key;

	public CacheNameItem(String cacheName, Object key) {
		this.cacheName = cacheName;
		this.key = key;
	}

	public String getCacheName() {
		return this.cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Object getKey() {
		return this.key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(this.cacheName).append(this.key).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != super.getClass()) {
			return false;
		}
		CacheNameItem temp = (CacheNameItem) obj;
		return new EqualsBuilder().append(this.cacheName, temp.getCacheName())
				.append(this.key, temp.getKey()).isEquals();
	}

	@Override
	public String toString() {
		return this.cacheName + this.key;
	}
	
}