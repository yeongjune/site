package com.site.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.util.JSONUtil;
import com.site.service.ColumnRoleService;

@Controller
@RequestMapping("columnRole")
public class ColumnRoleAction {

	@Autowired
	private ColumnRoleService service;

	@RequestMapping("index")
	public String index(ModelMap map, HttpServletRequest request, HttpServletResponse response){
		return "site/role/index";
	}
	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		List<Map<String, Object>> list = service.load(siteId);
		for (Map<String, Object> map : list) {
			if(map==null || map.isEmpty())continue;
			map.put("open", true);
		}
		JSONUtil.printToHTML(response, list);
	}
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, Integer pid, Integer sort){
		Integer siteId = User.getCurrentSiteId(request);
		String name = request.getParameter("name");
		pid = pid==null?0:pid;
		sort = sort==null?0:sort;
		Serializable id = service.save(siteId, name, pid, sort);
		JSONUtil.print(response, id!=null?id:"fail");
	}
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
		Integer siteId = User.getCurrentSiteId(request);
		int i = service.delete(id, siteId);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id, Integer pid, Integer sort){
		String name = request.getParameter("name");
		int i = service.update(id, name, pid, sort);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}
}
