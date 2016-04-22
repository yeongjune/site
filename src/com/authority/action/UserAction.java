package com.authority.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.authority.service.UserService;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.base.util.JSONUtil;

@Controller
@RequestMapping("user")
public class UserAction {

	@Autowired
	private UserService service;

	@RequestMapping("index")
	public String index(ModelMap map, HttpServletRequest request, HttpServletResponse response){
		return "site/user/index";
	}
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize){
		Integer siteId = User.getCurrentSiteId(request);
		currentPage = Init.getCurrentPage(currentPage);
		pageSize = Init.getPageSize(pageSize);
		String keyword = request.getParameter("keyword");
		Map<String, Object> pageListMap = service.getListByPage(siteId, currentPage, pageSize, keyword);
		JSONUtil.printToHTML(response, pageListMap);
	}
	@RequestMapping(value="add")
	public String add(ModelMap map, HttpServletRequest request, HttpServletResponse response){
		return "site/user/add";
	}
	@RequestMapping(value="save")
	public void save(HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		String account = request.getParameter("account");
		String name = request.getParameter("name");
		if(siteId==null || name==null || name.trim().equals("") || account==null || account.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			Serializable id = service.save(account, name, siteId);
			if(id!=null){
				JSONUtil.print(response, Init.SUCCEED);
			}else{
				JSONUtil.print(response, Init.FAIL);
			}
		}
	}
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
		if(id==null || id<=0){
			JSONUtil.print(response, Init.FAIL);
		}else{
			Integer siteId = User.getCurrentSiteId(request);
			int i = service.delete(id, siteId);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="edit")
	public String edit(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer id){
		map.put("id", id);
		return "site/user/edit";
	}
	@RequestMapping(value="load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> map = service.load(id);
		JSONUtil.printToHTML(response, map);
	}
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id){
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.update(id, name);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="updateEnable")
	public void updateEnable(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status){
		Integer siteId = User.getCurrentSiteId(request);
		if(siteId==null || id==null){
			JSONUtil.print(response, Init.FAIL);
		}else{
			status = status==null?0:status;
			int i = service.updateEnable(siteId, id, status);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="accountIsExist")
	public void accountIsExist(HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		String account = request.getParameter("account");
		if(account==null || account.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = service.countByAccount(siteId, account);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
	
	/**
	 * 站内管理员密码重置
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value="resetPwd")
	public void resetPwd(HttpServletRequest request,HttpServletResponse response, 
			Integer id){
		if(id != null){
			int result = service.resetPwd(id);
			JSONUtil.printToHTML(response, result);
		}
	}
}
