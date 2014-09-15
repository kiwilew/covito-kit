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
package org.covito.kit.commons.time;

import org.junit.Test;

/**
 * 一句话功能简述
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年9月15日]
 */
public class StopWatchTest {

	@Test
	public void test() throws InterruptedException{
		StopWatch sw=new StopWatch();
		sw.start();
		Thread.sleep(100);
		sw.split("one");
		Thread.sleep(60);
		sw.split("two");
		sw.stop();
		System.out.println(sw.prettyPrint());
		
		System.out.println(sw.toSplitString());
		System.out.println(sw.toString());
	}
}
