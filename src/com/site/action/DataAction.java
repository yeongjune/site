package com.site.action;

import java.util.Arrays;
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
import com.base.util.StringUtil;
import com.site.model.Data;
import com.site.model.Label;
import com.site.model.Recommend;
import com.site.service.ColumnService;
import com.site.service.DataService;
import com.site.service.LabelService;
import com.site.service.RecommendService;

@Controller
@RequestMapping("data")
public class DataAction {
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private ColumnService columnService;
	
	@Autowired
	private RecommendService recommendService;
	
	@Autowired
	private LabelService labelService;
	
	/**
	 * 进入标签管理列表页
	 * @param modelMap
	 * @param request
	 * @param labelId
	 * @return
	 */
	@RequestMapping("index")
	public String index(ModelMap modelMap, HttpServletRequest request, String labelId){
		Integer siteId = User.getCurrentSiteId(request);
		if(labelId == null || "".equals(labelId)){
			labelId = Label.Item.List;
		}
		List<Map<String, Object>> list = dataService.listByLabel(siteId, labelId);
		List<Label> labelList = labelService.getListByState(0);
		modelMap.put("list", list);
		modelMap.put("labelId", labelId);
		modelMap.put("labelList", labelList);
		return "site/data/listBy" + labelId;
	}
	
	/**
	 * 添加数据标签
	 * @param modelMap
	 * @param requset
	 * @param labelId
	 * @return
	 */
	@RequestMapping("add")
	public String add(ModelMap modelMap, HttpServletRequest requset, String labelId){
		Integer siteId = User.getCurrentSiteId(requset);
		List<Map<String, Object>> columnList = columnService.getList(siteId);
		List<Map<String, Object>> recommendList = recommendService.getAll(siteId);
		modelMap.put("columnList", columnList);
		modelMap.put("recommendList", recommendList);
		modelMap.put("labelId", labelId);
		return "site/data/addBy" + labelId;
	}
	
	/**
	 * 修改数据标签
	 * @param modelMap
	 * @param requset
	 * @param labelId
	 * @return
	 */
	@RequestMapping("edit")
	public String edit(ModelMap modelMap, HttpServletRequest requset, String labelId, Integer id){
		Integer siteId = User.getCurrentSiteId(requset);
		Map<String, Object> map = dataService.load(id, siteId);
		//栏目
		List<Map<String, Object>> columnList = columnService.getList(siteId);
		if (map.containsKey("columnIds")&&!StringUtil.isEmpty(map.containsKey("columnIds")+"")) {
			String [] columnIds=(map.get("columnIds")+"").split(",");
			for (Map<String, Object> column : columnList) {
				if (Arrays.asList(columnIds).contains(column.get("id")+"")) {
					column.put("selected", "selected");
				}
			}
		}
		//查询对应推荐位
		List<Map<String, Object>> recommendList = recommendService.getAll(siteId);
		
		modelMap.put("recommendList", recommendList);
		modelMap.put("data", map);
		modelMap.put("columnList", columnList);
		return "site/data/editBy" + labelId;
	}
	
	/**
	 * 保存数据标签
	 * @param request
	 * @param response
	 * @param name			名称
	 * @param labelId		标签ID
	 * @param size			数量
	 * @param columnId		栏目ID
	 * @param recommendId	推荐位ID
	 * @param displayPage	是否显示分页
	 * @param sortType		排序规则
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, 
			String name, String labelId, Integer size, String[] columnId, Integer recommendId, Integer displayPage, 
			Integer sortType, Integer displayContentSize,Integer isJsonData,Integer everyColumn, Integer isImages){
		
		Integer siteId = User.getCurrentSiteId(request);
		Data data = new Data();
		data.setSiteId(siteId);
		data.setName(name);
		if(columnId != null){
			String columnIds = StringUtil.replaceCollectionToString(columnId);
			data.setColumnIds(columnIds);
		}
		data.setDisplayPage(displayPage);
		data.setLabelId(labelId);
		data.setRecommendId(recommendId);
		data.setSize(size);
		data.setIsJsonData(isJsonData);
		data.setSortType(sortType);
		data.setDisplayContentSize(displayContentSize);
		data.setEveryColumn(everyColumn);
		data.setIsImages((isImages == null || isImages != 1) ? 0 : 1);
		Integer id = dataService.save(data);
		if(id > 0){
			JSONUtil.print(response, Init.SUCCEED);
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id, String name
			, String labelId, Integer size, String[] columnId, Integer recommendId, Integer displayPage
			, Integer sortType, Integer displayContentSize,Integer isJsonData,Integer everyColumn, Integer isImages){
		Integer siteId = User.getCurrentSiteId(request);
		Data data = new Data();
		data.setId(id);
		data.setSiteId(siteId);
		data.setName(name);
		if(columnId != null){
			String columnIds = StringUtil.replaceCollectionToString(columnId);
			data.setColumnIds(columnIds);
		}
		data.setDisplayPage(displayPage);
		data.setLabelId(labelId);
		data.setRecommendId(recommendId);
		data.setSize(size);
		data.setIsJsonData(isJsonData);
		data.setSortType(sortType);
		data.setDisplayContentSize(displayContentSize);
		data.setEveryColumn(everyColumn);
		data.setIsImages((isImages == null || isImages != 1) ? 0 : 1);
		dataService.update(data);
		JSONUtil.print(response, Init.SUCCEED);
	}
	
	
	/**
	 * 删除数据源
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
		Integer siteId = User.getCurrentSiteId(request);
		if(id != null && id > 0 && siteId > 0){
			dataService.delete(id, siteId);
			JSONUtil.print(response, Init.SUCCEED);
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	
	
	
	
}
