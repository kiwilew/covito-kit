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
package org.covito.kit.web.file.fastdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-4]
 */
public class Upload implements UploadCallback {

	private InputStream inputStream; // input stream for reading

	/**
	 * Constructor
	 */
	public Upload(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param out
	 * @return
	 * @throws IOException
	 */
	@Override
	public int send(OutputStream out) throws IOException {
		int len = 0;
		byte[] b = new byte[4096];
		while ((len = inputStream.read(b)) != -1) {
			out.write(b, 0, len);
		}
		return 0;
	}

}
