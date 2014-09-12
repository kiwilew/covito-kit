package org.apache.commons.lang3.text;

import java.util.Formattable;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TextUtilsTest {

	@Test
	public void test(){
		
		//大写首字母
		System.out.println(WordUtils.capitalize("wo shi test"));
		
		//提取字符串中每个词的首字母
		System.out.println(WordUtils.initials("Josh Nash"));
		
		//大小写互转
		System.out.println(WordUtils.swapCase("xiao XIE"));
		
		Map<String, Object> value=new HashMap<String, Object>();
		value.put("name", "Mr Make");
		
		System.out.println(StrSubstitutor.replace("hollo! ${name} ", value));
		
		
		StrMatcher sm=StrMatcher.stringMatcher("mike");
		int num=sm.isMatch("\"name\" is mike!".toCharArray(), 0);
		System.out.println(num);
		
		System.out.println(ExtendedMessageFormat.format("{0},{1}", "3","4"));;
	}
}
