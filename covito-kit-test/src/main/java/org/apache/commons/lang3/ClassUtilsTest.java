package org.apache.commons.lang3;

import static org.apache.commons.lang3.ClassUtils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

public class ClassUtilsTest {

	@Test
	public void test(){
		List<Class<?>> list=new ArrayList<Class<?>>();
		list.add(String.class);
		list.add(Integer.class);
		List<String> names=convertClassesToClassNames(list);
		System.out.println(names);//[java.lang.String, java.lang.Integer]
		
		System.out.println(convertClassNamesToClasses(names));//[class java.lang.String, class java.lang.Integer]
		
		//获取实现的接口
		System.out.println(getAllInterfaces(Mo.class));//[interface java.io.Serializable, interface java.lang.Cloneable]
		
		System.out.println(getAllSuperclasses(Mo.class));//[class java.lang.Object]
		
		System.out.println(getPackageCanonicalName(Mo.class));//org.apache.commons.lang3
		
		System.out.println(getPackageName(Mo.class));//org.apache.commons.lang3
		
		System.out.println(getShortCanonicalName(Mo.class));//ClassUtilsTest.Mo
		
		System.out.println(getSimpleName(Mo.class));//Mo
		
		try {
			//public void org.apache.commons.lang3.ClassUtilsTest$Mo.setName(java.lang.String)
			System.out.println(getPublicMethod(Mo.class, "setName", String.class));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		System.out.println(isInnerClass(Mo.class));//true
		
		System.out.println(isPrimitiveOrWrapper(Mo.class));//false
		
	}
	
	public static class Mo implements Serializable,Cloneable{
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
		}
		
	}
}
