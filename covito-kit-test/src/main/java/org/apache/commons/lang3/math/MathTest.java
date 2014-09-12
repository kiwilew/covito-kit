package org.apache.commons.lang3.math;

import org.junit.Test;

public class MathTest {

	@Test
	public void test(){
		System.out.println(NumberUtils.isDigits("234L"));//false
		System.out.println(NumberUtils.isNumber("234L"));//true
		
		System.out.println(NumberUtils.createBigDecimal("3"));
		
		//分数
		Fraction f=Fraction.getFraction("1/4");
		Fraction f1=Fraction.getFraction("1/2");
		//f1+f
		System.out.println(f.add(f1));//3/4
		//f/f1
		System.out.println(f.divideBy(f1));//1/2
		//f1*f
		System.out.println(f.multiplyBy(f1));//1/8
		//f-f1
		System.out.println(f.subtract(f1));
		
	}
}
