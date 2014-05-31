package org.covito.kit.utility.bean.castor;

import java.sql.Date;

import org.covito.kit.utility.DateUtil;


public class SqlDateCastor extends AbstractInnerCastor {
	private static SqlDateCastor singleton = new SqlDateCastor();

	public static SqlDateCastor getInstance() {
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
		if ((obj instanceof java.util.Date))
			return new Date(((java.util.Date) obj).getTime());
		if ((obj instanceof Long)) {
			return new Date(((Long) obj).longValue());
		}
		java.util.Date d = DateUtil.parseDateTime(obj.toString());
		if (d == null) {
			return null;
		}
		return new Date(d.getTime());
	}
}
