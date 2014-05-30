package com.covito.kit.utility.bean.castor;

import java.util.List;

public class ListCastor extends AbstractInnerCastor {
	public boolean canCast(Class<?> type) {
		return List.class.isAssignableFrom(type);
	}

	public Object cast(Object obj, Class<?> type) {
		return null;
	}
}
