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
package test.fileservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.covito.kit.file.FileService;
import org.covito.kit.file.support.LocalFileServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-6]
 */
public class LocalTest {

	private static FileService fileService;

	@BeforeClass
	public static void init() {
		fileService = new LocalFileServiceImpl();
	}

	@Test
	public void testUplaod() throws Exception {
		String path = fileService.upload(new FileInputStream("e:/Note.txt"), "Note.txt", null);
		System.out.println(path);

		path = fileService.upload(new FileInputStream("e:/34.jpg"), "34.jpg", null);
		System.out.println(path);
	}
}
