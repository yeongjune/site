package com.authority.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface RoleDao {

	/**
	 * 加载所有角色
	 * @param siteId 
	 * @return [{id, name, sort, pid}]
	 */
	List<Map<String, Object>> load(Integer siteId);

	/**
	 * 保存一个新增的角色
	 * @param map {name, sort, pid}
	 * @return
	 */
	Serializable save(Map<String, Object> map);

	/**
	 * 删除一个角色及其所有子角色，同时会删除角色相关的菜单数据
	 * @param id
	 * @param siteId 
	 * @return
	 */
	int delete(Integer id, Integer siteId);

	/**
	 * 修改一个教师数据
	 * @param map
	 * @return
	 */
	int update(Map<String, Object> map);

	/**
	 * 删除指定站点下所有角色
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);

	/**
	 * 查询指定站点指定角色的数量，用于判断指定角色是否是该站点所创建
	 * @param roleId
	 * @param siteId
	 * @return
	 */
	long countByIdAndSiteId(Integer roleId, Integer siteId);

}
