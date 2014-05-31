package org.covito.kit.utility.bean.castor;

import java.lang.reflect.Array;

public class GenricArrayCastor extends AbstractInnerCastor {
	private static GenricArrayCastor singleton = new GenricArrayCastor();

	public static GenricArrayCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return type.isArray();
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if (obj.getClass().isArray()) {
			if (type.isAssignableFrom(obj.getClass())) {
				return obj;
			}
			Object[] os = (Object[]) obj;
			Object[] arr = (Object[]) Array.newInstance(
					type.getComponentType(), os.length);
			for (int i = 0; i < os.length; i++) {
				arr[i] = (os[i] == null ? null : CastorService.toType(obj, type
						.getComponentType()));
			}
			return arr;
		}
		return new Object[] { CastorService
				.toType(obj, type.getComponentType()) };
	}
}

