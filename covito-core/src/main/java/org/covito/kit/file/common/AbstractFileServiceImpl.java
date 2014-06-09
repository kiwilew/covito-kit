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
package org.covito.kit.file.common;

import org.covito.kit.file.FileNameGenerate;
import org.covito.kit.file.FileService;
import org.covito.kit.file.GroupGenerate;


/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-4]
 */
public abstract class AbstractFileServiceImpl implements FileService {

	protected final String separate="/";
	
	protected GroupGenerate groupGenerate=new DataGroupGenerate();

	protected FileNameGenerate fileNameGenerate=new UUIDFileNameGenerate();

	/**
	 * 获取分组名
	 * 
	 * @param path
	 * @return
	 */
	protected String getGroup(String path) {
		path = path.replace("\\", "/");
		return path.substring(0, path.indexOf(separate));
	}
	
	/**
	 * 获取文件名
	 * 
	 * @param path
	 * @return
	 */
	protected String getFileName(String path) {
		path = path.replace("\\", "/");
		return path.substring(path.indexOf(separate) + 1);
	}
	
	/** 
	 * 生成最终的文件名
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param fileName 原始文件名
	 * @return
	 */
	protected String generatePath(String fileName) {
		return groupGenerate.generate(fileName)+separate+fileNameGenerate.generate(fileName);
	}
	
	

	/**
	 * Set groupGenerate
	 *
	 * @param groupGenerate the groupGenerate to set
	 */
	public void setGroupGenerate(GroupGenerate groupGenerate) {
		this.groupGenerate = groupGenerate;
	}

	/**
	 * Set fileNameGenerate
	 *
	 * @param fileNameGenerate the fileNameGenerate to set
	 */
	public void setFileNameGenerate(FileNameGenerate fileNameGenerate) {
		this.fileNameGenerate = fileNameGenerate;
	}
}
