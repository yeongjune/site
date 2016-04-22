package com.authority.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.service.MenuService;
import com.base.util.JSONUtil;

@Controller
@RequestMapping("menu")
public class MenuAction {

	@Autowired
	private MenuService service;

	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "authority/menu/index";
	}
	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response){
		List<Map<String, Object>> list = service.load();
		for (Map<String, Object> map : list) {
			if(map==null || map.isEmpty())continue;
			map.put("open", true);
		}
		JSONUtil.printToHTML(response, list);
	}
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, Integer pid, Integer sort){
		String name = request.getParameter("name");
		pid = pid==null?0:pid;
		sort = sort==null?0:sort;
		Serializable id = service.save(name, pid, sort);
		JSONUtil.print(response, id!=null?id:"fail");
	}
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id, Integer deleteChildren){
		int i = service.delete(id, deleteChildren);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id, Integer pid, Integer sort){
		String name = request.getParameter("name");
		int i = service.update(id, name, pid, sort);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}
	@RequestMapping("updateUrl")
	public void updateUrl(HttpServletRequest request, HttpServletResponse response, Integer id){
		String url = request.getParameter("url");
		int i = service.updateUrl(id, url);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}

}
