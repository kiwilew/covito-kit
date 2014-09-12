package org.apache.commons.lang3;

import org.junit.Test;

public class RangeTest {

	@Test
	public void test(){
		Range<String> r=Range.between("1", "9");
		System.out.println(r.contains("2"));
		System.out.println(r.getMaximum());
		System.out.println(r.getMinimum());
		System.out.println(r.isAfter("2"));
		System.out.println(r.isBefore("2"));
		System.out.println(r.isNaturalOrdering());
		System.out.println(r.isStartedBy("1"));
	}

}
