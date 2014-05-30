package com.covito.kit.utility.bean.castor;

import java.math.BigDecimal;

import com.covito.kit.utility.ObjectUtil;

public class BigDecimalCastor extends AbstractInnerCastor {
	private static BigDecimalCastor singleton = new BigDecimalCastor();

	public static BigDecimalCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (BigDecimal.class == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return BigDecimal.valueOf(0);
		}
		if ((obj instanceof Number)){
			return BigDecimal.valueOf(Double.valueOf(((Number) obj).doubleValue()));
		}
		try {
			String str = obj.toString();
			if (ObjectUtil.empty(str)) {
				return BigDecimal.valueOf(0);
			}
			return BigDecimal.valueOf(Double.valueOf(obj.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BigDecimal.valueOf(0L);
	}
}
