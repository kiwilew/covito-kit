package org.covito.kit.cache.hashmap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.covito.kit.cache.common.AbsCacheImpl;
import org.covito.kit.cache.common.Node;

public class MapCache<K,V> extends AbsCacheImpl<K, V>{

	private Map<K, Node<K, V> > map = new ConcurrentHashMap<K, Node<K, V> >();
	
	private String name;
	
	public MapCache(String name) {
		this.name=name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return map;
	}

	@Override
	protected Node<K, V> getNode(K key) {
		return map.get(key);
	}

	@Override
	protected void putNode(Node<K, V> n) {
		map.put(n.getKey(), n);
	}

	@Override
	protected void removeNode(K key) {
		map.remove(key);
	}

	@Override
	protected void removeAll() {
		map.clear();
	}

}
