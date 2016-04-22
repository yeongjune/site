package com.site.service;

import java.io.Serializable;
import java.util.Map;

public interface TemplateService {

	/**
	 * 查询指定站点指定名称的模板id
	 * @param name
	 * @param siteId
	 * @return
	 */
	Integer getTemplateId(String name, Integer siteId);

	/**
	 * 分页查询指定站点的模板数据
	 * @param siteId
	 * @param currentPage
	 * @param pageSize
	 * @param keyword
	 * @return {currentPage, pageSize, totalPage, count, list=[{id, siteId, name}]}
	 */
	Map<String, Object> getListByPage(Integer siteId, Integer currentPage, Integer pageSize,
			String keyword);

	/**
	 * 保存一个新增的模板数据
	 * @param name
	 * @param siteId
	 * @return
	 */
	Serializable save(String name, Integer siteId);

	/**
	 * 删除一个模板数据
	 * @param id
	 * @return
	 */
	int delete(Integer id);

	/**
	 * 查询一个模板数据
	 * @param id
	 * @return {id, siteId, name}
	 */
	Map<String, Object> load(Integer id);

	/**
	 * 修改一个模板数据
	 * @param id
	 * @param name
	 * @return
	 */
	int update(Integer id, String name);

	/**
	 * 查询指定站点指定名称的模板数量
	 * @param siteId
	 * @param name
	 * @return
	 */
	long countByName(Integer siteId, String name);

	/**
	 * 查询指定站点指定名称的模板数量（除本身）
	 * @param id
	 * @param name
	 * @return
	 */
	long countByNameWithSelf(Integer id, String name);

}
