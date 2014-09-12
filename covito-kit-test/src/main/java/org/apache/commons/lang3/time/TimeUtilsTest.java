package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;

public class TimeUtilsTest {

	@Test
	public void test() throws Exception{
		
		System.out.println(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
		
		Iterator<Calendar> it=DateUtils.iterator(DateUtils.parseDate("2014-1-1", "yyyy-MM-dd"), DateUtils.RANGE_MONTH_SUNDAY);
		while(it.hasNext()){
			System.out.println(DateFormatUtils.ISO_DATE_FORMAT.format(it.next()));
		}
		
		
		String time=FastDateFormat.getDateInstance(FastDateFormat.MEDIUM).format(new Date());
		System.out.println(time);
		
		StopWatch sw=new StopWatch();
		System.out.println(sw.getTime());
		sw.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sw.split();
		System.out.println(sw.getTime());
		System.out.println(sw.getSplitTime());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sw.split();
		System.out.println(sw.getSplitTime());
		sw.stop();
		System.out.println(sw.getTime());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(sw.getTime());
	}
}
