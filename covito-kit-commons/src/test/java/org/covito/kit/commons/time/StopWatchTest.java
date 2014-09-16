package org.covito.kit.commons.time;

import org.junit.Test;

public class StopWatchTest {

	@Test
	public void test() throws InterruptedException{
		
		StopWatch sw=new StopWatch();
		sw.start();
		Thread.sleep(100);
		sw.split("one");
		Thread.sleep(60);
		sw.split("two");
		Thread.sleep(30);
		sw.stop();
		System.out.println(sw.prettyPrint());
		
		System.out.println(sw.toSplitString());
		
		System.out.println(sw.getSnapshot(1));
		System.out.println(sw.getSnapshot("two"));
	}
}