package com.site.service;

import java.util.List;
import java.util.Map;

import com.site.model.Recommend;

public interface RecommendService {

	/**
	 * 查询所有的推荐位
	 * @param siteId
	 */
	List<Map<String, Object>> getAll(Integer siteId);
	
	/**
	 * 查询指定站点栏目的推荐位
	 * @param siteId
	 * @param columnId
	 */
	List<Map<String, Object>> getAll(Integer siteId, Integer columnId);
	
	/**
	 * 查询推荐位
	 * @param id
	 * @param siteId
	 * @return
	 */
	Recommend get(Integer id, Integer siteId);
	
	/**
	 * 保存推荐位
	 * @param recommend
	 * @return
	 */
	Integer save(Recommend recommend);
	
	/**
	 * 修改推荐位
	 * @param recommend
	 */
	void update(Recommend recommend);
	
	/**
	 * 删除推荐位
	 * @param id
	 * @param siteId
	 */
	void delete(Integer id, Integer siteId);
}
