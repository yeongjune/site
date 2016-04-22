package com.authority.service;

import java.util.List;
import java.util.Map;

public interface RoleMenuService {

	/**
	 * 查询指定角色可用菜单及已经使用的菜单
	 * @param roleId 
	 * @return [{id, name, sort, pid}]
	 */
	List<Map<String, Object>> load(Integer roleId);

	/**
	 * 
	 * @param roleId
	 * @param menuId
	 * @param checked
	 * @param siteId 
	 * @return
	 */
	int saveOrUpdate(Integer roleId, Integer menuId, Integer checked, Integer siteId);

}
