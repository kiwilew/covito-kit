package org.text;

import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import org.junit.Test;

public class StringFormatTest {

	@Test
	public void test(){
		
		System.out.println("----------------整数----------------");
		//%[index$][标识]……[最小宽度]d
		
		//%n换行 %s字符串 %d十进制  %o八进制   %x或X 十六进制
		System.out.printf("age:%d %1$o %1$x%n", 11);//age:11 13 b
		
		//[,]每三位以逗号分隔 
		System.out.printf("%,d %n", 10000);//10,000
		
		//[0]  不足位用0填充 9最小宽度
		System.out.printf("%1$,09d %n", 10000);//00010,000
		
		//[-]在最小宽度内左对齐，不可以与"用0填充"同时使用
		System.out.printf("%1$,-9d %n", 10000);//10,000   
		
		//[#] 只适用于8进制和16进制，8进制时在结果前面增加一个0，16进制时在结果前面增加0x
		System.out.printf("%1$#o %1$#x%n", 11);//013 0xb
		
		//[+] 结果总是包括一个符号一般情况下只适用于10进制
		System.out.printf("%1$+d %2$+d %n", -11,12);//-11 +12
		
		//' ' 正值前加空格，负值前加负号一般情况下只适用于10进制
		System.out.printf("%1$ d %2$ d %n", -11,12);//-11  12 
		
		//[(] 若参数是负数，则结果中不添加负号而是用圆括号把数字括起来
		System.out.printf("%1$(d %2$(d %n", -11,12);//(11) 12 
		
		
		System.out.println("----------------浮点----------------");
		//%[index$][标识]……[最少宽度][.精度]f
		System.out.printf("%1$(.2f %2$(.3f %n", -11.34545,12.36565);//(11.35) 12.366 
		
		//'e', 'E'  --  结果被格式化为用计算机科学记数法表示的十进制数
		System.out.printf("%1$(.2e %2$(.3e %n", -11.34545,12.36565);//(1.13e+01) 1.237e+01 
		
		//'g', 'G'    --根据具体情况，自动选择用普通表示方式还是科学计数法方式
		System.out.printf("%1$(.2g %2$(.3g %n", -11.34545,12.36565);//(11) 12.4 
		
		System.out.println("----------------字符串----------------");
		System.out.printf("%1$-5s %n", "Nm");//Nm   
		
		System.out.printf("%1$5s %n", "Nm");//   Nm  
		
		System.out.println("----------------百分比----------------");
		System.out.printf("%1$5.2f%% %n", 11.5389);//11.54% 
		
		System.out.println("----------------日期----------------");
		
		Locale.setDefault(new Locale("en"));
		
		//格式为：[t|T][格式]
		//[F] yyyy-MM-dd
		System.out.printf("%1$tF %n", new Date());//2014-09-19  
		
		//[D] dd/MM/yy
		System.out.printf("%1$tD %n", new Date());//09/19/14 
		
		//[c]
		System.out.printf("%1$tc %n", new Date());//Fri Sep 19 14:48:04 CST 2014
		
		//[r]
		System.out.printf("%1$tr %n", new Date());//02:21:50 下午
		
		//[T] 24小时制 hh:mm:ss
		System.out.printf("%1$tT %n", new Date());//14:22:27
		
		//[R] 24小时制 hh:mm
		System.out.printf("%1$tR %n", new Date());//14:22:27
		
		//[e]不带0天数[d]带0天数 m带0分份 y年后两位 Y四位
		System.out.printf("%1$tY-%1$tm-%1$td %1$ty-%1$tm-%1$td%n", new Date());//2014-09-19 14-09-19
		
		//[j]一年中的天数
		System.out.printf("%1$tj %n", new Date());//262
		
		//[a]语言环境的星期几简称，例如 "Sun" 和 "Mon"
		//[A]特定于语言环境的星期几全称，例如 "Sunday" 和 "Monday"
		System.out.printf("%1$ta %1$tA %n", new Date());//Fri Friday 
		
		//[b]特定于语言环境的月份简称，例如 "Jan" 和 "Feb"。
		//[B]特定于语言环境的月份全称，例如 "January" 和 "February"。
		System.out.printf("%1$tb %1$tB %n", new Date());//Sep September
		
		//[H]24 小时制的小时，被格式化为必要时带前导零的两位数，即 00 - 23
		//[k]24 小时制的小时，即 0 - 23。
		//[I]12 小时制的小时，被格式化为必要时带前导零的两位数，即 01 - 12。
		//[l]12 小时制的小时，即 1 - 12。
		System.out.printf("%1$tH %1$tk %1$tI %1$tl %n", new Date());//14 14 02 2 
		
		//[M]分钟，被格式化为必要时带前导零的两位数，即 00 - 59。
		//[S]秒，被格式化为必要时带前导零的两位数，即 00 - 60
		//[L]毫秒，被格式化为必要时带前导零的三位数，即 000 - 999
		//[N]毫微秒，被格式化为必要时带前导零的九位数，即 000000000 - 999999999
		System.out.printf("%1$tM 分 %1$tS 秒 %1$tL 毫秒 %1$tN 毫微秒 %n", new Date());//44 分 18 秒 916 毫秒 916000000 毫微秒 
		
		//[z]相对于 GMT 的 RFC 822 格式的数字时区偏移量，例如 -0800
		//[Z]表示时区缩写形式的字符串
		System.out.printf("%1$tZ %1$tz %n", new Date());//CST +0800
		
		//[s]自协调世界时 (UTC) 1970 年 1 月 1 日 00:00:00 至现在所经过的秒数
		System.out.printf("%1$ts %2$d %n", new Date(),System.currentTimeMillis());//1411109387 1411109387483 
		
		//[Q]自协调世界时 (UTC) 1970 年 1 月 1 日 00:00:00 至现在所经过的毫秒数
		System.out.printf("%1$tQ %2$d %n", new Date(),System.currentTimeMillis());//1411109467743 1411109467743 
		
		//等同于String.format
		System.out.println(String.format("等同于%s", "String.format"));
			
		//等同于Formatter
		System.out.println(new Formatter().format(new Locale("en"),"%1$ta %1$tA %n", new Date()));//Fri Friday 
	}
}
