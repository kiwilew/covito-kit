package org.covito.kit.cache;

public class TestAutoSave implements AutoSaveHandler<String, String> {

	@Override
	public long getAutoSaveTime() {
		return 5000;
	}

	@Override
	public boolean save(String key, String value) {
		System.out.println("saveing key:"+key+" value:"+value);
		return true;
	}

}
