package com.covito.kit.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.covito.kit.core.Filter;

/**
 * Object工具<br/>
 * <提供Object数组，List,Map 进行排序，过滤，找最大最小值，拷贝,转换,Object判等,判空>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-20]
 */
public class ObjectUtil {
	
	/** 
	 * <判断obj是否为空><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param obj
	 * @return
	 */
	public static boolean empty(Object obj) {
		if (obj == null) {
			return true;
		}
		if ((obj instanceof String)) {
			return obj.equals("");
		}
		if ((obj instanceof Number)) {
			return ((Number) obj).doubleValue() == 0.0D;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if ((obj instanceof Collection)) {
			return ((Collection) obj).size() == 0;
		}
		return false;
	}

	/** 
	 * <判断obj是否不为空><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param obj
	 * @return
	 */
	public static boolean notEmpty(Object obj) {
		return !empty(obj);
	}

	/** 
	 * <两个obj是否相等><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean equal(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 == null) {
			return obj2 == null;
		}

		return obj1.equals(obj2);
	}

	/** 
	 * <两个obj是否不相等><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean notEqual(Object obj1, Object obj2) {
		return !equal(obj1, obj2);
	}

	/** 
	 * <数字中的最小值><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param args
	 * @return
	 */
	public static Number minNumber(Number[] args) {
		if ((args == null) || (args.length == 0)) {
			return null;
		}
		Number minus = null;
		for (int i = 0; i < args.length; i++) {
			Number t = args[i];
			if (minus == null) {
				minus = t;
			} else if (minus.doubleValue() > t.doubleValue()) {
				minus = t;
			}
		}
		return minus;
	}

	/** 
	 * <数字中的最大值><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param args
	 * @return
	 */
	public static Number maxNumber(Number[] args) {
		if ((args == null) || (args.length == 0)) {
			return null;
		}
		Number max = null;
		for (int i = 0; i < args.length; i++) {
			Number t = args[i];
			if (max == null) {
				max = t;
			} else if (max.doubleValue() < t.doubleValue()) {
				max = t;
			}
		}
		return max;
	}

	/** 
	 * <找到最小值><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param args
	 * @return
	 */
	public static <T extends Comparable<T>> T min(T[] args) {
		if ((args == null) || (args.length == 0)) {
			return null;
		}
		T minus = null;
		for (int i = 0; i < args.length; i++) {
			T t = args[i];
			if ((minus == null) && (t != null)) {
				minus = t;
			} else if (minus.compareTo(t) > 0) {
				minus = t;
			}
		}
		return minus;
	}

	/** 
	 * <找到最大值><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param args
	 * @return
	 */
	public static <T extends Comparable<T>> T max(T[] args) {
		if ((args == null) || (args.length == 0)) {
			return null;
		}
		T max = null;
		for (int i = 0; i < args.length; i++) {
			T t = args[i];
			if ((max == null) && (t != null)) {
				max = t;
			} else if (max.compareTo(t) < 1) {
				max = t;
			}
		}
		return max;
	}

	/** 
	 * <如果 obj1为空，返回obj2，如果obj1不为空，返回obj1><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param obj1 判定值
	 * @param obj2 判定值为空时的默认值
	 * @return
	 */
	public static <T> T ifEmpty(T obj1, T obj2) {
		return empty(obj1) ? obj2 : obj1;
	}

	/** 
	 * <打印obj><br/>
	 * <当Obj是个数组，会以逗号分隔>
	 *
	 * @author  eighteencold
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if(obj instanceof Object[]){
			StringBuffer result=new StringBuffer();
			for(Object o:(Object[])obj){
				if(o==null){
					continue;
				}
				result.append(o.toString());
				result.append(",");
			}
			if(result.length()>0){
				result.deleteCharAt(result.length()-1);
			}
			return result.toString();
		}
		return obj == null ? null : obj.toString();
	}

	/** 
	 * <将数组转成List><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param args
	 * @return
	 */
	public static <T> List<T> toList(T[] args) {
		return Arrays.asList(args);
	}
	
	/** 
	 * <过滤数组不中符合过滤条件数据><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param filter
	 * @return
	 */
	public static <T> T[] filter(T[] arr, Filter<T> filter) {
		if ((arr == null) || (arr.length == 0)) {
			return arr;
		}
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < arr.length; i++) {
			T t = arr[i];
			if (filter.filter(t)) {
				list.add(t);
			}
		}
		T[] result = (T[]) Array.newInstance(arr.getClass().getComponentType(), list.size());
		int i = 0;
		for (T t:list) {
			result[(i++)] = t;
		}
		return result;
	}

