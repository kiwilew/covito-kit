package org.covito.kit.utility;

import java.io.Serializable;

/**
 * 值对 方法需要返回两个值时，可使用Pair减少代码量
 * 
 * @author meteor
 * 
 * @param <F>
 * @param <S>
 */
@SuppressWarnings("serial")
public final class Pair<F, S> implements Serializable {
	public F first;
	public S second;

	public Pair() {
	}

	public Pair(F f, S s) {
		this.first = f;
		this.second = s;
	}

	/**
	 * 通过值创建值对
	 * 
	 * @param f
	 *            第一个值
	 * @param s
	 *            第二个值
	 * @return 值对
	 */
	public static <FT, ST> Pair<FT, ST> makePair(FT f, ST s) {
		return new Pair<FT, ST>(f, s);
	}

	private static <T> boolean eq(T o1, T o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		Pair<F, S> pr = (Pair<F, S>) o;
		if (pr == null)
			return false;
		return eq(first, pr.first) && eq(second, pr.second);
	}

	private static int h(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	public int hashCode() {
		int seed = h(first);
		seed ^= h(second) + 0x9e3779b9 + (seed << 6) + (seed >> 2);
		return seed;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public void setSecond(S second) {
		this.second = second;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(first).append(", ").append(second).append("}");
		return sb.toString();
	}
}
