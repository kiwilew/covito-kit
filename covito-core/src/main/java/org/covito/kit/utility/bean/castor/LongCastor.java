package org.covito.kit.utility.bean.castor;

import org.covito.kit.utility.ObjectUtil;

public class LongCastor extends AbstractInnerCastor {
	private static LongCastor singleton = new LongCastor();

	public static LongCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (Long.class == type) || (Long.TYPE == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return Integer.valueOf(0);
		}
		if ((obj instanceof Number))
			return Long.valueOf(((Number) obj).longValue());
		try {
			String str = obj.toString();
			if (ObjectUtil.empty(str)) {
				return Integer.valueOf(0);
			}
			return Long.valueOf(Long.parseLong(obj.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Long.valueOf(0L);
	}
}
