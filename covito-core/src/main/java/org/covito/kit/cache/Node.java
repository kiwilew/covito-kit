package org.covito.kit.cache;

public final class Node<K, V> {
	K key;
	V value;
	Node<K, V> next;
	Node<K, V> prev;
	long createTime;

	public Node(K k, V v) {
		this(k, v, System.currentTimeMillis());
	}

	public Node(K k, V v, long createTime) {
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

	public void setCreateTime(long time) {
		createTime = time;
	}
}
