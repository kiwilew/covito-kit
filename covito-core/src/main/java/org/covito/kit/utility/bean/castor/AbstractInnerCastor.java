package org.covito.kit.utility.bean.castor;

public abstract class AbstractInnerCastor implements ICastor {
	public String getID() {
		return getClass().getName();
	}

	public String getName() {
		return getClass().getName();
	}
}
