package com.covito.kit.utility.bean.castor;

import java.sql.Time;
import java.util.Date;

import com.covito.kit.utility.DateUtil;

public class TimeCastor extends AbstractInnerCastor {
	private static TimeCastor singleton = new TimeCastor();

	public static TimeCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return Time.class == type;
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof Time))
			return (Time) obj;
		if ((obj instanceof Date))
			return new Time(((Date) obj).getTime());
		if ((obj instanceof Long)) {
			return new Time(((Long) obj).longValue());
		}
		Date d = DateUtil.parseDateTime(obj.toString());
		if (d == null) {
			return null;
		}
		return new Time(d.getTime());
	}
}
