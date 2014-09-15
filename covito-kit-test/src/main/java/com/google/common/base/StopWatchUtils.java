package com.google.common.base;

import org.junit.Test;

public class StopWatchUtils {

	@Test
	public void test(){
		Stopwatch sw=new Stopwatch();
		sw.start();
		
		sw.toString();
	}
}
