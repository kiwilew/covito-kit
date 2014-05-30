/*
 * 文 件 名:  ValidaterUtil.java
 *
 * 描    述:  <描述>
 * 创 建 人:  eighteencold
 * 创建时间:  2014-1-25
 */
package com.covito.kit.utility;

import java.util.regex.Pattern;

/**
 * 较验工具类<br/>
 * 常用较验
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-25]
 */
public class ValidaterUtil {
	
	private static Pattern PhoneNumber = Pattern.compile("(^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$)|(^((\\(\\d{3}\\))|(\\d{3}\\-))?(1[358]\\d{9})$)");
	
	private static Pattern URL = Pattern.compile("[a-zA-z]+://[^\\s]*");
	
	private static Pattern IP = Pattern.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
	
	private static Pattern Letter = Pattern.compile("^[A-Za-z]*$");
	
	private static Pattern LetterOrDigit = Pattern.compile("^\\w*$");
	
	private static Pattern ChineseOrLetterOrDigit = Pattern.compile("^[\\w|\u4e00-\u9fa5]*$");
	
	private static Pattern PDigit = Pattern.compile("^\\d*$");
	
	private static Pattern Email = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
	
	private static Pattern Chinese = Pattern.compile("[\u4e00-\u9fa5]");
	
	private static Pattern NumberPatter = Pattern.compile("^[\\d\\.E\\,\\+\\-]*$");
	
	private static Pattern MobileNumber = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
	
	private static Pattern QQNumber = Pattern.compile("[1-9][0-9]{4,}");
	
	private static Pattern PostalCode = Pattern.compile("[1-9]\\d{5}(?!\\d)");
	
	private static Pattern CarLicensePlate = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\u4e00-\u9fa5]$");
	
	
	
	/** 
	 * 检查str是否有除字母和数字之外的字符<br/>
	 * str是否只是字母和数字
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isLetterOrDigit(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return LetterOrDigit.matcher(str).find();
	}
	
	/** 
	 * 是否全是中文英文和数字组成<br/>
	 * 支持有下划线
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isChineseOrLetterOrDigit(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return ChineseOrLetterOrDigit.matcher(str).find();
	}

	/** 
	 * 检查str是否有除字母之外的字符<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return Letter.matcher(str).find();
	}
	
	/** 
	 * 是否是合法的URL<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isURL(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return URL.matcher(str).find();
	}
	/** 
	 * 是否是合法的车牌照<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isCarLicensePlate(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return CarLicensePlate.matcher(str).find();
	}
	
	/** 
	 * 是否是合法的邮编<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isPostalCode(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return PostalCode.matcher(str).find();
	}

	
	/** 
	 * 是否是合法的电话号码<br/>
	 * 可以是传真号，手机号或电话号
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumber(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return PhoneNumber.matcher(str).find();
	}
	
	/** 
	 * 检查是否有中文<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isContainsChinese(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return Chinese.matcher(str).find();
	}
	
	/** 
	 * 是否是合法的手机号码<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isMobileNumber(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return MobileNumber.matcher(str).find();
	}
	
	/** 
	 * 是否是合法的QQ号码<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isQQNumber(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return QQNumber.matcher(str).find();
	}

	/** 
	 * 检查是否全是数字<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return PDigit.matcher(str).find();
	}

	/**
	 * 校验参数是否为合法IP地址
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIpAddress(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return IP.matcher(str).find();
	}
	
	/**
	 * 校验参数是否为合法Email地址
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return Email.matcher(str).find();
	}
	
	/**
	 * 校验参数是否为合法Email地址
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIDCardNo(String str) {
		return IDCardValidate.isIdcard(str);
	}
	
	/** 
	 * <判断字符串是否为真假判定字符串><br/>
	 * <当为‘true’或‘false’时返回true>
	 *
	 * @author  eighteencold
	 * @param v
	 * @return
	 */
	public static boolean isBoolean(String v) {
		return ("true".equals(v)) || ("false".equals(v));
	}
	
	/** 
	 * 参数是否不为null<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param v
	 * @return
	 */
	public static boolean isNotNull(String v) {
		return StringUtil.isNotEmpty(v);
	}
	
	/** 
	 * <判断字符串是否为数字><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (ObjectUtil.empty(str)) {
			return false;
		}
		return NumberPatter.matcher(str).find();
	}
	
	/** 
	 * <判断字符串是否为int><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isInt(String str) {
		if (ObjectUtil.empty(str))
			return false;
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/** 
	 * <判断字符串是否为Integer><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		return isInt(str);
	}

	/** 
	 * <判断字符串是否为Long><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isLong(String str) {
		if (ObjectUtil.empty(str))
			return false;
		try {
			Long.parseLong(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/** 
	 * <判断字符串是否为Float><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isFloat(String str) {
		if (ObjectUtil.empty(str))
			return false;
		try {
			Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/** 
	 * <判断字符串是否为Double><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		if (ObjectUtil.empty(str))
			return false;
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/** 
	 * <判断字符串是否Time格式><br/>
	 * <必须HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param time
	 * @return
	 */
	public static boolean isTime(String time) {
		return DateUtil.isTime(time);
	}

	/** 
	 * <判断字符串是否Date格式><br/>
	 * <格式为：yyyy-MM-dd>
	 *
	 * @author  eighteencold
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date) {
		return DateUtil.isDate(date);
	}

	/** 
	 * <判断字符串是否Datetime格式><br/>
	 * <格式为：yyyy-MM-dd HH:mm:ss>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isDateTime(String str) {
		return DateUtil.isDateTime(str);
	}
	
}
