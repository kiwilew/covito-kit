package org.covito.kit.cache;

import org.covito.kit.cache.common.Node;

/**
 * 节点超时处理Handler
 * 
 * 用于定制在特定业务中某些实体属性特殊时排除清理范围
 *
 * @param <K>
 * @param <V>
 */
public interface EliminateHandler<K, V> {

	/**
	 * 超时处理
	 * @param n
	 * @return
	 */
	boolean onTimeOut(Node<K, V> n);
	
	/**
	 * 访问时间超时处理
	 * @param n
	 * @return
	 */
	boolean onVisitTimeOut(Node<K, V> n);
	
	/**
	 * 剩余允许大小个数所占比例超过清除率
	 * @param n
	 * @return
	 */
	boolean onCleanUpRate(Node<K, V> n);
}
