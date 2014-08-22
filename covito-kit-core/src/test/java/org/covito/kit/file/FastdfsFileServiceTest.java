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
package org.covito.kit.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.covito.kit.file.FileService;
import org.covito.kit.file.support.FastdfsFileServiceImpl;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-4]
 */
public class FastdfsFileServiceTest {

	private static FileService fileService;

	@BeforeClass
	public static void init() {
		fileService = new FastdfsFileServiceImpl();
	}

	@Test
//	@Ignore
	public void testUpload() throws FileNotFoundException {
		String path = fileService.upload(new FileInputStream("e:/Note.txt"),
				"Note.txt", null);
		System.out.println(path);
		System.out.println(fileService.getFileInfo(path));
		fileService.append(path, new FileInputStream("e:/Note.txt"));
		fileService.outputFile(path, new FileOutputStream("e:/Note1.txt"));
		Map<String, String> m = new HashMap<String, String>();
		m.put("small3*3", "p2323423");
		fileService.updataMeta(path, m);
		System.out.println(fileService.getFileInfo(path));
	}

	@Test
	@Ignore
	public void testDelete() throws FileNotFoundException {
		String path = "group1/M00/00/00/wKgPxFORGmOAKxDvAAAF2Bnv3po029.txt";
		fileService.append(path, new FileInputStream("e:/Desktop.txt"));
		fileService.outputFile(path, new FileOutputStream("e:/Note1.txt"));
		//fileService.deleteFile(path);
	}

}
