package com.base.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.authority.service.LoginService;
import com.authority.service.UserService;
import com.base.config.Init;
import com.base.util.JSONUtil;

@Controller
@RequestMapping("sys")
public class SystemAction {

	@Autowired
	private UserService service;
	@Autowired
	private LoginService loginService;

	@RequestMapping("index")
	public String index(){
		return "index";
	}
	@RequestMapping("indexBak")
	public String indexOld(){
		return "index_bak";
	}

	@RequestMapping("loadForMenu")
	public void loadForMenu(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> user = User.getCurrentUser(request);
		Integer siteId = (Integer) user.get("siteId");
		String account = (String) user.get("account");
		if(siteId>0){
			List<Map<String, Object>> menuList = loginService.loadForMenu((Integer)user.get("id"));
			user.put("menuList", menuList);
			Set<String> urlSet = loginService.getUrlSet((Integer)user.get("id"));
			user.put("urlSet", urlSet);
		}else{
			if(account.equals("admin")){
				List<Map<String, Object>> menuList = loginService.loadForMenu();
				user.put("menuList", menuList);
				Set<String> urlSet = loginService.getUrlSet();
				user.put("urlSet", urlSet);
			}else{
				List<Map<String, Object>> menuList = loginService.loadForMenu((Integer)user.get("id"));
				user.put("menuList", menuList);
				Set<String> urlSet = loginService.getUrlSet((Integer)user.get("id"));
				user.put("urlSet", urlSet);
			}
		}
		JSONUtil.printToHTML(response, user.get("menuList"));
	}
	@RequestMapping("login")
	public void login(HttpServletRequest request, HttpServletResponse response, Integer siteId){
		Map<String, Object> result = new HashMap<String, Object>();
		Integer currentSiteId = User.getCurrentSiteId(request);
		if(currentSiteId!=null && siteId!=null && siteId>0 && !currentSiteId.equals(siteId)){
			String account = "admin";
			String password = service.getAdminPassword(siteId);
			Map<String, Object> user = service.getUserByIdAndPassword(siteId, account, password);
			int i = User.login(request, user);
			if(i>0){
				result.put("status", Init.SUCCEED);
				result.put("message", "");
			}else{
				result.put("status", Init.FAIL);
				result.put("message", "帐号或密码错误");
			}
		}else{
			if(currentSiteId.equals(siteId)){
				result.put("status", Init.FAIL);
				result.put("message", "已经登录");
			}else{
				result.put("status", Init.FAIL);
				result.put("message", "未登录");
			}
		}
		JSONUtil.printToHTML(response, result);
	}

	@RequestMapping("resetPassword")
	public void resetPassword(HttpServletRequest request, HttpServletResponse response, Integer siteId){
		Integer currentSiteId = User.getCurrentSiteId(request);
		if(currentSiteId!=null && currentSiteId==0 && siteId!=null && siteId>0){
			int i = loginService.resetPassword(siteId);
			if(i>0){
				JSONUtil.print(response, "succeed");
			}else{
				JSONUtil.print(response, "fail");
			}
		}
	}
	@RequestMapping("editPassword")
	public String editPassword(HttpServletRequest request, HttpServletResponse response){
		return "editPassword";
	}
	@RequestMapping("updatePassword")
	public void updatePassword(HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		Integer userId = User.getCurrentUserId(request);
		String password = request.getParameter("password");
		String newPassword = request.getParameter("newPassword");
		if(siteId!=null){
			int i = loginService.updatePassword(siteId, userId, password, newPassword);
			if(i>0){
				Map<String, Object> user = User.getCurrentUser(request);
				user.put("password", newPassword);
				request.getSession().setAttribute(Init.AUTHORITY_USER, user);
				JSONUtil.print(response, "succeed");
			}else{
				JSONUtil.print(response, "fail");
			}
		}
	}
}
