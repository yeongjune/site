package com.site.action;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.base.util.JSONUtil;
import com.site.service.ArticleService;
import com.site.service.ColumnService;

@Controller
@RequestMapping("commom")
public class commomAction {

	@Autowired
	private ColumnService service;
	
	@Autowired
	private ArticleService articleService;

	@RequestMapping("currentPhoto")
	public void currentPhoto(HttpServletRequest request, HttpServletResponse response, Integer columnId,Integer id){
		Map<String, Object> map1 = service.load(columnId);
		Map<String, Object> map = articleService.load(id);
		JSONUtil.printToHTML(response, map);
	}
	
	@RequestMapping("prePhoto")
	public void prePhoto(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> map = service.load(id);
		JSONUtil.printToHTML(response, map);
	}
	@RequestMapping("nextPhoto")
	public void nextPhoto(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> map = service.load(id);
		JSONUtil.printToHTML(response, map);
	}
}
