package org.apache.commons.lang3;

import org.junit.Test;
import static org.apache.commons.lang3.StringUtils.*;

public class StringUtilsTest {

	/**
	 * 
	 */
	@Test
	public void test() {
		// 省略
		String str = "abcdefghij";
		System.out.println(abbreviate(str, 8));// abcde...
		System.out.println(abbreviate(str, 7));// abcd...
		System.out.println(abbreviate(str, 6));// abc...
		System.out.println(abbreviate(str, 5));// ab...
		System.out.println(abbreviate(str, 4));// a...

		// 首字母大写
		System.out.println(capitalize(str));// Abcdefghij
		System.out.println(capitalize("adDd"));// AdDd
		//首字母小写
		System.out.println(uncapitalize("DabD"));//dabD

		// 全部小写(大写)
		System.out.println(lowerCase(str));// abcdefghij
		System.out.println(upperCase(str));// ABCDEFGHIJ
		//大小写转换
		System.out.println(swapCase("abDE"));//ABde

		// 填充两边
		System.out.println("^" + center("a", 5) + "$");// ^ a $
		System.out.println("^" + center("a", 5, '.') + "$");// ^..a..$

		// 填充左边
		System.out.println("^" + leftPad("a", 5) + "$");// ^ a$
		System.out.println("^" + leftPad("a", 5, '.') + "$");// ^....a$

		// 填充右边
		System.out.println("^" + rightPad("a", 5) + "$");// ^a $
		System.out.println("^" + rightPad("a", 5, '.') + "$");// ^a....$

		// 删除一个换行符
		System.out.println(chomp(str));// abcdefghij
		System.out.println(chomp(str + "\n\n"));// abcdefghij\n

		// 截掉最后一个字符
		System.out.println(chop(str));// abcdefghi

		// 是否(不)包含
		System.out.println(contains(str, "ad"));// false
		System.out.println(containsAny(str, "ad"));// true
		System.out.println(containsAny(str, 'a', 'd'));// true
		System.out.println(containsNone(str, 'r', 'p'));// true
		System.out.println(containsNone(str, "ad"));// false

		// 是否字符串只由某几个字符构成
		System.out.println(containsOnly("ababa", "ab"));// true
		System.out.println(containsOnly("ababa", "a"));// false

		// 是否包含空格
		System.out.println(containsWhitespace("a b"));// true
		System.out.println(containsWhitespace("ab"));// false

		// 统计出现的次数
		System.out.println(countMatches(str, "ab"));// 1
		System.out.println(countMatches("babab", "ab"));// 2

		// 当为空的时候取的默认值
		System.out.println(defaultString(null, "unkown"));// unkown
		System.out.println(defaultString(str, "unkown"));// abcdefghij
		System.out.println(defaultIfBlank("", "blank"));// blank
		System.out.println(defaultIfEmpty("", "blank"));// blank
		System.out.println(defaultIfEmpty(null, "blank"));// blank

		// 删除字符串中的空格
		System.out.println(deleteWhitespace("a b"));// ab

		// 相对于第一个字符串而言，第二个字符的差别
		System.out.println(difference("abe", "abcd"));// cd

		// 是否以*结尾
		System.out.println(endsWith(str, "ij"));// true
		System.out.println(endsWith(str, "a"));// false

		// 获取相同的开头
		System.out.println(getCommonPrefix("abcd", "abe"));// ab

		// 返回两个字符串之间 差几个字符
		System.out.println(getLevenshteinDistance("abcde", "ae"));// 3

		// 是否为空
		System.out.println(isBlank(" "));// true
		System.out.println(isEmpty(" "));// false

		// 是否为空白
		System.out.println(isWhitespace(""));// true
		System.out.println(isWhitespace(" "));// true
		System.out.println(isWhitespace(" 3"));// false

		// 是否全是小写
		System.out.println(isAllLowerCase(str));// true
		// 是否全是大写
		System.out.println(isAllUpperCase(str));// false

		System.out.println(join("a", "b", "t"));// abt
		System.out.println(join(new Object[] { "2", "e" }, "-"));// 2-e

		// 从左(右)开始截N个字符
		System.out.println(left(str, 4));// abcd
		System.out.println(right(str, 4));// ghij
		// 在中间某个位置截N个长度
		System.out.println(mid(str, 2, 1));// c

		// 字符长度
		System.out.println(length(str));// 10

		// 第几次出现的位置(从0开始)
		System.out.println(ordinalIndexOf("abcabc", "b", 1));// 1
		System.out.println(ordinalIndexOf("abcabc", "b", 2));// 4
		System.out.println(lastOrdinalIndexOf("abcabc", "c", 1));// 5
		System.out.println(lastOrdinalIndexOf("abcabc", "c", 2));// 2

		// 将所有的换行符，制表符转换成空格
		System.out.println(normalizeSpace("a\tb\nc\r\n"));// a b c

		// 将指定字符覆盖到某一段位置
		System.out.println(overlay(str, "-", 3, 5));// abc-fghij

		// 删除字符串中所有指定字符
		System.out.println(remove("abcabc", "a"));// bcbc

		// 如果字符以指定字符结尾，删除字符串尾部的指定字符
		System.out.println(removeEnd("abc.com", ".com"));// abc
		System.out.println(removeEnd("abc.com1", ".com"));// abc.com1

		// 如果字符以指定字符开头,删除之
		System.out.println(removeStart("www.bac", "www."));// bac
		System.out.println(removeStart("www1.bac", "www."));// www1.bac

		// 以指定字符生成指定长度的字符串
		System.out.println(repeat("0", 6));// 000000
		// 以指定字符生成指定长度以指定字符间隔的字符串
		System.out.println(repeat("A", "0", 2));// A0A

		// 查找与替换(各种姿势)
		System.out.println(replace("abcaeb", "a", "A"));// AbcAeb
		System.out.println(replace("abcaeb", "a", "A", 1));// Abcaeb
		System.out.println(replaceChars("abcaeb", "ac", "xy"));// xbyxeb
		System.out.println(replaceChars("abcaeb", "ac", "x"));// xbxeb
		System.out.println(replaceEach("abcde", new String[] { "ab", "d" },
				new String[] { "d", "t" }));// dcte
		System.out.println(replaceEachRepeatedly("abcde", new String[] { "ab", "d" }, new String[] {
				"d", "t" }));// tcte

		// 只替换第一次出现
		System.out.println(replaceOnce("abcdea", "a", "y"));// ybcdea

		// 反转字符串
		System.out.println(reverse(str));// jihgfedcba

		// 包舍特定字符才反转
		System.out.println(reverseDelimited("ab.c", '.'));// c.ab
		System.out.println(reverseDelimited("ab.c", '#'));// ab.c

		// 是否以给定范围里的任意字符开头
		System.out.println(startsWithAny("中国", "中", "法"));// true

		// 删除前后的空格
		System.out.println(strip("   ab c   "));// ab c
		System.out.println(strip("#ba#d##", "#"));//ba#d
		// 删除后面的指定字符
		System.out.println(stripEnd("_ab_c___", "_"));// _ab_c
		System.out.println(stripStart("_ab_c___", "_"));// ab_c___
		System.out.println(stripToEmpty(null));//""
		System.out.println(stripToNull(null));//null
		
		System.out.println(trim("   ab c   "));//ab c
		System.out.println(trimToEmpty(null));//""
		System.out.println(trimToNull(null));//null
		
		//截取字符串(各种姿势)
		System.out.println(substring("abcd", 1, 2));//b
		//从头开始找第一个开始
		System.out.println(substringAfter("abcbc", "b"));//cbc
		//区别于substringAfter，从后找
		System.out.println(substringAfterLast("abcbc", "b"));//c
		System.out.println(substringBefore("abcbc", "b"));//a
		System.out.println(substringBeforeLast("abcbc", "b"));//abc
		System.out.println(substringBetween("23#rtrre#3", "#"));//rtrre
		System.out.println(substringBetween("23#rtrre$3", "#", "$"));//rtrre
		System.out.println(substringsBetween("23#rt$a#rre$3", "#", "$")[1]);//rre
		
		//拆分，以各种姿势
		// 以指定字符折分成N截
		System.out.println(split("a.bc.d", ".").length);// 3
		//以类型拆分，字母，符号、数字
		System.out.println(splitByCharacterType("ab ac3").length);//4
		//区别于上面，驼峰式大小写也算
		System.out.println(splitByCharacterTypeCamelCase("ab acAb3").length);//5
		
		//感觉跟split没啥区别？
		System.out.println(splitByWholeSeparator("a#b c#d", "#").length);//3
		System.out.println(split("a#b c#d", "#").length);//3
		System.out.println(splitByWholeSeparatorPreserveAllTokens("a#b c#d", "#").length);//3
		
		//当Tokens连接出现的时候，tokens要保存N-1个区别于split不保存
		System.out.println(splitPreserveAllTokens("a b").length);//2
		System.out.println(splitPreserveAllTokens("a  b").length);//3
		System.out.println(split("a  b").length);//2
	}
}
