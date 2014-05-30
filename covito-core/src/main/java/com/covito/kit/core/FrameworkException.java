package com.covito.kit.core;

/**
 * 一句话功能简述<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-3-6]
 */
public class FrameworkException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public FrameworkException(String message) {
		super(message);
	}

	public FrameworkException(Throwable t) {
		super(t);
	}

	public FrameworkException(String message, Throwable cause) {
		super(message, cause);
	}
}
