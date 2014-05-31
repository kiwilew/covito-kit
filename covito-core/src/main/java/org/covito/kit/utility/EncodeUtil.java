package org.covito.kit.utility;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class EncodeUtil {

	/** 
	 * <Base64加密><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param b
	 * @return
	 */
	public static String base64Encode(byte[] b) {
		if (b == null) {
			return null;
		}
		return new Base64().encodeAsString(b);
	}

	/** 
	 * Base64解密<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param s
	 * @return
	 */
	public static byte[] base64Decode(String s) {
		if (s != null) {
			Base64 decoder = new Base64();
			try {
				return decoder.decode(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/** 
	 * 将字符串做MD5加密后用Base64编码<br/>
	 * EncodeUtil.md5Base64("123456");//4QrcOUm6Wau+VuBX8g+IPg==
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static String md5Base64(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return base64Encode(md5.digest(str.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	 * 字符串做sha1加密后做十六进制编码<br/>
	 * EncodeUtil.sha1Hex("123456");//7c4a8d09ca3762af61e59520943dc26494f8941b
	 *
	 * @author  eighteencold
	 * @param src
	 * @return
	 */
	public static String sha1Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA1");
			byte[] md = md5.digest(src.getBytes());
			return hexEncode(md);
		} catch (Exception e) {
		}
		return null;
	}
	
	/** 
	 * 字符串做MD5加密后做十六进制编码<br/>
	 * EncodeUtil.md5Hex("123456");//e10adc3949ba59abbe56e057f20f883e
	 *
	 * @author  eighteencold
	 * @param src
	 * @return
	 */
	public static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src.getBytes());
			return hexEncode(md);
		} catch (Exception e) {
		}
		return null;
	}
	
	/** 
	 * 字符串做MD5加密<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param src
	 * @return
	 */
	public static byte[] md5(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src.getBytes());
			return md;
		} catch (Exception e) {
		}
		return null;
	}

	/** 
	 * MD5加密<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param src
	 * @return
	 */
	public static byte[] md5(byte[] src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src);
			return md;
		} catch (Exception e) {
		}
		return null;
	}
	
	/** 
	 * <十六进制编码><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param bs
	 * @return
	 */
	public static String hexEncode(byte[] bs) {
		return new String(new Hex().encode(bs));
	}

	/** 
	 * 十六进制解码<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @return
	 */
	public static byte[] hexDecode(String str) {
		try {
			if (str.endsWith("\n")) {
				str = str.substring(0, str.length() - 1);
			}
			char[] cs = str.toCharArray();
			return Hex.decodeHex(cs);
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	 * <escape编码><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		StringBuilder sb = new StringBuilder();
		sb.ensureCapacity(src.length() * 6);
		for (int i = 0; i < src.length(); i++) {
			char j = src.charAt(i);
			if ((Character.isDigit(j)) || (Character.isLowerCase(j))
					|| (Character.isUpperCase(j))) {
				sb.append(j);
			} else if (j < '?') {
				sb.append("%");
				if (j < '\020') {
					sb.append("0");
				}
				sb.append(Integer.toString(j, 16));
			} else {
				sb.append("%u");
				sb.append(Integer.toString(j, 16));
			}
		}
		return sb.toString();
	}

	/** 
	 * <escape解码><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuilder sb = new StringBuilder();
		sb.ensureCapacity(src.length());
		int lastPos = 0;
		int pos = 0;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					char ch = (char) Integer.parseInt(src.substring(pos + 2,
							pos + 6), 16);
					sb.append(ch);
					lastPos = pos + 6;
				} else {
					char ch = (char) Integer.parseInt(src.substring(pos + 1,
							pos + 3), 16);
					sb.append(ch);
					lastPos = pos + 3;
				}
			} else if (pos == -1) {
				sb.append(src.substring(lastPos));
				lastPos = src.length();
			} else {
				sb.append(src.substring(lastPos, pos));
				lastPos = pos;
			}
		}
		return sb.toString();
	}
	
	/** 
	 * <URL编码><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String urlEncode(String str, String charset) {
		try {
			return URLEncoder.encode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <URL解码><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String urlDecode(String str, String charset) {
		try {
			return URLDecoder.decode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
