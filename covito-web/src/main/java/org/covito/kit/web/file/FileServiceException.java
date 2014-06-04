package org.covito.kit.web.file;

public class FileServiceException extends RuntimeException{

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
