package com.site.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.util.JSONUtil;
import com.site.service.RoleColumnService;

@Controller
@RequestMapping("roleColumn")
public class RoleColumnAction {

	@Autowired
	private RoleColumnService service;

	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer roleId){
		List<Map<String, Object>> list = service.load(roleId);
		JSONUtil.printToHTML(response, list);
	}
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer roleId, Integer columnId, Integer checked){
		Integer siteId = User.getCurrentSiteId(request);
		int i = service.saveOrUpdate(roleId, columnId, checked, siteId);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}
}
