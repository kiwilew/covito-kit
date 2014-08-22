package org.covito.kit.utility.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Map;

public class BeanManager {
	
	static Map<Class<?>, BeanDescription> managerMap = new HashMap<Class<?>, BeanDescription>();

	public static BeanDescription getBeanDescription(Class<?> clazz) {
		BeanDescription ret = (BeanDescription) managerMap.get(clazz);
		if (ret == null) {
			ret = createBeanManager(clazz);
		}
		return ret;
	}

	static BeanDescription createBeanManager(Class<?> pClass) {
		BeanDescription ret = (BeanDescription) managerMap.get(pClass);
		if (ret == null) {
			ret = new BeanDescription(pClass);
			managerMap.put(pClass, ret);
		}
		return ret;
	}

	static Method getPublicMethod(Method m) {
		if (m == null) {
			return null;
		}
		Class clazz = m.getDeclaringClass();
		if (Modifier.isPublic(clazz.getModifiers())) {
			return m;
		}
		Method ret = getPublicMethod(clazz, m);
		if (ret != null) {
			return ret;
		}
		return m;
	}

	static Method getPublicMethod(Class<?> clazz, Method pMethod) {
		if (Modifier.isPublic(clazz.getModifiers()))
			try {
				Method m;
				try {
					m = clazz.getDeclaredMethod(pMethod.getName(), pMethod
							.getParameterTypes());
				} catch (AccessControlException ex) {
					m = clazz.getMethod(pMethod.getName(), pMethod
							.getParameterTypes());
				}
				if (Modifier.isPublic(m.getModifiers()))
					return m;
			} catch (NoSuchMethodException localNoSuchMethodException) {
			}
		Class[] interfaces = clazz.getInterfaces();
		if (interfaces != null) {
			for (int i = 0; i < interfaces.length; i++) {
				Method m = getPublicMethod(interfaces[i], pMethod);
				if (m != null) {
					return m;
				}
			}
		}
		Class superclass = clazz.getSuperclass();
		if (superclass != null) {
			Method m = getPublicMethod(superclass, pMethod);
			if (m != null) {
				return m;
			}
		}
		return null;
	}
}
