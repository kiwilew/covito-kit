package com.covito.test;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class StringUtil {

	public static String filerString(String str) {
		char[] cs = str.toCharArray();
		Set<Character> set = new LinkedHashSet<Character>();
		for (char c : cs) {
			set.add(c);
		}
		String re = "";
		for (char c : set) {
			re += c;
		}
		return re;
	}

//	public static void main(String[] args) {
//		String s = filerString("weiroqweirqewrqewrqewrqewrqewr");
//		System.out.println(s);
//	}

	public static void main(String[] args) {
		AtomicInteger ai = new AtomicInteger(0);
		int i1 = ai.get();
		v(i1);
		int i2 = ai.getAndSet(5);
		v(i2);
		int i3 = ai.get();
		v(i3);
		int i4 = ai.getAndIncrement();
		v(i4);
		v(ai.get());

	}

	static void v(int i) {
		System.out.println("i : " + i);
	}
}
