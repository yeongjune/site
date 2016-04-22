package com.authority.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.service.AuthorityService;
import com.base.config.Init;
import com.base.util.JSONUtil;

@Controller
@RequestMapping("authority")
public class AuthorityAction {

	@Autowired
	private AuthorityService service;

	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "authority/authority/index";
	}
	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response){
		List<Map<String, Object>> list = service.load();
		for (Map<String, Object> map : list) {
			if(map==null || map.isEmpty())continue;
			if(map.get("common")!=null && map.get("common").equals(1)){
				map.put("checked", true);
			}
		}
		JSONUtil.printToHTML(response, list);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer checked){
		String id = request.getParameter("id");
		int i = service.update(id, checked);
		if(i>0){
			Map<String, Object> map = service.get(id);
			if(map==null || map.isEmpty())return ;
			Object obj = request.getSession().getServletContext().getAttribute(Init.APPLICATION_URL_KEY);
			Map<Object, Object> commonUrl = null;
			if(obj==null || !(obj instanceof Map)){
				commonUrl = new HashMap<Object, Object>();
			}else{
				commonUrl = (Map<Object, Object>) obj;
			}
			commonUrl.put(map.get("url"), map);
			request.getSession().getServletContext().setAttribute(Init.APPLICATION_URL_KEY, commonUrl);
		}
		JSONUtil.print(response, i>0?"succeed":"fail");
	}

}
