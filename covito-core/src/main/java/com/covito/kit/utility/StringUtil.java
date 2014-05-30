package com.covito.kit.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-23]
 */
public class StringUtil {
	
	/**UTF-8 格式头bom*/
	public static final byte[] BOM = { -17, -69, -65 };
	
	private static final Pattern PTitle = Pattern.compile("<title>(.+?)</title>");

	/** 
	 * <转换成java编码><br/>
	 * <将特殊占位符串显示出来>
	 *
	 * @author  eighteencold
	 * @param txt
	 * @return
	 */
	public static String javaEncode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replace(txt, "\\", "\\\\");
		txt = replace(txt, "\r\n", "\n");
		txt = replace(txt, "\r", "\\r");
		txt = replace(txt, "\t", "\\t");
		txt = replace(txt, "\n", "\\n");
		txt = replace(txt, "\"", "\\\"");
		txt = replace(txt, "'", "\\'");
		return txt;
	}

	/** 
	 * <java编码解码><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param txt
	 * @return
	 */
	public static String javaDecode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replace(txt, "\\\\", "\\");
		txt = replace(txt, "\\r", "\r");
		txt = replace(txt, "\\t", "\t");
		txt = replace(txt, "\\n", "\n");
		txt = replace(txt, "\\\"", "\"");
		txt = replace(txt, "\\'", "'");
		txt = replace(txt, "\n", "\r\n");
		return txt;
	}

	/** 
	 * <字符串切分><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @param spliter
	 * @return
	 */
	public static String[] split(String str, String spliter) {
		return str.split(spliter);
	}

	/** 
	 * <替换字符串><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str 目标字符串
	 * @param subStr 字符串
	 * @param reStr 新字符串
	 * @return
	 */
	public static String replace(String str, String subStr, String reStr) {
		if (str == null) {
			return null;
		}
		if ((subStr == null) || (subStr.equals("")) || (reStr == null)) {
			return str;
		}
		return str.replace(subStr, reStr);
	}

	/** 
	 * <替换所有><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param source 目录字符串
	 * @param oldstring 正则或旧字符串
	 * @param newstring 新字符串
	 * @return
	 */
	public static String replaceAllIgnoreCase(String source, String oldstring,
			String newstring) {
		return source.replaceAll(oldstring, newstring);
	}

	/** 
	 * <将""编码成HTML支持的&quot;将&编码成&amp;><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param txt
	 * @return
	 */
	public static String quotEncode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replace(txt, "&", "&amp;");
		txt = replace(txt, "\"", "&quot;");
		return txt;
	}

	/** 
	 * <quotEncode的反编码，将&quot;转成",&amp;转成&><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param txt
	 * @return
	 */
	public static String quotDecode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replace(txt, "&quot;", "\"");
		txt = replace(txt, "&amp;", "&");
		return txt;
	}

	

	/** 
	 * <左边补全><br/>
	 * StringUtil.leftPad("90",'0',8)//00000090
	 *
	 * @author  eighteencold
	 * @param srcString
	 * @param c
	 * @param length
	 * @return
	 */
	public static String leftPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();
		if (tLen >= length)
			return srcString;
		int iMax = length - tLen;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < iMax; i++) {
			sb.append(c);
		}
		sb.append(srcString);
		return sb.toString();
	}

	/** 
	 * <右边补全><br/>
	 * StringUtil.leftPad("90",'0',8)//90000000
	 *
	 * @author  eighteencold
	 * @param srcString
	 * @param c
	 * @param length
	 * @return
	 */
	public static String rightPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();

		if (tLen >= length)
			return srcString;
		int iMax = length - tLen;
		StringBuilder sb = new StringBuilder();
		sb.append(srcString);
		for (int i = 0; i < iMax; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	/** 
	 * <截掉字符串右边的空格，换行，制表符><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param src
	 * @return
	 */
	public static String rightTrim(String src) {
		if (src != null) {
			char[] chars = src.toCharArray();
			for (int i = chars.length - 1; i >= 0; i--) {
				if ((chars[i] != ' ') && (chars[i] != '\t')
						&& (chars[i] != '\r')) {
					return src.substring(0, i + 1);
				}
			}
			return "";
		}
		return src;
	}

	/** 
	 * <从一段html中截取tilte><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param html
	 * @return
	 */
	public static String getHtmlTitle(String html) {
		
		Matcher m = PTitle.matcher(html);
		if (m.find()) {
			return m.group(1).trim();
		}
		return null;
	}

	/** 
	 * <大写首字母><br/>
	 * StringUtil.capitalize("boolean")//Boolean
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
		if ((str == null) || (str.length() == 0))
			return str;
		return Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}

	/** 
	 * 字符串是不为空，或长度为0<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null) || (str.length() == 0);
	}

	/** 
	 * <字符串长度于于0><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/** 
	 * 判空之外，判断不等"null"<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return (isEmpty(str)) || ("null".equals(str));
	}

	/** 
	 * isNull的相反<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		return (isNotEmpty(str)) && (!"null".equals(str));
	}

	/** 
	 * 当字符串为空时，返回默认值<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param string
	 * @param defaultString
	 * @return
	 */
	public static final String noNull(String string, String defaultString) {
		return isEmpty(string) ? defaultString : string;
	}

	/** 
	 * 当字符串为空时，返回""<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param string
	 * @return
	 */
	public static final String noNull(String string) {
		return noNull(string, "");
	}

	/** 
	 * 将数组拼成字符串，以,号分隔<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static String join(Object[] arr) {
		return join(arr, ",");
	}

	/** 
	 * 将数组拼成字符串，以\n和,号分隔<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static String join(Object[][] arr) {
		return join(arr, "\n", ",");
	}

	/** 
	 * 将数组拼成字符串，指定分隔符<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param spliter
	 * @return
	 */
	public static String join(Object[] arr, String spliter) {
		if (arr == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/** 
	 * 将数组拼成字符串，指定分隔符<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param spliter1
	 * @param spliter2
	 * @return
	 */
	public static String join(Object[][] arr, String spliter1, String spliter2) {
		if (arr == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(spliter2);
			}
			sb.append(join(arr[i], spliter2));
		}
		return sb.toString();
	}

	/** 
	 * <将list拼成字符串><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param list
	 * @return
	 */
	public static String join(List<?> list) {
		return join(list, ",");
	}

	/** 
	 * <将list拼成字符串，指定分隔符><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param list
	 * @param spliter
	 * @return
	 */
	public static String join(List<?> list, String spliter) {
		if (list == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(list.get(i));
		}
		return sb.toString();
	}

	/** 
	 * 统计findStr在str出现的次数<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @param findStr
	 * @return
	 */
	public static int count(String str, String findStr) {
		int lastIndex = 0;
		int length = findStr.length();
		int count = 0;
		int start = 0;
		while ((start = str.indexOf(findStr, lastIndex)) >= 0) {
			lastIndex = start + length;
			count++;
		}
		return count;
	}

	/** 
	 * 两个分隔符，将字符串分隔成Map<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @param entrySpliter 实体分隔符
	 * @param keySpliter kye-value分隔符
	 * @return
	 */
	public static Map<String, String> splitToMap(String str,
			String entrySpliter, String keySpliter) {
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = split(str, entrySpliter);
		for (int i = 0; i < arr.length; i++) {
			String[] arr2 = split(arr[i], keySpliter);
			String key = arr2[0];
			if (isEmpty(key)) {
				continue;
			}
			key = key.trim();
			String value = null;
			if (arr2.length > 1) {
				value = arr2[1];
			}
			map.put(key, value);
		}
		return map;
	}

	/** 
	 * 获取URL地址的扩展名<br/>
	 * StringUtil.getURLExtName("http://www.baidu.com/index.php?tn=monline_5_dg");//php
	 *
	 * @author  eighteencold
	 * @param url
	 * @return
	 */
	public static String getURLExtName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf('?');
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf('.', index1);
		if (index2 == -1) {
			return null;
		}
		int index3 = url.indexOf('/', 8);
		if (index3 == -1) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		if (ext.matches("[^\\/\\\\]*")) {
			return ext;
		}
		return null;
	}

	/** 
	 * 获取URL地址的名称<br/>
	 * StringUtil.getURLFileName("http://www.baidu.com/index.php?tn=monline_5_dg");//index.php
	 *
	 * @author  eighteencold
	 * @param url
	 * @return
	 */
	public static String getURLFileName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf('?');
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf('/', index1);
		if ((index2 == -1) || (index2 < 8)) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		return ext;
	}

	/** 
	 * 简体中文转成繁体中文<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static String toTradition(String str) {
		return Big5Convert.toTradition(str);
	}
	
	/** 
	 * <繁体转成简体中文><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static String toSimple(String str) {
		return Big5Convert.toSimple(str);
	}
}
