package com.authority.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.service.UrlService;
import com.base.util.JSONUtil;

@Controller
@RequestMapping("url")
public class UrlAction {

	@Autowired
	private UrlService service;

	@RequestMapping("reload")
	public void initUrls(HttpServletRequest request, HttpServletResponse response) {
		JSONUtil.print(response, service.updateUrls(request.getSession().getServletContext()));
	}
}
