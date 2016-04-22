package com.site.dao;

import java.util.List;
import java.util.Map;

import com.site.model.Recommend;

public interface RecommendDao {

	/**
	 * 查询站点下所有推荐位
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> list(Integer siteId);
	
	/**
	 * 查询站点某栏目下所有推荐位
	 * @param siteId
	 * @param columnId
	 * @return
	 */
	List<Map<String, Object>> list(Integer siteId, Integer columnId);
	
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

	/**
	 * 删除指定站点所有推荐位
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);
	
}
