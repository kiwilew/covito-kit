package org.covito.kit.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解定义
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	/**
	 * 字段排序（升序）
	 */
	int sort();
	
	/**
	 * 导出——字段标题
	 */
	String title() default "";
	
	/**
	 * 动作范围
	 */
	ExAct type() default ExAct.both;
	
	/**
	 * 导出字段对齐方式（0：自动；1：靠左；2：居中；3：靠右）
	 * 
	 * 备注：Integer/Long类型设置居右对齐（align=3）
	 */
	ExAlign align() default ExAlign.auto;
	
	/**
	 * 字段值处理器
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends ValueHandler> handler() default NULLHolder.class;
	
	public static enum ExAct{
		imp,exp,both
	}
	
	public static enum ExAlign{
		auto(0),left(1),center(2),right(3);
		private int value;
		ExAlign(int value){
			this.value=value;
		}
		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 导入导出转换器
	 * @param <E> Excel中的字段类型
	 * @param <M> 实体中的字段类型
	 */
	public static interface ValueHandler<E,M>{
		
		/**
		 * 导入值转换
		 * @param value
		 * @return
		 */
		M impConvert(E value);
		
		/**
		 * 导出值转换
		 * @param value
		 * @return
		 */
		E expConvert(M value);
		
	}
	
	public static class NULLHolder implements ValueHandler<Object, Object>{
		@Override
		public Object impConvert(Object value) {
			return value;
		}

		@Override
		public Object expConvert(Object value) {
			return null;
		}
	}
	
}
