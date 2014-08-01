package com.covito.test;

import java.util.LinkedHashSet;
import java.util.Set;

public class ListTest {

	public static void main(String[] args) {
		Set<String> set=new LinkedHashSet<String>() ;
				//new TreeSet<String>();
				//new HashSet<String>();
				
		
		set.add("a");
		set.add("b");
		set.add("c");
		set.add("a");
		set.add("b");
		System.out.println(set);
		
	}
}
