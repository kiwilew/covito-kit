package org.covito.kit.utility.bean.castor;

import java.sql.Timestamp;

import org.covito.kit.utility.DateUtil;


public class TimestampCastor extends AbstractInnerCastor {
	private static TimestampCastor singleton = new TimestampCastor();

	public static TimestampCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return Timestamp.class == type;
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof Timestamp))
			return (Timestamp) obj;
		if ((obj instanceof java.util.Date))
			return new Timestamp(((java.util.Date) obj).getTime());
		if ((obj instanceof Long)) {
			return new Timestamp(((Long) obj).longValue());
		}
		java.util.Date d = DateUtil.parseDateTime(obj.toString());
		if (d == null) {
			return null;
		}
		return new Timestamp(d.getTime());
	}
}
