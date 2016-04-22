package com.authority.service;

import javax.servlet.ServletContext;

public interface UrlService {

	/**
	 * 初始化系统所有url,并更新缓存
	 * @param servletContext
	 * @return
	 */
	String updateUrls(ServletContext servletContext);

}
