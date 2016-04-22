package com.authority.service;

import java.util.List;
import java.util.Map;

public interface SiteRoleService {

	/**
	 * 查询所有角色数据及指定用户的角色数据
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getRoleList(Integer siteId);

	/**
	 * 保存或修改指定用户与指定角色的关系
	 * @param siteId
	 * @param roleId
	 * @param checked
	 * @return
	 */
	int saveOrUpdate(Integer siteId, Integer roleId, Integer checked);

}
