package com.authority.dao;

import java.util.List;
import java.util.Map;

public interface UserRoleDao {

	/**
	 * 查询指定用户组所有的角色数据及用拥有的角色数据
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getRoleList(Integer userId);

	/**
	 * 保存或修改拥有与角色的关系数据
	 * @param siteId
	 * @param userId
	 * @param roleId
	 * @param checked
	 * @return
	 */
	int saveOrUpdate(Integer siteId, Integer userId, Integer roleId, Integer checked);

	/**
	 * 查询指定用户用的所有角色id
	 * @param userId
	 * @return
	 */
	List<Integer> getUserRole(Integer userId);

	/**
	 * 删除用户的所有角色
	 * @param userId
	 * @return
	 */
	int deleteByUserId(Integer userId);

	/**
	 * 删除所有用户的指定角色
	 * @param roleId
	 * @return
	 */
	int deleteByRoleId(Integer roleId);

	/**
	 * 删除指定站点所有用户与角色的关联数据
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);

}
