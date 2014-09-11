package org.covito.kit.test.clz;

import java.lang.reflect.Type;

public class ReTest {
	
	public static void main(String[] args) {
		SonTest a=new SonTest();
		Type t=a.getClass().getGenericSuperclass();
		System.out.println(t);
	}

	public static class ClaTest<T>{
		
	}
	
	public static class SonTest extends ClaTest<String>{
		
	}
}
