package com.authority.dao;

import java.util.List;
import java.util.Map;

public interface SiteRoleDao {

	/**
	 * 查询系统角色及指定站点拥有的角色数据
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> getRoleList(Integer siteId);

	/**
	 * 保存或修改指定站点与系统角色的关系数据
	 * @param siteId
	 * @param roleId
	 * @param checked
	 * @return
	 */
	int saveOrUpdate(Integer siteId, Integer roleId, Integer checked);

	/**
	 * 删除指定站点所有角色关联数据
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);

	/**
	 * 删除所有网站的指定角色
	 * @param roleId
	 * @return
	 */
	int deleteByRoleId(Integer roleId);

}
