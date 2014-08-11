package org.covito.kit.cache.spring;

import java.util.Map;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class CacheAware implements ApplicationContextAware, InitializingBean,
		ApplicationListener<ContextRefreshedEvent> {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	@SuppressWarnings("rawtypes")
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Map<String, Cache> map = applicationContext.getBeansOfType(Cache.class);
		for (Cache<?, ?> cache : map.values()) {
			try {
				CacheManager.addCache(cache);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
