package com.covito.kit.web.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * 事件发布帮助类<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2013-10-17]
 */
public class EventPublisher implements ApplicationContextAware {
	
	private static Log log = LogFactory.getLog(EventPublisher.class);

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		EventPublisher.context=context;
	}
	
	public static void publishEvent(ApplicationEvent event){
		if(context==null){
			log.warn("Spring context not found ,Not used Spring or class[com.uisftech.tech.framework.aware.EventPublisher] not configured in spring xml!");
			return;
		}
		context.publishEvent(event);
	}
	
}
