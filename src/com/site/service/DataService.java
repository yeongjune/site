package com.site.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;

import com.site.model.Data;

public interface DataService {
	
	/**
	 * 加载数据源
	 * @param id
	 * @param siteId
	 * @return
	 */
	Map<String, Object> load(Integer id, Integer siteId);
	
	/**
	 * 保存数据源
	 * 
	 * @param data
	 * @return
	 */
	Integer save(Data data);
	
	/**
	 * 删除数据源
	 * @param id
	 * @param siteId
	 */
	void delete(Integer id, Integer siteId);
	
	/**
	 * 修改数据源
	 * @param data
	 */
	void update(Data data);
	
	/**
	 * 根据不同标签编号 查询对应标签结果集
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> listByLabel(Integer siteId, String labelId);
	
	/**
	 * 根据模板名称加载对应模板数据
	 * 
	 * @param modelMap			
	 * @param siteId			站点编号
	 * @param templateId		模板编号
	 * @param currentPage		当前页码
	 * @return
	 */
	Map<String, Object> getTemplateDate(ModelMap modelMap, Integer siteId, Integer templateId, Integer currentPage, Integer pageSize, Integer columnId, Integer articleId, Map<String, Object> paramMap);
	
	/**
	 * 根据模板名称加载对应模板数据
	 * 
	 * @param modelMap			
	 * @param siteId			站点编号
	 * @param templateId		模板编号
	 * @param currentPage		当前页码
	 * @return
	 */
	Map<String, Object> getTemplateDate(ModelMap modelMap, Integer siteId, Integer templateId, String dataName,Integer currentPage, Integer pageSize, Integer columnId, Integer articleId, Map<String, Object> paramMap);
	
	/**
	 * 根据站点ID 查询所有的数据源
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> getList(Integer siteId);
	
	/**
	 * 根据站点ID 和模板Id 查询
	 * @param siteId
	 * @param templateId
	 * @return
	 */
	List<Data> getList(Integer siteId, Integer templateId);
	
	
	/**
	 * 加载菜单
	 * @author lostself
	 * @param modelMap
	 * @param data
	 * @param siteId
	 */
	void processMenu(ModelMap modelMap, Data data, Integer siteId);
	
	/**
	 * 根据站点ID 和 名称 加载数据源
	 * @author lostself
	 * @param name
	 * @return
	 */
	Data getByNameAndSiteId(String name, Integer siteId);	
}
