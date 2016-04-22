package com.site.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.ueditor.MyActionEnter;
import com.base.util.JSONUtil;

/**
 * ueditor服务器后台交互请求数据的处理
 * 主要是上传
 * @author lfq
 *
 */
@Controller
@RequestMapping(value="ueditor")
public class UeditorAction {
	
	@RequestMapping("controller")
	public void controller(HttpServletResponse response, HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		//官方原版使用的时ActionEnter,MyActionEnter是结合项目修改后对ActionEnter作了图片上传后的处理修改
		JSONUtil.print(response, new MyActionEnter( request, rootPath ).exec());
	}
}
