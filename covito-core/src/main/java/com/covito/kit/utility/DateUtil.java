package com.covito.kit.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-20]
 */
public class DateUtil {

	public static final String Format_Date = "yyyy-MM-dd";
	public static final String Format_Time = "HH:mm:ss";
	public static final String Format_DateTime = "yyyy-MM-dd HH:mm:ss";
	
	/** 
	 * <获取当前日期><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @return
	 */
	public static String getCurrentDate() {
		return new SimpleDateFormat(Format_Date).format(new Date());
	}

	/** 
	 * <获取当前日期><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param format 时间格式
	 * @return
	 */
	public static String getCurrentDate(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	/** 
	 * <获取当前时间><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @return
	 */
	public static String getCurrentTime() {
		return new SimpleDateFormat(Format_Time).format(new Date());
	}

	/** 
	 * <获取当前时间><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param format 时间格式
	 * @return
	 */
	public static String getCurrentTime(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	/** 
	 * <获取当前日期时间><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @return
	 */
	public static String getCurrentDateTime() {
		return getCurrentDateTime(Format_DateTime);
	}

	/** 
	 * <获取当前日期时间><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param format
	 * @return
	 */
	public static String getCurrentDateTime(String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(new Date());
	}

	
	/** 
	 * <时间转成字符串><br/>
	 * <格式为：yyyy-MM-dd>
	 *
	 * @author  eighteencold
	 * @param date
	 * @return
	 */
	public static String toDateString(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(Format_Date).format(date);
	}

	/** 
	 * <时间转成字符串><br/>
	 * <格式为：yyyy-MM-dd HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param date
	 * @return
	 */
	public static String toDateTimeString(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(Format_DateTime).format(date);
	}
	
	/** 
	 * <时间转成字符串><br/>
	 * <格式为：HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param date
	 * @return
	 */
	public static String toTimeString(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(Format_Time).format(date);
	}

	/** 
	 * <时间转成字符串><br/>
	 * <格式自定义：如yyyy/MM/dd>
	 *
	 * @author  eighteencold
	 * @param date
	 * @param format 格式如yyyy/MM/dd
	 * @return
	 */
	public static String toString(Date date, String format) {
		SimpleDateFormat t = new SimpleDateFormat(format);
		return t.format(date);
	}

	/** 
	 * <判断字符串是否Time格式><br/>
	 * <格式必须为HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param time
	 * @return
	 */
	public static boolean isTime(String time) {
		Date d=parseTime(time);
		if(d==null){
			return false;
		}
		String co=toTimeString(d);
		time=time.replaceFirst("^0{1}", "");
		time=time.replace(":0", ":");
		co=co.replaceFirst("^0{1}", "");
		co=co.replace(":0", ":");
		if(time.equals(co)){
			return true;
		}
		return false;
	}

	/** 
	 * <判断字符串是否Date格式><br/>
	 * <格式必须为：yyyy-MM-dd>
	 *
	 * @author  eighteencold
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date) {
		Date d=parseDate(date);
		if(d==null){
			return false;
		}
		String co=toDateString(d);
		date=date.replace("-0", "-");
		co=co.replace("-0", "-");
		if(date.equals(co)){
			return true;
		}
		return false;
	}

	/** 
	 * <判断字符串是否Datetime格式><br/>
	 * <格式必须为：yyyy-MM-dd HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isDateTime(String str) {
		Date d=parseDateTime(str);
		if(d==null){
			return false;
		}
		String s[]=str.split(" ");
		return isDate(s[0])&&isTime(s[1]);
	}

	/** 
	 * <判断日期是否为周末><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param date
	 * @return
	 */
	public static boolean isWeekend(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int t = cal.get(Calendar.DAY_OF_WEEK);
		return (t == 7) || (t == 1);
	}

	/** 
	 * <将字符串转成Date类型格式为：yyyy-MM-dd><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static Date parseDate(String str) {
		if (ObjectUtil.empty(str))
			return null;
		try {
			return new SimpleDateFormat(Format_Date).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <将字符串转成Date类型><br/>
	 * <格式自定义>
	 *
	 * @author  eighteencold
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date parse(String str, String format) {
		if (ObjectUtil.empty(str))
			return null;
		try {
			SimpleDateFormat t = new SimpleDateFormat(format);
			return t.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <将字符串转成Date类型><br/>
	 * <格式为：yyyy-MM-dd HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static Date parseDateTime(String str) {
		if (ObjectUtil.empty(str)) {
			return null;
		}
		try {
			return new SimpleDateFormat(Format_DateTime).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	 * <将字符串转成Time类型><br/>
	 * <格式为：HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static Date parseTime(String str) {
		if (ObjectUtil.empty(str)) {
			return null;
		}
		try {
			return new SimpleDateFormat(Format_Time).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <追加时间以分钟为单位><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addMinute(Date date, int count) {
		return new Date(date.getTime() + 60000L * count);
	}

	/** 
	 * <追加时间以小时为单位><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addHour(Date date, int count) {
		return new Date(date.getTime() + 3600000L * count);
	}

	/** 
	 * <追加时间以天为单位><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addDay(Date date, int count) {
		return new Date(date.getTime() + 86400000L * count);
	}

	/** 
	 * <追加时间以周为单位><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addWeek(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.WEEK_OF_YEAR, count);
		return c.getTime();
	}

	/** 
	 * <追加时间以月为单位><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addMonth(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, count);
		return c.getTime();
	}

	/** 
	 * <追加时间以年为单位><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addYear(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, count);
		return c.getTime();
	}

}
