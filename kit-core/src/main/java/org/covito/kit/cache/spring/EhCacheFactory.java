package org.covito.kit.cache.spring;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

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
			cacheManager=new CacheManager();
		}
		Cache sfca = cacheManager.getCache(name);
		if (sfca == null) {
			cacheManager.addCache(name);
			sfca=cacheManager.getCache(name);
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
