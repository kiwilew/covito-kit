/*
 * Copyright 2010-2014  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */
package org.covito.kit.exception;

import org.covito.kit.FrameworkException;


/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-4]
 */
public class FileNotFoundException extends FrameworkException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -687146617137065790L;

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public FileNotFoundException(String message) {
		super(message);
	}

	/** 
	 * Constructor
	 * @param t
	 */
	public FileNotFoundException(Throwable t) {
		super(t);
	}

	/** 
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public FileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
