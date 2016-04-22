package com.site.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.model.Data;
import com.site.service.DataService;
import com.site.service.TemplateService;

@Controller
@RequestMapping("templet")
public class TempletAction {

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private DataService dataService;

	@RequestMapping("index")
	public String index(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response, Integer siteId){
		siteId = siteId==null?User.getCurrentSiteId(request):siteId;
		List<Map<String, Object>> dataList = dataService.getList(siteId);
		modelMap.put("dataList", dataList);
		modelMap.put("siteId", siteId);
		return "site/templet/index";
	}
	
	@RequestMapping("selectData")
	public String selectData(ModelMap modelMap, HttpServletRequest request, Integer id, Integer siteId){
		siteId = siteId==null?User.getCurrentSiteId(request):siteId;
		List<Map<String, Object>> dataListMap = dataService.getList(siteId);
		List<Data> dataList = dataService.getList(siteId, id);
		for(Map<String, Object> map : dataListMap){
			Integer dataId = (Integer) map.get("id");
			for(Data data : dataList){
				if(data.getId().intValue() == dataId.intValue()){
					map.put("check", true);
					break;
				} 
			}
		}
		modelMap.put("dataList", dataListMap);
		modelMap.put("templateId", id);
		return "site/templet/select";
	}
	
	@RequestMapping(value="add")
	public String add(ModelMap map, HttpServletRequest request, HttpServletResponse response){
		return "site/templet/add";
	}
	
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
		if(id==null || id<=0){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = templateService.delete(id);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	
	
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Integer siteId, Integer currentPage, Integer pageSize){
		siteId = siteId==null?User.getCurrentSiteId(request):siteId;
		currentPage = Init.getCurrentPage(currentPage);
		pageSize = Init.getPageSize(pageSize);
		String keyword = request.getParameter("keyword");
		Map<String, Object> pageListMap = templateService.getListByPage(siteId, currentPage, pageSize, keyword);
		JSONUtil.printToHTML(response, pageListMap);
	}
	
	@RequestMapping(value="save")
	public void save(HttpServletRequest request, HttpServletResponse response, Integer siteId){
		siteId = siteId==null?User.getCurrentSiteId(request):siteId;
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			Serializable id = templateService.save(name, siteId);
			if(id!=null){
				JSONUtil.print(response, Init.SUCCEED);
			}else{
				JSONUtil.print(response, Init.FAIL);
			}
		}
	}
	
	@RequestMapping(value="edit")
	public String edit(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer id){
		map.put("id", id);
		return "site/templet/edit";
	}
	@RequestMapping(value="load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> map = templateService.load(id);
		JSONUtil.printToHTML(response, map);
	}
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id){
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = templateService.update(id, name);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="nameIsExistWithSelf")
	public void nameIsExistWithSelf(HttpServletRequest request, HttpServletResponse response, Integer id){
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = templateService.countByNameWithSelf(id, name);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
	@RequestMapping(value="nameIsExist")
	public void nameIsExist(HttpServletRequest request, HttpServletResponse response, Integer siteId){
		siteId = siteId==null?User.getCurrentSiteId(request):siteId;
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = templateService.countByName(siteId, name);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
}
