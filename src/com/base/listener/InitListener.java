package com.base.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.authority.service.UrlService;
import com.authority.service.UserService;
import com.base.config.Init;
import com.base.util.ContextAware;
import com.site.service.ImageService;

/**
 * Application Lifecycle Listener implementation class InitListener
 * 
 */
public class InitListener implements ServletContextListener {


	/**
	 * 系统所有url
	 */
	public static List<String> urlList = new ArrayList<String>();



	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		Init.reload(event.getServletContext());
		
		// initialize system manager for "admin"
		UserService userService = ContextAware.getService(UserService.class);
		long count = userService.countByAccount(0, "admin");
		if(count<=0){
			userService.save("admin", "admin", 0);
		}

		UrlService urlService = ContextAware.getService(UrlService.class);
		urlService.updateUrls(event.getServletContext());
		
		String root = event.getServletContext().getRealPath("/");
		try {
			ImageService imageService= ContextAware.getService(ImageService.class);;
			//删除没用用到的新闻图片(site_article数据量12506时，执行这个操作需要50多秒所以启动的时候不执行这个方法)
			//imageService.deleteAllDisalbeFile(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
