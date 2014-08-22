package com.covito.test;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class GuavaTest {

	@Test
	@Ignore
	public void simpleTest() {
		// arrange
		Iterator i = mock(Iterator.class);
		when(i.next()).thenReturn("Hello").thenReturn("World");
		// act
		String result = i.next() + " " + i.next();
		// verify
		verify(i, times(2)).next();
		// assert
		Assert.assertEquals("Hello World", result);
	}
}
