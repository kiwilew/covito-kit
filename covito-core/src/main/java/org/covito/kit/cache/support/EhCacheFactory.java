package org.covito.kit.cache.support;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;

import org.springframework.util.Assert;

public class EhCacheFactory {

	private CacheManager cacheManager;

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void init() {
		Assert.notNull(this.cacheManager, "A backing EhCache CacheManager is required");
		Status status = this.cacheManager.getStatus();
		Assert.isTrue(
				Status.STATUS_ALIVE.equals(status),
				"An 'alive' EhCache CacheManager is required - current cache is "
						+ status.toString());
		String[] names = this.cacheManager.getCacheNames();
		int i = names.length;
		for (int j = 0; j < i; ++j) {
			String str = names[j];
			Ehcache ehcache=this.cacheManager.getEhcache(str);
			@SuppressWarnings("rawtypes")
			EhCacheWrp eh=new EhCacheWrp(ehcache);
			org.covito.kit.cache.CacheManager.addCache(eh);
		}
	}
}
