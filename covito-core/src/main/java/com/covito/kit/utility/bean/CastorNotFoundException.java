package com.covito.kit.utility.bean;

import com.covito.kit.core.FrameworkException;

public class CastorNotFoundException extends FrameworkException {
	private static final long serialVersionUID = 1L;

	public CastorNotFoundException(String message) {
		super(message);
	}

	public CastorNotFoundException(Exception e) {
		super(e);
	}
}

