package org.covito.kit.file;

import org.covito.kit.FrameworkException;

public class FileServiceException extends FrameworkException{

	private static final long serialVersionUID = 1L;

	public FileServiceException(String message) {
		super(message);
	}

	public FileServiceException(Throwable t) {
		super(t);
	}

	public FileServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
