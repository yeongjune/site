package com.authority.dao;

import java.util.List;
import java.util.Map;

public interface RoleMenuDao {

	/**
	 * 保存或修改角色的菜单关系数据
	 * @param roleId
	 * @param menuId
	 * @param checked
	 * @return
	 */
	int saveOrUpdate(Integer roleId, Integer menuId, Integer checked);

	/**
	 * 查询指定角色的菜单数据
	 * @param roleId
	 * @return [{id, roleId, menuId, checked}]
	 */
	List<Map<String, Object>> load(Integer roleId);

	/**
	 * 查询指定角色拥有的菜单数据
	 * @param roleId
	 * @return [{id, name, url, pid, sort}]
	 */
	List<Map<String, Object>> loadMenu(Integer roleId);

}
