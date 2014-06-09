package org.covito.kit.cache;

import org.covito.kit.exception.FrameworkException;

public class CacheException extends FrameworkException{

	private static final long serialVersionUID = 1L;

	public CacheException(String message) {
		super(message);
	}

	public CacheException(Throwable t) {
		super(t);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
