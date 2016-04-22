package com.site.dao;

import java.util.List;
import java.util.Map;

import com.site.model.Data;

public interface DataDao {
	
	/**
	 * 加载数据源
	 * @param id
	 * @param siteId
	 * @return
	 */
	Map<String, Object> load(Integer id, Integer siteId);

	/**
	 * 保存数据源
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
	 * 查询新闻标签 相关 数据源
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> listByList(Integer siteId);
	
	/**
	 * 查询栏目标签 相关 数据源
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> listByColumn(Integer siteId);
	
	/**
	 * 查询推荐位标签 相关 数据源
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> listByRecommend(Integer siteId);

	
	/**
	 * 查询其他标签 相关数据源 
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> listByOther(Integer siteId, String labelId);
	
	/**
	 * 删除指定站点所有数据源
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);
	
	/**
	 * 根据站点ID 查询所有的数据源
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> getList(Integer siteId);

	
	/**
	 * 根据模板ID 和 站点ID 查询该模板 所有数据源
	 * 
	 * @param templateId
	 * @param siteId
	 * @return
	 */
	List<Data> getList(Integer templateId, Integer siteId);
	/**
	 * 根据站点ID 和 名称 加载数据源
	 * @author lostself
	 * @param name
	 * @return
	 */
	Data getByNameAndSiteId(String name, Integer siteId);
}
