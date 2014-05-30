package com.covito.kit.core;

/**
 * <过滤功能类><br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-20]
 */
public abstract class Filter<T> {
	protected Object Param;

	public Filter() {
	}

	public Filter(Object param) {
		this.Param = param;
	}

	public abstract boolean filter(T paramT);
}
