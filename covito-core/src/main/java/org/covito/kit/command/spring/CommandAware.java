package org.covito.kit.command.spring;

import java.util.Map;

import org.covito.kit.command.Command;
import org.covito.kit.command.CommandManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class CommandAware implements ApplicationContextAware,InitializingBean,ApplicationListener<ContextRefreshedEvent>{

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		 Map<String, Command> map=applicationContext.getBeansOfType(Command.class);
		 for(Command cmd:map.values()){
			 CommandManager.addCommand(cmd);
		 }
	}

}
