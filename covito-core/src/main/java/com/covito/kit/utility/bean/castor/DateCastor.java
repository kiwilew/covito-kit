package com.covito.kit.utility.bean.castor;

import java.util.Date;

import com.covito.kit.utility.DateUtil;

public class DateCastor extends AbstractInnerCastor {
	private static DateCastor singleton = new DateCastor();

	public static DateCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return Date.class == type;
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof Date))
			return (Date) obj;
		if ((obj instanceof Long)) {
			return new Date(((Long) obj).longValue());
		}
		return DateUtil.parseDateTime(obj.toString());
	}
}
