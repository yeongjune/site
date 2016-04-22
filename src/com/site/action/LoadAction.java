package com.site.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.ColumnService;
import com.site.service.DataService;
import com.site.service.SiteService;
import com.site.service.TemplateService;

@Controller
@RequestMapping("load")
public class LoadAction {
	
	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private SiteService siteService;

	@Autowired
	private ColumnService columnService;
	/**
	 * 进入网站首页
	 * @param modelMap
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @param articleId
	 * @param columnId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("index")
	public String index(ModelMap modelMap, HttpServletRequest request, Integer currentPage, Integer pageSize, Integer articleId, Integer columnId){
		try {
			Integer siteId = siteService.getSiteId(request);
			if (siteId==null||siteId==0) {
				return "redirect:/login/index.action";
			}else{
				String page = "index";
				
				Integer templateId = templateService.getTemplateId(page, siteId);
				currentPage = currentPage == null ? 1 : currentPage;
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("currentPage", currentPage);
				paramMap.put("columnId", columnId);
				dataService.getTemplateDate( modelMap, siteId, templateId, currentPage, pageSize, columnId, articleId, paramMap);
				Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
				return "template/" + site.get("directory") + "/" + page ;
			}
		} catch (Exception e) {
			modelMap.put("error", e.getMessage()+"<br/>"+e.getLocalizedMessage());
			modelMap.put("statck", e.getStackTrace());
			e.printStackTrace();
			return "error";
		}
		
	}
	
	
	/**
	 * 进入网站列表页
	 * @param modelMap
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @param columnId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "list")
	public String list(ModelMap modelMap, HttpServletRequest request, Integer currentPage, Integer pageSize, Integer columnId,String keyword){
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
			dataService.getTemplateDate( modelMap, siteId, templateId, currentPage, pageSize, columnId, null, paramMap);
			Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
			return "template/" + site.get("directory") + "/list";
		} catch (Exception e) {
			modelMap.put("error", e.getMessage()+"<br/>"+e.getLocalizedMessage());
			modelMap.put("statck", e.getStackTrace());
		 	e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 进入网站列表页   返回json数据
	 * @param modelMap
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @param columnId
	 * @return
	 */

	@RequestMapping(value= "list", method= RequestMethod.POST)
	public void listPost(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response
			, Integer currentPage, Integer pageSize, Integer columnId,Integer dataId,String keyword){
		list(modelMap, request, currentPage, pageSize, columnId, keyword);
		JSONUtil.printToHTML(response, modelMap);
	}
	/**
	 * 进入文章内容页
	 * @param modelMap
	 * @param request
	 * @param articleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("article")
	public String article(ModelMap modelMap, HttpServletRequest request, Integer articleId){
		try{
			Integer siteId = siteService.getSiteId(request);
			Integer templateId = templateService.getTemplateId("article", siteId);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("articleId", articleId);
			dataService.getTemplateDate( modelMap, siteId, templateId, null, null, null, articleId, paramMap);
			Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
			return "template/" + site.get("directory") + "/article";
		} catch (Exception e) {
			modelMap.put("error", e.getMessage()+"<br/>"+e.getLocalizedMessage());
			modelMap.put("statck", e.getStackTrace());
			e.printStackTrace();
			return "error";
		}
	} 
	
	/**
	 * 进入其他
	 * @param modelMap
	 * @param request
	 * @param articleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("other")
	public String other(ModelMap modelMap, HttpServletRequest request, String page,Integer currentPage, 
			Integer pageSize,Integer columnId, Integer articleId,String keyword){
		// 将所带参数放进前段
		Map<String, String[]> map = request.getParameterMap();
		for (Entry<String, String[]> entrySet : map.entrySet()) {
			String[] values = entrySet.getValue();
			modelMap.put(entrySet.getKey(), values.length == 1 ? values[0] : values);
		}
		try{
			Integer siteId = siteService.getSiteId(request);
			Integer templateId = templateService.getTemplateId(page, siteId);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			if (!StringUtil.isEmpty(keyword)) {
				paramMap.put("keyword", keyword);
			}
			//dataService.getTemplateDate( modelMap, siteId, templateId, null, null, null, null, paramMap);
			dataService.getTemplateDate( modelMap, siteId, templateId, currentPage, pageSize, columnId, articleId, paramMap);
			Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
			return "template/" + site.get("directory") + "/"+page;
		} catch (Exception e) {
			modelMap.put("error", e.getMessage()+"<br/>"+e.getLocalizedMessage());
			modelMap.put("statck", e.getStackTrace());
			e.printStackTrace();
			return "error";
		}
	} 
	
	/**
	 * 单网页栏目处理
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("single")
	public String single(ModelMap modelMap, HttpServletRequest request, Integer columnId){
		try{
			Integer siteId = siteService.getSiteId(request);
			Integer templateId = templateService.getTemplateId("single", siteId);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			dataService.getTemplateDate(modelMap, siteId, templateId, null, null, columnId, null, paramMap);
			Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
			return "template/" + site.get("directory") + "/single";
		} catch (Exception e) {
			modelMap.put("error", e.getMessage()+"<br/>"+e.getLocalizedMessage());
			modelMap.put("statck", e.getStackTrace());
			e.printStackTrace();
			return "error";
		}
	}
	
	
}
