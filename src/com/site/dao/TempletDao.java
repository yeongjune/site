package com.site.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface TempletDao {

	/**
	 * 查询指定站点指定模板名称的模板id
	 * @param name
	 * @param siteId
	 * @return
	 */
	Integer getTemplateId(String name, Integer siteId);

	/**
	 * 批量保存新增的模板
	 * @param mapList
	 * @return
	 */
	int save(List<Map<String, Object>> mapList);

	/**
	 * 分页查询指定站点的页面模板
	 * @param siteId
	 * @param currentPage
	 * @param pageSize
	 * @param keyword
	 * @return {currentPage, pageSize, totalPage, count, list=[{id, siteId, name}]}
	 */
	Map<String, Object> getListByPage(Integer siteId, Integer currentPage, Integer pageSize,
			String keyword);

	/**
	 * 保存一个新增的模板
	 * @param map {siteId, name}
	 * @return
	 */
	Serializable save(Map<String, Object> map);
	
	/**
	 * 删除一个模板
	 * @param id
	 * @return
	 */
	int delete(Integer id);

	/**
	 * 查询一个模板信息
	 * @param id
	 * @return {id, siteId, name}
	 */
	Map<String, Object> load(Integer id);

	/**
	 * 修改一个模板信息
	 * @param map {id, name}
	 * @return
	 */
	int update(Map<String, Object> map);

	/**
	 * 查询指定站点指定名字的模板个数
	 * @param siteId
	 * @param name
	 * @return
	 */
	long countByName(Integer siteId, String name);

	/**
	 * 查询指定站点指定名字的模板个数（排除本身）
	 * @param id
	 * @param name
	 * @return
	 */
	long countByNameWithSelf(Integer id, String name);

	/**
	 * 删除指定站点所有模板记录
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);

}
