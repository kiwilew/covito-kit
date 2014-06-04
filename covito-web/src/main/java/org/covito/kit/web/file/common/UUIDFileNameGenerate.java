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
package org.covito.kit.web.file.common;

import java.util.UUID;

import org.covito.kit.web.file.FileNameGenerate;

/**
 * 一句话功能简述
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014-6-4]
 */
public class UUIDFileNameGenerate implements FileNameGenerate {

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param fileName
	 * @return
	 */
	@Override
	public String generate(String fileName) {
		String file_ext_name="";
		if (fileName.contains(".")) {
			file_ext_name = fileName.substring(fileName.lastIndexOf("."));
		}
		
		return UUID.randomUUID().toString().replace("-", "")+file_ext_name;
	}
	
	public static void main(String[] args) {
		System.out.println(new UUIDFileNameGenerate().generate("333"));;
		System.out.println(new UUIDFileNameGenerate().generate("333.doc"));;
	}

}
