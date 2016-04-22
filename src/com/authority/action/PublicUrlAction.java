package com.authority.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.service.PublicUrlService;
import com.base.util.JSONUtil;

@Controller("AuthorityPublicAction")
@RequestMapping("authorityPublicUrl")
public class PublicUrlAction {

	@Autowired
	private PublicUrlService service;

	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "authority/public/index";
	}
	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response){
		List<Map<String, Object>> list = service.load();
		for (Map<String, Object> map : list) {
			if(map==null || map.isEmpty())continue;
			if(map.get("isPublic")!=null && map.get("isPublic").equals(1)){
				map.put("checked", true);
			}
		}
		JSONUtil.printToHTML(response, list);
	}
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer checked){
		String id = request.getParameter("id");
		int i = service.update(request, id, checked);
		JSONUtil.print(response, i>0?"succeed":"fail");
	}

}
