package com.base.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.base.dao.SQLDao;

public class ContextAware implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContest)
			throws BeansException {
		context = applicationContest;
	}

	public static <T> T getService(Class<T> c){
		return context.getBean(c);
	}

	public static SQLDao getSQLDao(){
		return context.getBean(SQLDao.class);
	}

	public static ApplicationContext getContext() {
		return context;
	}

}
