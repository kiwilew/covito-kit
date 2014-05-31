package org.covito.kit.utility.bean.castor;

public class LongArrayCastor extends AbstractInnerCastor {
	private static LongArrayCastor singleton = new LongArrayCastor();

	public static LongArrayCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (Long[].class == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof long[]))
			return (long[]) obj;
		if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			long[] arr = new long[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = ((Long) LongCastor.getInstance().cast(os[i], type))
						.longValue();
			}
			return arr;
		}
		if (((obj instanceof String))
				&& (DoubleArrayCastor.isNumberArray(obj.toString()))) {
			double[] ds = DoubleArrayCastor.toDoubleArray(obj.toString());
			long[] arr = new long[ds.length];
			for (int i = 0; i < ds.length; i++) {
				arr[i] = new Double(ds[i]).longValue();
			}
			return arr;
		}
		return new long[] { ((Long) LongCastor.getInstance().cast(obj, type))
				.longValue() };
	}
}

