package org.apache.commons.lang3;

import org.junit.Test;

public class SystemUtilsTest {

	@Test
	public void test(){
		System.out.println(SystemUtils.IS_JAVA_1_7);
		System.out.println(SystemUtils.IS_JAVA_1_6);
		System.out.println(SystemUtils.JAVA_CLASS_PATH);
		System.out.println(SystemUtils.getUserHome());
		System.out.println(SystemUtils.getJavaIoTmpDir());
		System.out.println(SystemUtils.getUserDir());
		System.out.println(SystemUtils.FILE_ENCODING);
		System.out.println(SystemUtils.JAVA_COMPILER);
		System.out.println(SystemUtils.JAVA_CLASS_VERSION);
		System.out.println(SystemUtils.JAVA_HOME);
		
		System.out.println(SystemUtils.JAVA_RUNTIME_NAME);
		
		System.out.println(SystemUtils.OS_NAME);
		
		System.out.println(SystemUtils.USER_COUNTRY);
		
	}
}
