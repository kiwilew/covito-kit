package org.covito.kit.utility.bean;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 实体转换工具类<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-18]
 */
public class BeanUtil {
	
	/** 
	 * <将Map中key与属性值相同的值填充到bean中><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param bean
	 * @param values
	 */
	public static void fill(Object bean, Map<?, ?> values) {
		fill(bean, values, "");
	}
	
	public static void fill(Object bean, Map<?, ?> values, String valuePrefix) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object k: values.keySet()) {
			if (k != null) {
				String key2 = k.toString();
				if (key2.startsWith(valuePrefix)) {
					map.put(key2.substring(valuePrefix.length()), values.get(k));
				}
			}
		}
		BeanDescription bim = BeanManager.getBeanDescription(bean.getClass());
		fillArrays(bean, map);
		fillInnerBeans(bean, map);
		for (Object k:map.keySet()) {
			if (k == null) {
				continue;
			}
			String key2 = k.toString();
			if ((key2.indexOf("[") > 0) || (key2.indexOf(".") > 0)) {
				continue;
			}
			Object v = map.get(k);
			BeanProperty bip = bim.getProperty(key2);
			if (bip != null)
				bip.write(bean, v);
		}
	}

	private static void fillArrays(Object bean, Map<String, Object> map) {
		BeanDescription bim = BeanManager.getBeanDescription(bean.getClass());
		HashMap<String,Integer> arraySizes = new HashMap<String,Integer>();
		int i1;
		for (Iterator<String> localIterator = map.keySet().iterator(); localIterator
				.hasNext();) {
			Object k = localIterator.next();
			if (k != null) {
				String key = k.toString();
				if (key.indexOf("[") > 0) {
					i1 = key.indexOf("[");
					int i2 = key.indexOf("]");
					String prefix = key.substring(0, i1);
					int index = 0;
					try {
						index = Integer.parseInt(key.substring(i1 + 1, i2));
						if (arraySizes.get(prefix) == null) {
							arraySizes.put(prefix, Integer.valueOf(index));
						} else if (((Integer) arraySizes.get(prefix))
								.intValue() < index) {
							arraySizes.put(prefix, Integer.valueOf(index));
						}
					} catch (Exception localException) {
					}
				}
			}
		}
		Object[] obj=new Object[arraySizes.keySet().size()];
		int key = (obj = arraySizes.keySet().toArray()).length;
		for (int str1 = 0; str1 < key; str1++) {
			Object k = obj[str1];
			String prefix = k.toString();
			BeanProperty bip = bim.getProperty(prefix);
			if ((bip == null) || (!bip.getPropertyType().isArray())) {
				arraySizes.remove(k);
			} else {
				Object[] arr = (Object[]) createArray(bip.getPropertyType(),
						((Integer) arraySizes.get(prefix)).intValue() + 1);
				for (int i = 0; i < arr.length; i++) {
					arr[i] = create(bip.getPropertyType().getComponentType());
					fill(arr[i], map, prefix + "[" + i + "].");
				}
				bip.write(bean, arr);
			}
		}
	}

	private static void fillInnerBeans(Object bean, Map<String, Object> map) {
		BeanDescription bim = BeanManager.getBeanDescription(bean.getClass());
		HashMap<String,String> innerBeans = new HashMap<String,String>();
		for (String k : map.keySet()) {
			if (k != null) {
				String key = k.toString();
				if (key.indexOf("[") > 0) {
					continue;
				}
				int i = key.indexOf(".");
				if (i > 0) {
					innerBeans.put(key.substring(0, i), "");
				}
			}
		}
		for (String name : innerBeans.keySet()) {
			BeanProperty bip = bim.getProperty(name);
			if (bip != null) {
				Object v = bip.read(bean);
				if (v == null) {
					v = create(bip.getPropertyType());
					bip.write(bean, v);
				}
				if (!(v instanceof Date))
					fill(v, map, name + ".");
			}
		}
	}

	/** 
	 * <反射创建实体><br/>
	 * <调用示例：KO b=BeanUtil.create(KO.class);>
	 *
	 * @author  eighteencold
	 * @param type
	 * @return
	 */
	public static <T> T create(Class<T> type) {
		try {
			if (Modifier.isAbstract(type.getModifiers())) {
				throw new RuntimeException("Bean class " + type.getName()
						+ " is a abstract class!");
			}
			return type.newInstance();
		} catch (Exception e) {
		}
		throw new RuntimeException("Bean class " + type.getName()
				+ " has not default constructor!");
	}

	/** 
	 * <创建指定长度的数组><br/>
	 * <调用示例：KO [] bean=BeanUtil.createArray(KO.class, 4);>
	 *
	 * @author  eighteencold
	 * @param type
	 * @param length
	 * @return
	 */
	public static  <T> T[] createArray(Class<T> type, int length) {
		try {
			if (Modifier.isAbstract(type.getModifiers())) {
				throw new RuntimeException("Bean class " + type.getName()+ " is a abstract class!");
			}
			return (T[])Array.newInstance(type, length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Bean class " + type.getName()+ " has not default constructor!");
	}

	/** 
	 * <将srcBean的中属性覆盖到targetBean><br/>
	 * <srcBean中的null值也将覆盖掉原有值>
	 *
	 * @author  eighteencold
	 * @param srcBean
	 * @param targetBean
	 */
	public static void copyProperties(Object srcBean, Object targetBean) {
		BeanDescription m1 = BeanManager.getBeanDescription(srcBean.getClass());
		BeanDescription m2 = BeanManager.getBeanDescription(targetBean
				.getClass());
		for (String k1 : m1.getPropertyMap().keySet()){
			for (String k2 : m2.getPropertyMap().keySet()){
				if(k1.equals("class")){
					continue;
				}
				if (k1.equals(k2)){
					m2.getProperty(k2).write(targetBean,
							m1.getProperty(k1).read(srcBean));
				}
			}
		}
	}

	/** 
	 * <获取bean中的指定值><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param bean
	 * @param field
	 * @return
	 */
	public static Object get(Object bean, String field) {
		BeanDescription bim = BeanManager.getBeanDescription(bean.getClass());
		BeanProperty bip = bim.getProperty(field);
		return bip.read(bean);
	}

	/** 
	 * <给bean的属性赋值或修改属性性><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param bean
	 * @param field
	 * @param value
	 */
	public static void set(Object bean, String field, Object value) {
		BeanDescription bim = BeanManager.getBeanDescription(bean.getClass());
		BeanProperty bip = bim.getProperty(field);
		if (bip != null){
			bip.write(bean, value);
		}
	}

	/** 
	 * <将bean转成Map><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param bean
	 * @return
	 */
	public static Map<String, Object> toMap(Object bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		BeanDescription m1 = BeanManager.getBeanDescription(bean.getClass());
		for (String k1 : m1.getPropertyMap().keySet()){
			if(k1.equals("class")){
				continue;
			}
			map.put(k1, m1.getProperty(k1).read(bean));
		}
		return map;
	}
}

