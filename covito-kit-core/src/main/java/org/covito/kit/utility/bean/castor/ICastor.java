package org.covito.kit.utility.bean.castor;

public abstract interface ICastor {
	public abstract boolean canCast(Class<?> paramClass);

	public abstract Object cast(Object paramObject, Class<?> paramClass);
}
