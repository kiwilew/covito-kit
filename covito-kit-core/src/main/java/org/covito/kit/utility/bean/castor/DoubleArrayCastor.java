package org.covito.kit.utility.bean.castor;

public class DoubleArrayCastor extends AbstractInnerCastor {

	private static DoubleArrayCastor singleton = new DoubleArrayCastor();

	public static DoubleArrayCastor getInstance() {
		return singleton;
	}

	public boolean canCast(Class<?> type) {
		return (Double[].class == type);
	}

	public Object cast(Object obj, Class<?> type) {
		if (obj == null) {
			return null;
		}
		if ((obj instanceof double[]))
			return (double[]) obj;
		if (obj.getClass().isArray()) {
			Object[] os = (Object[]) obj;
			double[] arr = new double[os.length];
			for (int i = 0; i < os.length; i++) {
				arr[i] = ((Double) DoubleCastor.getInstance().cast(os[i], type))
						.doubleValue();
			}
			return arr;
		}
		if (((obj instanceof String)) && (isNumberArray(obj.toString()))) {
			return toDoubleArray(obj.toString());
		}
		return new double[] { ((Double) DoubleCastor.getInstance().cast(obj,
				type)).doubleValue() };
	}

	public static boolean isNumberArray(String str) {
		if (!Character.isDigit(str.charAt(0))) {
			return false;
		}
		if (!Character.isDigit(str.charAt(str.length() - 1))) {
			return false;
		}
		for (int i = 1; i < str.length() - 1; i++) {
			char c = str.charAt(i);
			if (c == ' ') {
				for (int j = i + 1; j < str.length(); j++) {
					if (str.charAt(j) != ' ')
						break;
					i++;
				}

				i++;
				if (str.charAt(i) != ',') {
					return false;
				}
				for (int j = i + 1; j < str.length(); j++) {
					if (str.charAt(j) != ' ')
						break;
					i++;
				}

			} else if (c == '.') {
				if (!Character.isDigit(str.charAt(i + 1)))
					return false;
			} else if (c == ',') {
				for (int j = i + 1; j < str.length(); j++) {
					if (str.charAt(j) != ' ')
						break;
					i++;
				}

				if (!Character.isDigit(str.charAt(i + 1)))
					return false;
			} else if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public static double[] toDoubleArray(String str) {
		String[] arr = str.split(",");
		double[] r = new double[arr.length];
		for (int i = 0; i < arr.length; i++) {
			r[i] = Double.parseDouble(arr[i].trim());
		}
		return r;
	}
}
