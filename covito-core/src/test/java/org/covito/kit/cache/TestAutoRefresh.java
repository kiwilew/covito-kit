package org.covito.kit.cache;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class TestAutoRefresh implements AutoRefreshHandler<String, String> {

	@Override
	public long getAutoRefreshTime() {
		return 5000;
	}

	@Override
	public ConcurrentHashMap<String, String> refresh() {
		ConcurrentHashMap<String, String> m=new ConcurrentHashMap<String, String>();
		Random r=new Random();
		m.put("A", "AA"+r.nextInt(100));
		return m;
	}

	

}
