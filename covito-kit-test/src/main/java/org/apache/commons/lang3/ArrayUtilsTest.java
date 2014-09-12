package org.apache.commons.lang3;

import org.junit.Test;
import static org.apache.commons.lang3.ArrayUtils.*;

public class ArrayUtilsTest {

	@Test
	public void test(){
		String[] t=add(ArrayUtils.EMPTY_STRING_ARRAY, "2");
		System.out.println(t.length);
		System.out.println(contains(t, "3"));//false
		System.out.println(contains(t, "2"));//true
		
		System.out.println(indexOf(t, "2"));//0
		System.out.println(indexOf(t, "3"));//-1
		
		System.out.println(isEmpty(t));//false
		
		System.out.println(isSameLength(t, ArrayUtils.EMPTY_STRING_ARRAY));//false
		
		System.out.println(t.length);//1
		//删除在指定的位置从指定的数组元素。所有后续元素左移（下标减1）
		System.out.println(remove(t, 0).length);//0
		System.out.println(removeAll(t, 0).length);//0
		
		String[] a=nullToEmpty(new String[]{"a",null,"","b","a"});
		System.out.println(ArrayUtils.toString(a, ""));//{a,<null>,,b,a}
		//删除第一个出现的指定元素
		System.out.println(removeElement(a, "a").length);//4
		System.out.println(ArrayUtils.toString(a, ""));//{a,<null>,,b,a}
		//反转
		reverse(a);
		System.out.println(ArrayUtils.toString(a, ""));//{a,b,,<null>,a}
		
		System.out.println(ArrayUtils.toMap(new String[][] {
		     {"RED", "#FF0000"},
		     {"GREEN", "#00FF00"},
		     {"BLUE", "#0000FF"}
		}));//{BLUE=#0000FF, RED=#FF0000, GREEN=#00FF00}
		
	}

}
