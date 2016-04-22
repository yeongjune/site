package com.authority.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.service.SiteRoleService;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.service.SiteService;

@Controller
@RequestMapping("siteRole")
public class SiteRoleAction {

	@Autowired
	private SiteRoleService service;
	@Autowired
	private SiteService siteService;

	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "authority/site/index";
	}
	@RequestMapping("dialog")
	public String dialog(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer siteId){
		map.put("siteId", siteId);
		return "authority/site/dialog";
	}
	@RequestMapping("siteList")
	public void siteList(HttpServletRequest request, HttpServletResponse response){
		List<Map<String, Object>> mapList = siteService.getList();
		JSONUtil.printToHTML(response, mapList);
	}
	@RequestMapping("roleList")
	public void roleList(HttpServletRequest request, HttpServletResponse response, Integer siteId){
		List<Map<String, Object>> mapList = service.getRoleList(siteId);
		if(mapList!=null && mapList.size()>0){
			for (Map<String, Object> map : mapList) {
				map.put("open", true);
			}
		}
		JSONUtil.printToHTML(response, mapList);
	}
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer siteId, Integer roleId, Integer checked){
		if(siteId==null || roleId==null || checked==null){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.saveOrUpdate(siteId, roleId, checked);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
}
