package org.covito.kit.utility.bean.castor;

import org.covito.kit.utility.ObjectUtil;

public class DoubleCastor extends AbstractInnerCastor {
	private static DoubleCastor singleton = new DoubleCastor();

	public static DoubleCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (Double.class == type) || (Double.TYPE == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return Integer.valueOf(0);
		}
		if ((obj instanceof Number)) {
			return Double.valueOf(((Number) obj).doubleValue());
		}
		String str = obj.toString();
		if (ObjectUtil.empty(str))
			return Integer.valueOf(0);
		try {
			return Double.valueOf(Double.parseDouble(obj.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Double.valueOf(0.0D);
	}
}
