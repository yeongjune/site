package com.site.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.DataUtil;
import com.base.util.JSONUtil;
import com.site.model.Recommend;
import com.site.service.ColumnService;
import com.site.service.RecommendService;

@Controller
@RequestMapping("recommend")
public class RecommendAction {
	
	@Autowired
	private RecommendService recommendService;
	
	@Autowired
	private ColumnService columnService;
	
	/**
	 * 查询推荐位列表
	 * @param modelMap
	 * @param session
	 * @param request	
	 * @param response
	 * @return
	 */
	@RequestMapping("index")
	public String index(ModelMap modelMap, HttpSession session, HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		List<Map<String, Object>> recommendList = recommendService.getAll(siteId);
		modelMap.put("recommendList", recommendList);
		return "site/recommend/index";
	}

	/**
	 * 进入添加页面 
	 * @return
	 */
	@RequestMapping("add")
	public String add(ModelMap modelMap,HttpServletRequest request){
		Integer siteId = User.getCurrentSiteId(request);
		
		List<Map<String, Object>> columnList =  columnService.getList(siteId);
		List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>();
		DataUtil.getSortBypid(columnList, 0, resultListMap);
		modelMap.put("columnList", resultListMap);
		
		return "site/recommend/add";
	}
	
	
	/**
	 * 进入修改页面
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping("edit")
	public String edit(ModelMap modelMap, HttpServletRequest request, Integer id){
		Integer siteId = User.getCurrentSiteId(request);
		
		List<Map<String, Object>> columnList =  columnService.getList(siteId);
		List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>();
		DataUtil.getSortBypid(columnList, 0, resultListMap);
		
		Recommend recommend = recommendService.get(id, siteId);
		modelMap.put("columnList", resultListMap);
		modelMap.put("recommend", recommend);
		return "site/recommend/edit";
	}
	
	/**
	 * 保存推荐位
	 * @param modelMap
	 * @param session
	 * @param request
	 * @param columnId
	 * @return
	 */
	@RequestMapping("save")
	public void save(HttpSession session, HttpServletRequest request, HttpServletResponse response, 
			 String name){
		if(name != null && !"".equals(name.trim())){
			Integer siteId = User.getCurrentSiteId(request);
			Recommend recommend = new Recommend();
			recommend.setName(name);
			//recommend.setColumnId(columnId);
			recommend.setSiteId(siteId);
			recommendService.save(recommend);
			JSONUtil.print(response, Init.SUCCEED);
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	/**
	 * 修改推荐位
	 * @param modelMap
	 * @param session
	 * @param request
	 * @param id
	 * @param columnId
	 * @return
	 */
	@RequestMapping("update")
	public void update(HttpSession session, HttpServletRequest request, HttpServletResponse response, 
			Integer id, String name){
		if(name != null && !"".equals(name.trim()) && id != null){
			Integer siteId = User.getCurrentSiteId(request);
			Recommend recommend = new Recommend();
			recommend.setSiteId(siteId);
			recommend.setId(id);
			recommend.setName(name);
			//recommend.setColumnId(columnId);
			recommendService.update(recommend);
			JSONUtil.print(response, Init.SUCCEED);
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	/**
	 * 删除推荐位
	 * @param request
	 * @param response	
	 * @param id
	 */
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
		Integer siteId = User.getCurrentSiteId(request);
		recommendService.delete(id, siteId);
		JSONUtil.print(response, Init.SUCCEED);
	}
	
	/**
	 * 根据 siteId 和 columnId 读取推荐位
	 * @param columnId
	 */
	@RequestMapping("findRecommend")
	public void findRecommend(HttpServletRequest request, HttpServletResponse response){
		Integer siteId = User.getCurrentSiteId(request);
		List<Map<String, Object>> recommendList = recommendService.getAll(siteId);
		JSONUtil.printToHTML(response, recommendList);
	}
}
