package org.apache.commons.lang3;

import org.junit.Test;

import static org.apache.commons.lang3.EnumUtils.*;

public class EnumUtilsTest {

	@Test
	public void test(){
		System.out.println(generateBitVector(Eo.class, Eo.a,Eo.b));
		System.out.println(getEnum(Eo.class, "a"));
		System.out.println(getEnumList(Eo.class));
		System.out.println(getEnumMap(Eo.class));
		System.out.println(isValidEnum(Eo.class, "d"));
		System.out.println(isValidEnum(Eo.class, "b"));
	}
	
	public static enum Eo{
		a("1"),b("2"),c("3");
		
		String value;
		private Eo(String v) {
			value=v;
		}
		@Override
		public String toString() {
			return value;
		}
	}
}
