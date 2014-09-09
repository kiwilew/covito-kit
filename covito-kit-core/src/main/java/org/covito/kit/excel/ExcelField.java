package org.covito.kit.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解定义
 * @author ThinkGem
 * @version 2013-03-10
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	/**
	 * 导入——在excel表中的列名，如A,B,C
	 */
	String index();
	
	/**
	 * 导出——字段标题（需要添加批注请用“**”分隔，标题**批注，仅对导出模板有效）
	 */
	String title() default "";
	
	/**
	 * 动作范围
	 */
	ExAct type() default ExAct.both;

	/**
	 * 导出——字段字段排序（升序）
	 */
	int sort() default 0;
	
	/**
	 * 字段值处理器
	 * @return
	 */
	Class<? extends ValueHandler> handler() default NULLHolder.class;
	
	public static enum ExAct{
		imp,exp,both
	}
	
	public static interface ValueHandler{
		Object dealValue(Object value);
	}
	
	public static class NULLHolder implements ValueHandler{
		@Override
		public Object dealValue(Object value) {
			return value;
		}
	}
}
