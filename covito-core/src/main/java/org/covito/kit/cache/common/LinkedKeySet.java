package org.covito.kit.cache.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

final class LinkedKeySet<K>  {

	private transient Map<K, KeyNode<K>> map;
	volatile KeyNode<K> head;
	volatile KeyNode<K> tail;

	public LinkedKeySet() {
		map = new HashMap<K, KeyNode<K>>();
	}

	public boolean isEmpty() {
		return head == null;
	}

	public void remove(K k) {
		KeyNode<K> n=map.get(k);
		if(n==null){
			return;
		}
		if (n.next != null) {
			n.next.prev = n.prev;
		} else {
			tail = n.prev;
			if (tail != null)
				tail.next = null;
		}
		if (n.prev != null) {
			n.prev.next = n.next;
		} else {
			head = n.next;
			if (head != null)
				head.prev = null;
		}
		map.remove(k);
	}

	public void add(K key) {
		KeyNode<K> n=new KeyNode<K>(key);
		if (head == null) {
			tail = head = n;
		} else {
			n.next = head;
			head.prev = n;
			head = n;
			head.prev = null;
		}
		map.put(key, n);
	}

	public void moveToTop(K k) {
		if (head != tail) {
			KeyNode<K> n=map.get(k);
			if (n.next != null) {
				n.next.prev = n.prev;
			} else {
				tail = n.prev;
				if (tail != null)
					tail.next = null;
			}
			if (n.prev != null) {
				n.prev.next = n.next;
			} else {
				head = n.next;
				if (head != null)
					head.prev = null;
			}
			if (head == null) {
				tail = head = n;
			} else {
				n.next = head;
				head.prev = n;
				head = n;
				head.prev = null;
			}
		}
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

	public void clear() {
		head = tail = null;
	}

	public final static class KeyNode<K> {
		K value;
		KeyNode<K> prev;
		KeyNode<K> next;
		
		public KeyNode(K value) {
			this.value=value;
		}
	}
	
	public static void main(String[] args) {
		LinkedKeySet<String> link=new LinkedKeySet<String>();
		link.add("aa");
		link.add("bb");
		link.add("cc");
		link.add("dd");
		
		link.moveToTop("cc");
		
		System.out.println(link.tail);
	}

	public long size() {
		return map.size();
	}

	public Iterator<K> iterator() {
		return map.keySet().iterator();
	}
	
	public Set<K> keySet(){
		return Collections.unmodifiableSet(map.keySet());
	}

}
