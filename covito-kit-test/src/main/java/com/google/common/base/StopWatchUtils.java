package com.google.common.base;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class StopWatchUtils {

	@Test
	public void test() throws InterruptedException{
		Stopwatch sw=new Stopwatch();
		sw.start();
		Thread.sleep(100);
		sw.stop();
		System.out.println(sw.toString());;
		
		System.out.println(sw.elapsed(TimeUnit.MILLISECONDS));
	}
}
