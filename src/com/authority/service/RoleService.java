package com.authority.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface RoleService {

	/**
	 * 加载所有角色
	 * @param siteId 
	 * @return [{id, name, sort, pid}]
	 */
	List<Map<String, Object>> load(Integer siteId);

	/**
	 * 保存一个新增的角色
	 * @param siteId 
	 * @param name
	 * @param pid
	 * @param sort
	 * @return
	 */
	Serializable save(Integer siteId, String name, Integer pid, Integer sort);

	/**
	 * 删除一个角色及其所有子角色，同时会删除角色相关的菜单数据
	 * @param id
	 * @param siteId 
	 * @return
	 */
	int delete(Integer id, Integer siteId);

	/**
	 * 修改一个角色数据
	 * @param id
	 * @param name
	 * @param pid
	 * @param sort
	 * @return
	 */
	int update(Integer id, String name, Integer pid, Integer sort);

}
