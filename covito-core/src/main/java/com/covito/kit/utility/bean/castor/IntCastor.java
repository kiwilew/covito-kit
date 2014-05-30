package com.covito.kit.utility.bean.castor;

import com.covito.kit.utility.ObjectUtil;

public class IntCastor extends AbstractInnerCastor {
	private static IntCastor singleton = new IntCastor();

	public static IntCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (Integer.class == type) || (Integer.TYPE == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return Integer.valueOf(0);
		}
		if ((obj instanceof Number))
			return Integer.valueOf(((Number) obj).intValue());
		try {
			String str = obj.toString();
			if (ObjectUtil.empty(str)) {
				return Integer.valueOf(0);
			}
			return Integer.valueOf(Integer.parseInt(obj.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Integer.valueOf(0);
	}
}
