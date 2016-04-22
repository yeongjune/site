package com.site.service;

import java.util.List;
import java.util.Map;

public interface UserColumnRoleService {

	/**
	 * 查询指定站点用户
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> getUserList(Integer siteId);

	/**
	 * 查询所有角色数据及指定用户的角色数据
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getRoleList(Integer userId);

	/**
	 * 保存或修改指定用户与指定角色的关系
	 * @param siteId
	 * @param userId
	 * @param roleId
	 * @param checked
	 * @return
	 */
	int saveOrUpdate(Integer siteId, Integer userId, Integer roleId, Integer checked);

}
