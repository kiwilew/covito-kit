package org.apache.commons.lang3;

import org.junit.Test;
import static org.apache.commons.lang3.StringUtils.*;
import static java.lang.System.*;

public class StringUtilsTest {

	@Test
	public void test(){
		//省略
		String str="abcdefghij";
		out.println(abbreviate(str, 8));//abcde...
		out.println(abbreviate(str, 7));//abcd...
		out.println(abbreviate(str, 6));//abc...
		out.println(abbreviate(str, 5));//ab...
		out.println(abbreviate(str, 4));//a...
		
		//首字母大写
		out.println(capitalize(str));//Abcdefghij
		out.println(capitalize("adDd"));//AdDd
		
		//填充两边
		out.println("^"+center("a", 5)+"$");//^  a  $
		out.println("^"+center("a", 5, '.')+"$");//^..a..$
		
		//填充左边
		out.println("^"+leftPad("a", 5)+"$");//^    a$
		out.println("^"+leftPad("a", 5, '.')+"$");//^....a$
		
		//填充右边
		out.println("^"+rightPad("a", 5)+"$");//^a    $
		out.println("^"+rightPad("a", 5, '.')+"$");//^a....$
		
		//删除一个换行符
		out.println(chomp(str));//abcdefghij
		out.println(chomp(str+"\n\n"));//abcdefghij\n
		
		//截掉最后一个字符
		out.println(chop(str));//abcdefghi
		
		//是否(不)包含
		out.println(contains(str, "ad"));//false
		out.println(containsAny(str, "ad"));//true
		out.println(containsAny(str, 'a','d'));//true
		out.println(containsNone(str, 'r','p'));//true
		out.println(containsNone(str, "ad"));//false
		
		//是否字符串只由某几个字符构成
		out.println(containsOnly("ababa", "ab"));//true
		out.println(containsOnly("ababa", "a"));//false
		
		//是否包含空格
		out.println(containsWhitespace("a b"));//true
		out.println(containsWhitespace("ab"));//false
		
		//统计出现的次数
		out.println(countMatches(str, "ab"));//1
		out.println(countMatches("babab", "ab"));//2
		
		//当为空的时候取的默认值
		out.println(defaultString(null, "unkown"));//unkown
		out.println(defaultString(str, "unkown"));//abcdefghij
		out.println(defaultIfBlank("", "blank"));//blank
		out.println(defaultIfEmpty("", "blank"));//blank
		out.println(defaultIfEmpty(null, "blank"));//blank
		
		//删除字符串中的空格
		out.println(deleteWhitespace("a b"));//ab
		
		//相对于第一个字符串而言，第二个字符的差别
		out.println(difference("abe", "abcd"));//cd
		
		//是否以*结尾
		System.out.println(endsWith(str, "ij"));//true
		System.out.println(endsWith(str, "a"));//false
		
		//获取相同的开头
		System.out.println(getCommonPrefix("abcd","abe"));//ab
		
		//返回两个字符串之间 差几个字符
		System.out.println(getLevenshteinDistance("abcde", "ae"));//3
		
		//是否为空
		System.out.println(isBlank(" "));//true
		System.out.println(isEmpty(" "));//false
		
		//是否为空白
		System.out.println(isWhitespace(""));//true
		System.out.println(isWhitespace(" "));//true
		System.out.println(isWhitespace(" 3"));//false
		
	}
}
