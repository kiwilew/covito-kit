package org.apache.commons.lang3;

import java.util.Iterator;

import org.junit.Test;

public class CharUtilsTest {

	@Test
	public void test(){
		System.out.println(CharUtils.toChar("AB"));//A
		
		CharSet set=CharSet.getInstance("ABDCDDE","34523452");
		System.out.println(set.getCharRanges().length);//9
		
		System.out.println(set.toString());//[D, B, 3, 5, E, C, A, 2, 4]
		
		//统计出现的次数
		System.out.println(CharSetUtils.count("keep", "e"));//2
		
		//删除某个字符
		System.out.println(CharSetUtils.delete("keep", "e"));//kp
		
		//挤压 多个只保留 一个
		System.out.println(CharSetUtils.squeeze("keepe", "e"));//kepe
		
		//除指定字符，把其它的都干掉
		System.out.println(CharSetUtils.keep("keepe", "e"));//eee
		
		//字符范围
		CharRange cr=CharRange.isIn('A', 'z');
		System.out.println(cr.contains('A'));
		Iterator<Character> it=cr.iterator();
		for(;it.hasNext();){
			System.out.print(it.next());//ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz
		}
		System.out.println();
		
		//是否支持此编码名字
		System.out.println(CharEncoding.isSupported("UTF-16"));//true
		System.out.println(CharEncoding.isSupported("UTF"));//false
	}

}
