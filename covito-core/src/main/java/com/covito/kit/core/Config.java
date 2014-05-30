/*
 * 文 件 名:  Config.java
 *
 * 描    述:  <描述>
 * 创 建 人:  eighteencold
 * 创建时间:  2014-1-23
 */
package com.covito.kit.core;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.covito.kit.utility.ResourceReader;

/**
 * 一句话功能简述<br/>
 * <功能详细描述>
 * 
 * @author eighteencold
 * @version [v1.0, 2014-1-23]
 */
public class Config {

	// 在线用户
	public static int OnlineUserCount = 0;

	// 登录用户
	public static int LoginUserCount = 0;

	// 是否允许登录
	public static boolean isAllowLogin = true;

	protected static Map<String, String> configMap = new HashMap<String, String>();

	private static String ClassPath;

	protected static void init() {

		if (!configMap.containsKey(ConfigConstant.SYSTEM_JAVAVERSION)) {
			configMap.put(ConfigConstant.SYSTEM_JAVAVERSION,
					System.getProperty("java.version"));
			configMap.put(ConfigConstant.SYSTEM_JAVAVENDOR,
					System.getProperty("java.vendor"));
			configMap.put(ConfigConstant.SYSTEM_JAVAHOME,
					System.getProperty("java.home"));
			configMap.put(ConfigConstant.SYSTEM_OSPATCHLEVEL,
					System.getProperty("sun.os.patch.level"));
			configMap.put(ConfigConstant.SYSTEM_OSARCH,
					System.getProperty("os.arch"));
			configMap.put(ConfigConstant.SYSTEM_OSVERSION,
					System.getProperty("os.version"));
			configMap.put(ConfigConstant.SYSTEM_OSNAME,
					System.getProperty("os.name"));
			if ((System.getProperty("os.name").toLowerCase().indexOf("windows") > 0)
					&& (System.getProperty("os.name").equals("6.1"))) {
				configMap.put(ConfigConstant.SYSTEM_OSNAME, "Windows 7");
			}
			configMap.put(ConfigConstant.SYSTEM_OSUSERLANGUAGE,
					System.getProperty("user.language"));
			configMap.put(ConfigConstant.SYSTEM_OSUSERNAME,
					System.getProperty("user.name"));
			configMap.put(ConfigConstant.SYSTEM_TIMEZONE,
					System.getProperty("user.timezone"));
			configMap.put(ConfigConstant.SYSTEM_LINESEPARATOR,
					System.getProperty("line.separator"));
			configMap.put(ConfigConstant.SYSTEM_FILESEPARATOR,
					System.getProperty("file.separator"));
			configMap.put(ConfigConstant.SYSTEM_FILEENCODING,
					System.getProperty("file.encoding"));

			configMap.put(ConfigConstant.APP_DEBUGMODE, "false");
			configMap.put(ConfigConstant.APP_PREFIX, "monsoon");
			
			Map<String,String> config=ResourceReader.getAll(ConfigConstant.CONFIGFILENAME);
			configMap.putAll(config);
		}
	}

	

	public static String getClassPath() {
		if (ClassPath == null) {
			URL url = Thread.currentThread().getContextClassLoader()
					.getResource("");
			try {
				String path = URLDecoder.decode(url.getPath(),
						getValue(ConfigConstant.SYSTEM_FILEENCODING));
				if (System.getProperty("os.name").toLowerCase()
						.indexOf("windows") >= 0) {
					if (path.startsWith("/"))
						path = path.substring(1);
					else if (path.startsWith("file:/")) {
						path = path.substring(6);
					}
				} else if (path.startsWith("file:/")) {
					path = path.substring(5);
				}
				ClassPath = path;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		return ClassPath;
	}

	/**
	 * <设置web启动成功时间><br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 * @param time
	 */
	public static void setSystemUpTime(long time) {
		configMap.put(ConfigConstant.SYSTEM_UPTIME, String.valueOf(time));
	}

	/**
	 * <获取配置项的值><br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 * @param configName
	 * @return
	 */
	public static String getValue(String configName) {
		init();
		return configMap.get(configName);
	}

	/**
	 * <是否为调试模式><br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 * @return
	 */
	public static boolean isDebugMode() {
		return "true".equalsIgnoreCase(getValue(ConfigConstant.APP_DEBUGMODE));
	}

	public static String getContainerInfo() {
		if (configMap.containsKey(ConfigConstant.SYSTEM_CONTAINERINFO)) {
			return configMap.get(ConfigConstant.SYSTEM_CONTAINERINFO);
		} else {
			return "unknown";
		}
	}

	public static String getGlobalCharset() {
		return "UTF-8";
	}

}
