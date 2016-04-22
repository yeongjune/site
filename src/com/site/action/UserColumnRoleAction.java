package com.site.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.service.UserColumnRoleService;

@Controller
@RequestMapping("userColumnRole")
public class UserColumnRoleAction {

	@Autowired
	private UserColumnRoleService service;

	@RequestMapping("dialog")
	public String dialog(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer userId){
		map.put("userId", userId);
		return "site/role/dialog";
	}
	@RequestMapping("userList")
	public void userList(HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		List<Map<String, Object>> mapList = service.getUserList(siteId);
		JSONUtil.printToHTML(response, mapList);
	}
	@RequestMapping("roleList")
	public void roleList(HttpServletRequest request, HttpServletResponse response, Integer userId){
		List<Map<String, Object>> mapList = service.getRoleList(userId);
		if(mapList!=null && mapList.size()>0){
			for (Map<String, Object> map : mapList) {
				map.put("open", true);
			}
		}
		JSONUtil.printToHTML(response, mapList);
	}
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer userId, Integer roleId, Integer checked){
		Integer siteId = User.getCurrentSiteId(request);
		if(siteId==null || userId==null || roleId==null || checked==null){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.saveOrUpdate(siteId, userId, roleId, checked);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
}
