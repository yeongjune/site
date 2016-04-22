package com.authority.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.service.MenuUrlService;
import com.base.util.JSONUtil;

@Controller
@RequestMapping("menuUrl")
public class MenuUrlAction {

	@Autowired
	private MenuUrlService service;

	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer menuId){
		List<Map<String, Object>> list = service.load(menuId);
		JSONUtil.printToHTML(response, list);
	}
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer menuId, Integer checked){
		String urlId = request.getParameter("urlId");
		int i = service.saveOrUpdate(menuId, urlId, checked);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}
}
