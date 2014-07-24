package org.covito.kit.cache;

public interface ItemHandler<K, V> {

	boolean handle(Node<K, V> n, boolean isTimeout);
}
