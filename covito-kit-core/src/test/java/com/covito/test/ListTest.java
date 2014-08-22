package com.covito.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
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
		
		
		Map<String,String> map=new HashMap<String, String>();
		map.put("a", "a");
//		Map b=Collections.unmodifiableMap(map);
		Map<String,String>  b=new HashMap<String, String>(map);
		map.put("b", "b");
		System.out.println(b);
		
	}
}