	/** 
	 * <过滤List不中符合过滤条件数据><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param filter
	 * @return
	 */
	public static <T> List<T> filter(List<T> arr, Filter<T> filter) {
		if ((arr == null) || (arr.size() == 0)) {
			return arr;
		}
		try {
			List<T> list = (List<T>) arr.getClass().newInstance();
			for (T t : arr) {
				if (filter.filter(t)) {
					list.add(t);
				}
			}
			return list;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <过滤Map中的不符合条件的数据><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param map
	 * @param filter
	 * @return
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map,Filter<Map.Entry<K, V>> filter) {
		if ((map == null) || (map.size() == 0)) {
			return map;
		}
		Map<K, V> map2=null;
		try {
			map2 = (Map<K, V>)map.getClass().newInstance();
			for (Map.Entry<K,V> t : map.entrySet()) {
				if (filter.filter(t)) {
					map2.put(t.getKey(), t.getValue());
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return map2;
	}

	/** 
	 * <转成int数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static int[] toIntArray(Object[] arr) {
		int[] arr2 = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Integer.parseInt(String.valueOf(arr[i]));
		}
		return arr2;
	}

	/** 
	 * <转成Long数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static long[] toLongArray(Object[] arr) {
		long[] arr2 = new long[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Long.parseLong(String.valueOf(arr[i]));
		}
		return arr2;
	}

	/** 
	 * <转成Float数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static float[] toFloatArray(Object[] arr) {
		float[] arr2 = new float[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Float.parseFloat(String.valueOf(arr[i]));
		}
		return arr2;
	}

	/** 
	 * <转成Double数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static double[] toDoubleArray(Object[] arr) {
		double[] arr2 = new double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Double.parseDouble(String.valueOf(arr[i]));
		}
		return arr2;
	}

	/** 
	 * <转成Boolean数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static boolean[] toBooleanArray(Object[] arr) {
		boolean[] arr2 = new boolean[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = Boolean.valueOf(String.valueOf(arr[i])).booleanValue();
		}
		return arr2;
	}

	/** 
	 * <转成String数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static String[] toStringArray(Object[] arr) {
		String[] arr2 = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = String.valueOf(arr[i]);
		}
		return arr2;
	}

	/** 
	 * <转成Object数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @return
	 */
	public static <T> Object[] toObjectArray(T[] arr) {
		Object[] arr2 = new Object[arr.length];
		for (int i = 0; i < arr.length; i++) {
			arr2[i] = arr[i];
		}
		return arr2;
	}

	/** 
	 * <对数组进行排序><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param c
	 * @return
	 */
	public static <T> T[] sort(T[] arr, Comparator<T> c) {
		if ((arr == null) || (arr.length == 0)) {
			return arr;
		}
		T[] a = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length);
		for (int i = 0; i < arr.length; i++) {
			a[i] = arr[i];
		}
		Arrays.sort(a, c);
		return a;
	}

	/** 
	 * <对List进行排序><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param c
	 * @return
	 */
	public static <T> List<T> sort(List<T> arr, Comparator<T> c) {
		if ((arr == null) || (arr.size() == 0)) {
			return arr;
		}
		T[] a = (T[]) Array.newInstance(arr.toArray().getClass().getComponentType(), arr.size());
		a = arr.toArray(a);
		Arrays.sort(a, c);
		return toList(a);
	}

	/** 
	 * <对Map进行排序><br/>
	 * <Map必须是有序的>
	 *
	 * @author  eighteencold
	 * @param map
	 * @param c
	 * @return
	 */
	public static <K, V> Map<K, V> sort(Map<K, V> map,Comparator<Map.Entry<K, V>> c) {
		if ((map == null) || (map.size() == 0)) {
			return map;
		}
		try {
			Map.Entry<K, V>[] a = (Map.Entry<K, V>[]) Array.newInstance(Map.Entry.class.getComponentType(), map.size());
			Map<K, V> map2 = (Map<K, V>) map.getClass().newInstance();
			a = (Map.Entry<K, V>[]) map.entrySet().toArray(a);
			Arrays.sort(a, c);
			for (Map.Entry<K, V> t : a) {
				map2.put(t.getKey(), t.getValue());
			}
			return map2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <从数组copy指定长度生成新的数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr 
	 * @param length copy长度
	 * @return
	 */
	public static <T> T[] copyOf(T[] arr, int length) {
		T[] copy = (T[]) Array.newInstance(arr.getClass().getComponentType(), length);
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	/** 
	 * <从字节数组copy指定长度生成新的数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param length
	 * @return
	 */
	public static byte[] copyOf(byte[] arr, int length) {
		byte[] copy = new byte[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	/** 
	 * <从boolean数组copy指定长度生成新的数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param length
	 * @return
	 */
	public static boolean[] copyOf(boolean[] arr, int length) {
		boolean[] copy = new boolean[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	/** 
	 * <从float数组copy指定长度生成新的数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param length
	 * @return
	 */
	public static float[] copyOf(float[] arr, int length) {
		float[] copy = new float[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

	/** 
	 * <从int数组copy指定长度生成新的数组><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param arr
	 * @param length
	 * @return
	 */
	public static int[] copyOf(int[] arr, int length) {
		int[] copy = new int[length];
		System.arraycopy(arr, 0, copy, 0, Math.min(arr.length, length));
		return copy;
	}

}
