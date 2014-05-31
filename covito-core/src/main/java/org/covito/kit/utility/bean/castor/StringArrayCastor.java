package org.covito.kit.utility.bean.castor;

public class StringArrayCastor extends AbstractInnerCastor {
	private static StringArrayCastor singleton = new StringArrayCastor();

	public static StringArrayCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return String[].class == type;
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof String[]))
			return (String[]) obj;
		if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			String[] arr = new String[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = (os[i] == null ? null : os[i].toString());
			}
			return arr;
		}
		if (((obj instanceof String)) && (obj.toString().indexOf(",") > 0)) {
			return obj.toString().split(",");
		}
		return new String[] { obj.toString() };
	}
}
