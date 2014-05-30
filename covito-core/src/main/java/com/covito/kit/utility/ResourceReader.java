/*
 * 文 件 名:  ResourceReadUtil.java
 *
 * 描    述:  <描述>
 * 创 建 人:  eighteencold
 * 创建时间:  2014-2-12
 */
package com.covito.kit.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 资源文件读取类<br/>
 * <功能详细描述>
 * 
 * @author eighteencold
 * @version [v1.0, 2014-2-12]
 */
public class ResourceReader {

	protected Properties config = new Properties();

	private Log log = LogFactory.getLog(ResourceReader.class);

	private static Map<String, ResourceReader> resourceMap = new HashMap<String, ResourceReader>();

	/**
	 * <私有化默认构造函数>s
	 */
	private ResourceReader() {

	}

	/**
	 * 单例<br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 * @param path
	 * @return
	 */
	private static ResourceReader getInstance(String name) {
		if (resourceMap.get(name) == null) {
			ResourceReader resource = new ResourceReader();
			resourceMap.put(name, resource);
			resource.init(name);
		}
		return resourceMap.get(name);
	}

	private void init(String name) {
		if (name == null) {
			return;
		}
		if(!name.startsWith("/")){
			name="/"+name;
		}
		InputStream is = getClass().getResourceAsStream(name);
		if(is==null){
			log.error("can't find file ["+name+"]");
			return;
		}
		try {
			config.load(is);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	

	public static String getValue(String name, String key) {
		String re = getInstance(name).config.getProperty(key);
		if (re == null) {
			return null;
		}
		for (; re.indexOf("${") >= 0;) {
			String para = re.substring(re.indexOf("${") + 2, re.indexOf("}"));
			if (getValue(name, para) != null) {
				re = re.replace("${" + para + "}", getValue(name, para));
			}
		}
		return re;
	}
	
	/** 
	 * 获取<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param name
	 * @return
	 */
	public static Map<String,String> getAll(String name) {
		Set<Object> re = getInstance(name).config.keySet();
		if (re == null) {
			return new HashMap<String, String>();
		}
		Map<String,String> result=new HashMap<String, String>();
		for(Object key:re){
			String v=getInstance(name).config.get(key).toString();
			for (; v.indexOf("${") >= 0;) {
				String para = v.substring(v.indexOf("${") + 2, v.indexOf("}"));
				if (getValue(name, para) != null) {
					v = v.replace("${" + para + "}", getValue(name, para));
				}
			}
			result.put(key.toString(), v);
		}
		return result;
	}

}
