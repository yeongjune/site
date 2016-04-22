package com.authority.service.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.AuthorityUrl;
import com.authority.model.Menu;
import com.authority.service.UrlService;
import com.base.config.Init;
import com.base.dao.SQLDao;
import com.base.util.ContextAware;
import org.springframework.web.bind.annotation.RequestMethod;

@Service
public class UrlServiceImpl implements UrlService {

	@Autowired
	private SQLDao dao;

	@Override
	public String updateUrls(ServletContext application) {
		ApplicationContext context = ContextAware.getContext();
		Map<String, Object> controllers = context.getBeansWithAnnotation(Controller.class);
		if(controllers==null || controllers.isEmpty()){
			
		}else{
			List<Map<String, Object>> urlList = new ArrayList<Map<String,Object>>();
			for (String key : controllers.keySet()) {
				Object controller = controllers.get(key);
				RequestMapping classAnno = controller.getClass().getAnnotation(RequestMapping.class);
				if(classAnno==null)continue;
				String[] classMappings = classAnno.value();
				if(classMappings!=null && classMappings.length>0){
					Method[] methods = controller.getClass().getMethods();
					if(methods!=null && methods.length>0){
						List<String> methodMappingList = new ArrayList<String>();
						for (Method method : methods) {
							RequestMapping methodAnno = method.getAnnotation(RequestMapping.class);
							if(methodAnno==null)continue;
                            RequestMethod[] requestMethods = methodAnno.method();
                            if(requestMethods != null && requestMethods.length > 0
                                    && requestMethods[0].equals(RequestMethod.POST))
                                continue;
                            String[] methodMappings = methodAnno.value();
							if(methodMappings!=null && methodMappings.length>0){
								for (String methodMapping : methodMappings) {
									methodMappingList.add(methodMapping);
								}
							}
						}
						for (String classMapping : classMappings) {
							classMapping = classMapping.replaceAll("^/+", "");
							classMapping = classMapping.replaceAll("/+$", "");
							classMapping = classMapping.replaceAll("^\\+", "");
							classMapping = classMapping.replaceAll("\\+$", "");
							Map<String, Object> classUrl = new HashMap<String, Object>();
							classUrl.put("id", classMapping);
							classUrl.put("url", classMapping);
							urlList.add(classUrl);
							for (String methodMapping : methodMappingList) {
								methodMapping = methodMapping.replaceAll("^/+", "");
								methodMapping = methodMapping.replaceAll("/+$", "");
								methodMapping = methodMapping.replaceAll("^\\+", "");
								methodMapping = methodMapping.replaceAll("\\+$", "");
								Map<String, Object> methodUrl = new HashMap<String, Object>();
								methodUrl.put("id", classMapping+"_"+methodMapping);
								methodUrl.put("url", classMapping+"/"+methodMapping);
								methodUrl.put("pid", classMapping);
								urlList.add(methodUrl);
							}
						}
					}
				}
			}
			List<Map<String, Object>> mapList = dao.queryForList("select * from "+AuthorityUrl.tableName);
			Map<Object, Object> commonMap = new HashMap<Object, Object>();
			if(mapList!=null && mapList.size()>0){
				for (Map<String, Object> map : mapList) {
					commonMap.put(map.get("id"), map.get("common"));
				}
			}
			for (Map<String, Object> map : urlList) {
				map.put("common", commonMap.get(map.get("id")));
			}
			dao.update("TRUNCATE TABLE "+AuthorityUrl.tableName);
			dao.save(AuthorityUrl.tableName, urlList);
			
			long menuCount = dao.queryForLong("select count(id) from "+Menu.tableName);
			if(menuCount<=0){
				Map<String, Object> systemManage = new HashMap<String, Object>();
				systemManage.put("name", "系统管理");
				systemManage.put("sort", 0);
				systemManage.put("pid", 0);
				Serializable id = dao.save(Menu.tableName, systemManage);
				if(id!=null){
					Map<String, Object> menuManage = new HashMap<String, Object>();
					menuManage.put("name", "菜单管理");
					menuManage.put("sort", 0);
					menuManage.put("url", "menu/index");
					menuManage.put("pid", id);
					dao.save(Menu.tableName, menuManage);
				}
			}
		}
		
		List<Map<String, Object>> urlList = dao.queryForList("select * from "+AuthorityUrl.tableName);
		if(urlList!=null && urlList.size()>0){
			Map<Object, Object> urlMap = new HashMap<Object, Object>();
			for (Map<String, Object> map : urlList) {
				if(map==null || map.isEmpty())continue;
				urlMap.put(map.get("url"), map);
			}
			application.setAttribute(Init.APPLICATION_URL_KEY, urlMap);
		}
		return Init.SUCCEED;
	}

}
