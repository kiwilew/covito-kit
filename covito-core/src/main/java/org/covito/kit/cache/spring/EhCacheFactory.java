package org.covito.kit.cache.spring;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.covito.kit.cache.CacheException;
import org.springframework.beans.factory.FactoryBean;

public class EhCacheFactory implements FactoryBean<Ehcache>{

	private CacheManager cacheManager;

	private String name;
	
	public EhCacheFactory(String name,CacheManager cacheManager) {
		this.name=name;
		this.cacheManager=cacheManager;
	}
	
	@Override
	public Ehcache getObject() throws Exception {
		if (cacheManager == null) {
			throw new CacheException("cacheManager is null!");
		}
		Cache sfca = cacheManager.getCache(name);
		if (sfca == null) {
			throw new CacheException("not found cache name is " + name + " cache!");
		}
		return sfca;
	}

	@Override
	public Class<?> getObjectType() {
		return Ehcache.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
