package com.baoidc.idcserver.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
@Component
public class GetBeanUtil implements ApplicationContextAware {

	private static ApplicationContext context = null;
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		GetBeanUtil.context = context;
	}
	
	public synchronized static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

}
