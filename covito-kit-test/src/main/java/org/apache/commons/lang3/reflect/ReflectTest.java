package org.apache.commons.lang3.reflect;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import org.junit.Test;

public class ReflectTest {

	@Test
	public void test() throws Exception{
		Constructor<Mo> con=ConstructorUtils.getMatchingAccessibleConstructor(Mo.class, String.class);
		
		System.out.println(con.newInstance("aaa").getName());
		
		Mo m=ConstructorUtils.invokeConstructor(Mo.class, "bb");
		
		System.out.println(m.getName());
		
		String name=FieldUtils.readDeclaredField(m, "ext").toString();
		
		System.out.println(name);
		
		System.out.println(FieldUtils.readStaticField(Mo.class, "stat"));
		
		
		
	}
	
	public static class Mo implements Serializable,Cloneable{
		
		public static String stat="static";
		
		private String name;
		
		public String ext="a";
		
		public Mo() {
		}
		
		public Mo(String name) {
			this.name=name;
		}

		public String getName() {
			return name;
		}
		
	}
}
