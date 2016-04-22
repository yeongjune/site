package com.site.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.util.JSONUtil;
import com.site.service.ArticleService;

@Controller
@RequestMapping("tools")
public class ToolsAction {
	
	@Autowired
	private ArticleService articleService;
	
	/**
	 * 前段幻灯片工具 juicebox 所需要的配置信息
	 */
	@RequestMapping("/getArticleImages")
	public void getArticleImages(Integer id, HttpServletResponse response){
		Map<String, Object> article = articleService.load(id);
		List<Map<String, Object>> images = (List<Map<String, Object>>) article.get("imageList");
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<juiceboxgallery>");
		for (Map<String, Object> map : images) {
			sb.append("<image imageURL=\""+ map.get("path") +"\"");
			sb.append(" thumbURL=\""+ map.get("path") +"\"");
			sb.append(" linkURL=\""+ map.get("path") +"\"");
			sb.append(" linkTarget=\"_blank\">");
			sb.append("<caption><![CDATA["+ ( map.get("description") == null? "" : map.get("description") ) +"]]></caption>");
			sb.append("</image>");
        }
		sb.append("</juiceboxgallery>");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/xml");
		response.setHeader("Accept-Ranges", "bytes");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(sb.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if( os != null ){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
