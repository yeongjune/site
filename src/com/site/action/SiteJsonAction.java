package com.site.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.ColumnService;
import com.site.service.DataService;
import com.site.service.SiteService;
import com.site.service.TemplateService;

@Controller
@RequestMapping("json")
public class SiteJsonAction {
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private SiteService siteService;

	@Autowired
	private ColumnService columnService;

	@RequestMapping(value= "list")
	public void list(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response
			, Integer currentPage, Integer pageSize, Integer columnId,Integer dataId,String keyword,String dataName){
		try{
			Integer siteId = siteService.getSiteId(request);
			Integer templateId = templateService.getTemplateId("list", siteId);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			if (!StringUtil.isEmpty(keyword)) {
				paramMap.put("keyword", keyword);
			}
			Map<String, Object> columnMap = columnService.load(columnId);
			//分页数据
			currentPage = currentPage == null ? 1 : currentPage;
			pageSize = pageSize == null ? (Integer)columnMap.get("pageSize") : pageSize;
			paramMap.put("currentPage", currentPage);
			paramMap.put("columnId", columnId);
			modelMap.put("column", columnMap);
			dataService.getTemplateDate( modelMap, siteId, templateId,dataName,currentPage,pageSize, columnId, null, paramMap);
		} catch (Exception e) {
			modelMap.put("error", e.getMessage()+"<br/>"+e.getLocalizedMessage());
			modelMap.put("statck", e.getStackTrace());
		 	e.printStackTrace();
		}
		JSONUtil.printToHTML(response, modelMap);
	}
}
