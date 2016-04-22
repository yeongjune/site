package com.site.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.service.ColumnService;

@Controller
@RequestMapping("column")
public class ColumnAction {

	@Autowired
	private ColumnService service;

	@RequestMapping("index")
	public String index(ModelMap map, HttpServletRequest request, HttpServletResponse response){
		return "site/column/index";
	}
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		List<Map<String, Object>> mapList = service.getList(siteId);
		List<Map<String, Object>> sortMapList = new ArrayList<Map<String,Object>>();
		if(mapList!=null){
			for (Map<String, Object> map : mapList) {
				map.put("open", true);
				if(map.get("pid")==null || map.get("pid").equals(0)){
					sortMapList.addAll(sort(map, mapList));
				}
			}
		}
		JSONUtil.printToHTML(response, sortMapList);
	}
	private List<Map<String, Object>> sort(Map<String, Object> map,
			List<Map<String, Object>> mapList) {
		List<Map<String, Object>> sortMapList = new ArrayList<Map<String,Object>>();
		sortMapList.add(map);
		List<Map<String, Object>> allChildren = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : mapList) {
			if(map2.get("pid").equals(map.get("id"))){
				List<Map<String, Object>> children = sort(map2, mapList);
				if(children!=null && children.size()>0){
					allChildren.addAll(children);
				}
			}
		}
		if(allChildren!=null && allChildren.size()>0){
			List<Object> childrenIds = new ArrayList<Object>();
			for (Map<String,Object> child : allChildren) {
				if(child==null || child.isEmpty())continue;
				childrenIds.add(child.get("id"));
			}
			map.put("childrenIds", childrenIds);
			sortMapList.addAll(allChildren);
		}
		return sortMapList;
	}
	@RequestMapping("add")
	public String add(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer pid){
		Integer siteId = User.getCurrentSiteId(request);
		if(siteId!=null && siteId>0){
			pid = pid==null?0:pid;
			List<Map<String, Object>> PageSizesList = Init.getByMapList("pageSizes");
			map.put("siteId", siteId);
			map.put("pid", pid);
			map.put("pageSizesList", PageSizesList);
			return "site/column/add";
		}else{
			return "redirectIndex";
		}
	}
	@RequestMapping(value="save")
	public void save(HttpServletRequest request, HttpServletResponse response, Integer pid, Integer navigation){
		Integer siteId = User.getCurrentSiteId(request);
		pid = pid==null?0:pid;
		navigation = navigation==null?1:navigation;
		String type = request.getParameter("type");
		String name = request.getParameter("name");
		String alias = request.getParameter("alias");
		String description = request.getParameter("description");
		String className = request.getParameter("className");
		String url = request.getParameter("url");
		String pageSize = request.getParameter("pageSize");
		//if(url!=null)url=url.startsWith("http://")?url:"http://"+url;
		if(siteId==null || siteId<=0 || name==null || name.trim().equals("") || type==null || type.trim().equals("") || ((type.equals("新闻栏目") || type.equals("图片栏目")) && (pageSize==null || pageSize.trim().equals(""))) ){
			JSONUtil.print(response, Init.FAIL);
		}else{
			Serializable id = service.save(siteId, name, pid, alias, type, url, navigation, Integer.parseInt(pageSize), description, className);
			if(id!=null){
				JSONUtil.print(response, id);
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
			int i = service.delete(id);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping("edit")
	public String edit(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer id){
		List<Map<String, Object>> PageSizesLIst = Init.getByMapList("pageSizes");
		map.put("pageSizesList", PageSizesLIst);
		map.put("id", id);
		return "site/column/edit";
	}
	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> map = service.load(id);
		JSONUtil.printToHTML(response, map);
	}
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id, Integer navigation){
		String type = request.getParameter("type");
		String url = request.getParameter("url");
		String pageSize = request.getParameter("pageSize");
		//if(url!=null)url=url.startsWith("http://")?url:"http://"+url;
		if(type==null || type.trim().equals("") || ((type.equals("新闻栏目") || type.equals("图片栏目")) && (pageSize==null || pageSize.trim().equals("")))){
			JSONUtil.print(response, Init.FAIL);
		}else{
			String name = request.getParameter("name");
			String alias = request.getParameter("alias");
			String description = request.getParameter("description");
			String className = request.getParameter("className");
			if(name==null || name.trim().equals("")){
				JSONUtil.print(response, Init.FAIL);
			}else{
				int i = service.update(id, name, alias, type, url, navigation, Integer.parseInt(pageSize), description, className);
				JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
			}
		}
	}
	@RequestMapping(value="updateRelation")
	public void updateRelation(HttpServletRequest request, HttpServletResponse response, Integer id, Integer pid, Integer sort, Integer level){
		int i = service.updateRelation(id, pid, sort, level);
		JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
	}
}
