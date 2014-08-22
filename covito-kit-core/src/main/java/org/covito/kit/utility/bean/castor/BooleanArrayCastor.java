package org.covito.kit.utility.bean.castor;

public class BooleanArrayCastor extends AbstractInnerCastor {
	private static BooleanArrayCastor singleton = new BooleanArrayCastor();

	public static BooleanArrayCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (Boolean[].class == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof boolean[]))
			return (boolean[]) obj;
		if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			boolean[] arr = new boolean[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = ((Boolean) BooleanCastor.getInstance().cast(os[i],
						type)).booleanValue();
			}
			return arr;
		}
		return new boolean[] { ((Boolean) BooleanCastor.getInstance().cast(obj,
				type)).booleanValue() };
	}
}

