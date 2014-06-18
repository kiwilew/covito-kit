package org.covito.kit.cache;

import org.covito.kit.FrameworkException;

/**
 * 缓存异常类
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年6月17日]
 */
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
