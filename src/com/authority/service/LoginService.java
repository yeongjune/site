package com.authority.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LoginService {

	/**
	 * 按层及加载所有菜单数据，用于系统导航菜单，目前只加载三级
	 * @return [{id, name, url, pid, sort, children=[{}]}]
	 */
	List<Map<String, Object>> loadForMenu();

	/**
	 * 按层及加载指定用户的菜单数据，用于系统导航菜单，目前只加载三级
	 * @param userId
	 * @return [{id, name, url, pid, sort, children=[{}]}]
	 */
	List<Map<String, Object>> loadForMenu(Integer userId);

	/**
	 * 查询所有的url地址
	 * @return [{id, url, pid}]
	 */
	Set<String> getUrlSet();

	/**
	 * 查询指定用户允许访问的url地址
	 * @param userId
	 * @return [{id, url, pid}]
	 */
	Set<String> getUrlSet(Integer userId);

	/**
	 * 重置指定网站管理员帐号的密码为“123456”
	 * @param siteId
	 * @return
	 */
	int resetPassword(Integer siteId);

	/**
	 * 修改指定网站指定用户的密码
	 * @param siteId
	 * @param userId
	 * @param password
	 * @param newPassword
	 * @return
	 */
	int updatePassword(Integer siteId, Integer userId, String password,
			String newPassword);

}
