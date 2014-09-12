package org.apache.commons.lang3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;


public class AnnotationUtilsTest {

	@Test
	public void test(){
		Anno an=Mo.class.getAnnotation(Anno.class);
		
		System.out.println(an.toString());//@org.apache.commons.lang3.AnnotationUtilsTest$Anno(value=this is a class)
		//妈蛋，有毛病，一样的写个毛啊
		System.out.println(AnnotationUtils.toString(an));;//@org.apache.commons.lang3.AnnotationUtilsTest$Anno(value=this is a class)
		
		System.out.println(AnnotationUtils.isValidAnnotationMemberType(Mo.class));;//false
		System.out.println(AnnotationUtils.isValidAnnotationMemberType(Anno.class));;//true
	}

	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Anno{
		String value();
	}
	
	@Anno(value = "this is a class")
	public static class Mo{
		@Anno(value = "oh,is filed")
		String name;
		
	}
}
