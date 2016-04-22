package com.site.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.site.service.StatisticsService;

@Controller
@RequestMapping("statistics")
public class StatisticsAction {

	@Autowired
	private StatisticsService statisticsService;
	
	/**
	 * 更新或保存页面访问量数据
	 * @param siteId
	 * @throws IOException 
	 */
	@RequestMapping("saveOrUpdatePageView")
	public void saveOrUpdatePageView(HttpServletRequest request, HttpServletResponse response, 
			Integer siteId) throws IOException{
		if(siteId != null){
			statisticsService.saveOrUpdatePageView(siteId);
			response.getWriter().write("true");
		}
	}
	
	/**
	 * 查询网页访问量
	 * @param siteId
	 * @throws IOException 
	 */
	@RequestMapping("findPageView")
	public void findPageView(HttpServletRequest request, HttpServletResponse response, 
			Integer siteId) throws IOException{
		if(siteId != null){
			Integer pageView = statisticsService.findPageView(siteId);
			response.getWriter().print(pageView.intValue());
		}
	}
}
