package com.covito.kit.utility;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 一句话功能简述<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-24]
 */
public class FreemarkerUtil {
	
	/**
	 * @Fields cfg :(变量)
	 */ 
	public static Configuration cfg;

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @throws IOException 如字段定义
	 */ 
	public FreemarkerUtil() throws IOException {
		init();
	}

	
	/**
	 * @Title: init
	 * @Description: (方法)
	 * @throws IOException 如方法
	 */ 
	private static void init() throws IOException {
		cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(""));
		cfg.setDefaultEncoding("GBK");
	}

	
	/**
	 * @Title: getConfiguration
	 * @Description: (实例化 Configuration对象)
	 * @return Configuration
	 * @throws IOException 如方法
	 */ 
	private static Configuration getConfiguration() throws IOException {
		if (cfg == null)
			init();
		return (cfg);
	}

	/**
	 * @Title: process
	 * @Description: (通过模板生成页面)
	 * @param map 模板中需要的数据
	 * @param templatePath 模板路径
	 * @return 返回生成的页面中的字符串
	 * @throws Exception 如方法
	 */ 
	public static String process(Map<String,Object> map, String templatePath) throws Exception {
		Configuration conf = getConfiguration();
		conf.setDefaultEncoding("UTF-8");
		Template temp = conf.getTemplate(templatePath);
		StringWriter sw = new StringWriter();
		temp.setEncoding("UTF-8");
		temp.setOutputEncoding("UTF-8");
		temp.process(map, sw);
		return (sw.toString());
	}
	/**
	 * @Title: process
	 * @Description: (通过模板生成页面)
	 * @param map 模板中需要的数据
	 * @param str 带标签的字符串
	 * @return 返回生成的页面中的字符串
	 * @throws Exception 如方法
	 */ 
	public static String processString(Map<String,Object> map, String str) throws Exception {
		Configuration cfg = new Configuration();
		StringTemplateLoader stl=new StringTemplateLoader();
		stl.putTemplate("name", str);
		cfg.setTemplateLoader(stl);
		cfg.setDefaultEncoding("UTF-8");
		Template template = cfg.getTemplate("name");
		
		StringWriter writer = new StringWriter();
		template.process(map, writer);
		String result=  writer.toString();
		
		return result;
	}

	/**
	 * @Title: process
	 * @Description: (通过模板生成页面)
	 * @param ob 模板中需要的数据
	 * @param templatePath 模板路径
	 * @return 返回生成的页面中的字符串
	 * @throws Exception 如方法
	 */ 
	public static String process(Object ob, String templatePath) throws Exception {
		Configuration conf = getConfiguration();
		Template temp = conf.getTemplate(templatePath);
		StringWriter sw = new StringWriter();
		temp.setEncoding("UTF-8");
		temp.setOutputEncoding("UTF-8");
		temp.process(ob, sw);
		return (sw.toString());
	}
}
