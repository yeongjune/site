package com.base.interceptor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.authority.model.User;
import com.authority.service.UserService;
import com.base.config.Init;

public class OperatorInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String actionName = request.getRequestURI().replace(request.getContextPath(), "");
		if(isExcludeUrl(request, actionName)){
			return true;
		}else{
			if(actionName!=null && isAuthorityUrl(actionName)){
				if(managerLogin(request, response)){
					if(managerAuthority(request, response)){
						return true;
					}else{
						return false;
					}
				}else{
					if(actionName.startsWith("/lottery")){
						response.getWriter().write("<script>window.location.href='/lottery/login/index.action';</script>");
					}else{
						response.getWriter().write("<script>window.location.href='/login/index.action';</script>");
					}
					return false;
				}
			}else{
				return false;
			}
		}
	}

	/**
	 * 判断是否后台管理链接
	 * @param actionName
	 * @return
	 */
	private boolean isAuthorityUrl(String actionName) {
		return true;
//		LinkedHashMap<String, Object> excludeUrlMap = Init.get("authorityUrl");
//		if(excludeUrlMap==null || excludeUrlMap.isEmpty())return false;
//		for (String key : excludeUrlMap.keySet()) {
//			@SuppressWarnings("unchecked")
//			LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) excludeUrlMap.get(key);
//			if(map!=null){
//				if(actionName.contains(map.get("value").toString()))return true;
//			}
//		}
//		return false;
	}

	/**
	 * 判断是否已经登录前台
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean memberLogin(HttpServletRequest request) {
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute("member");
		return user!=null && !user.isEmpty();
	}

	/**
	 * 判断是否已经登录后台
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean managerLogin(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute(Init.AUTHORITY_USER);
		if(user!=null && !user.isEmpty()){
			return true;
		}else{
			int i = 0;
			Map<String, Object> cookie = User.cookie(request);
			if(cookie!=null && !cookie.isEmpty()){
				UserService userService = com.base.util.ContextAware.getService(UserService.class);
				user = userService.getUserByIdAndPassword((Integer)cookie.get("organizationId"), (String)cookie.get("account"), (String)cookie.get("password"));
				i = User.login(request, user);
			}
			return i>0;
		}
	}
	/**
	 * 判断是否有访问权限
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean managerAuthority(HttpServletRequest request, HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute(Init.AUTHORITY_USER);
		if(user!=null && !user.isEmpty()){
			if(user.get("account").equals("admin"))return true;
			String actionName = request.getRequestURI().replace(request.getContextPath(), "");
			actionName = actionName.replaceAll("\\+", "/");
			actionName = actionName.replaceAll("/+", "/");
			actionName = actionName.substring(1, actionName.lastIndexOf("."));
			if(actionName.equals(Init.SYSTEM_INDEX_URL))return true;
			Object obj = request.getSession().getServletContext().getAttribute(Init.APPLICATION_URL_KEY);
			if(obj!=null && obj instanceof Map){
				@SuppressWarnings("unchecked")
				Map<Object, Map<String, Object>> urlsMap = (Map<Object, Map<String, Object>>) obj;
				if(urlsMap.get(actionName)!=null && urlsMap.get(actionName).get("common")!=null && urlsMap.get(actionName).get("common").equals(1))return true;
				@SuppressWarnings("unchecked")
				Set<String> urlSet = (Set<String>) user.get("urlSet");
				if(urlSet==null || urlSet.isEmpty())return false;
				if(urlSet.contains(actionName))return true;
			}else{
				User.logout(request, response);
			}
			return false;
		}
		return false;
	}

	/**
	 * 判断是否是开放的访问地址
	 * @param actionName
	 * @return
	 */
	private boolean isExcludeUrl(HttpServletRequest request, String actionName){
		if(actionName.contains("/authorityLogin/"))return true;
		if(actionName.contains("/login/"))return true;
		if("/".equals(actionName)) return true;
		LinkedHashMap<String, Object> excludeUrlMap = Init.get("excludeUrl");
		if(excludeUrlMap!=null && !excludeUrlMap.isEmpty()) {
			for (String key : excludeUrlMap.keySet()) {
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) excludeUrlMap.get(key);
				if(map!=null){
					if(actionName.contains(map.get("value").toString()))return true;
				}
			}
		}
		Object obj = request.getSession().getServletContext().getAttribute(Init.APPLICATION_URL_KEY);
		if(obj!=null && obj instanceof Map){
			@SuppressWarnings("unchecked")
			Map<Object, Map<String, Object>> urlsMap = (Map<Object, Map<String, Object>>) obj;
			String key = request.getRequestURI().replace(request.getContextPath(), "");
			key = key.replaceAll("\\+", "/");
			key = key.replaceAll("/+", "/");
			key = key.substring(1, key.lastIndexOf("."));
			if(urlsMap.get(key)!=null && urlsMap.get(key).get("isPublic")!=null) {
				if (urlsMap.get(key).get("isPublic").equals(1)) {
					return true;
				}
			}
		}
		return false;
	}
}
