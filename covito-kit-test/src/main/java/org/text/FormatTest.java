package org.text;

import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class FormatTest {

	@Test
	public void test() throws ParseException{
		NumberFormat nf=NumberFormat.getCurrencyInstance();
		System.out.println(nf.format(1000));//￥1,000.00
		System.out.println(nf.parse("￥1,000.00"));//1000
		
		nf=NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumIntegerDigits(6);
		nf.setMinimumFractionDigits(2);
		System.out.println(nf.format(1000));//001000.00
		
		nf=NumberFormat.getNumberInstance();
		System.out.println(nf.format(1000));//1,000
		
		nf=NumberFormat.getPercentInstance();
		nf.setMinimumIntegerDigits(3);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);
		System.out.println(nf.format(0.2398));//023.98%
		System.out.println(nf.format(1));//100.00%
		
		System.out.println(ArrayUtils.toString(NumberFormat.getAvailableLocales()));
		System.out.println("---------------------------");
		double num[]={1,2,3,4,5,6,7};
		String days[]={"Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
		ChoiceFormat cf=new ChoiceFormat(num, days);
		System.out.println(cf.format(0));//Sun
		System.out.println(cf.format(1));//Sun
		System.out.println(cf.format(56));//Sat
		
		System.out.println();//1.0
		
		int rt=cf.parse("Sun").intValue();
		switch(rt){
			case 1:
				System.out.println("one");
				break;
			default:
				System.out.println("unkown");
		}
		
		try {
			System.out.println(cf.parse("unkdoen"));
		} catch (Exception e) {
			//java.text.ParseException: Unparseable number: "unkdoen"
			System.out.println(e.getMessage());
		}

		System.out.println("---------------------------");
		
		DateFormat df=DateFormat.getDateInstance(DateFormat.DEFAULT);
		System.out.println(df.format(new Date()));
		
	}
}
