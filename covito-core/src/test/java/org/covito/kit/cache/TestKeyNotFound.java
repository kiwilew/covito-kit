package org.covito.kit.cache;

public class TestKeyNotFound implements KeyNotFoundHandler<String, String> {

	@Override
	public String onKeyNotFound(String key) {
		
		return "unkown";
	}

}
