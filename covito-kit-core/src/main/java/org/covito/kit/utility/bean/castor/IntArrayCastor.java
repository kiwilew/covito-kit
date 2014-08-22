package org.covito.kit.utility.bean.castor;

public class IntArrayCastor extends AbstractInnerCastor {
	private static IntArrayCastor singleton = new IntArrayCastor();

	public static IntArrayCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (Integer[].class == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof int[])) {
			return (int[]) obj;
		}

		if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			int[] arr = new int[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = ((Integer) IntCastor.getInstance().cast(os[i], type))
						.intValue();
			}
			return arr;
		}
		if (((obj instanceof String))
				&& (DoubleArrayCastor.isNumberArray(obj.toString()))) {
			double[] ds = DoubleArrayCastor.toDoubleArray(obj.toString());
			int[] arr = new int[ds.length];
			for (int i = 0; i < ds.length; i++) {
				arr[i] = new Double(ds[i]).intValue();
			}
			return arr;
		}
		return new int[] { ((Integer) IntCastor.getInstance().cast(obj, type))
				.intValue() };
	}
}
