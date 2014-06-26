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
package org.covito.kit.utility;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.apache.commons.lang.ClassUtils;

/**
 * Class帮助类
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014年6月26日]
 */
public class ClassUtil extends ClassUtils{

	public static URL getClassLocation(final String clsName) {
		if (clsName == null) {
			return null;
		}
		Class cls = null;
		try {
			cls = Class.forName(clsName);
		} catch (Exception e) {
			return null;
		}
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		// java.lang.Class contract does not specify if 'pd' can ever be null;
		// it is not the case for Sun's implementations, but guard against null
		// just in case:
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			// 'cs' can be null depending on the classloader behavior:
			if (cs != null)
				result = cs.getLocation();
			if (result != null) {
				// Convert a code source location into a full class file
				// location
				// for some common cases:
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar")
								|| result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:".concat(result.toExternalForm()).concat("!/")
									.concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory()) {
							result = new URL(result, clsAsResource);
						}
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}
		if (result == null) {
			// Try to find 'cls' definition as a resource; this is not
			// document．d to be legal, but Sun's implementations seem to //allow
			// this:
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader
					.getSystemResource(clsAsResource);
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getClassLocation("org.apache.commons.io.EndianUtils"));
	}
}
