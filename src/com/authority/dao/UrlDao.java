package com.authority.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UrlDao {

	/**
	 * 查询所有非公共的url地址
	 * @return [{id, url, pid}]
	 */
	List<Map<String, Object>> load();

	/**
	 * 查询所有的url地址
	 * @return [{id, url, pid}]
	 */
	Set<String> getUrlSet();

	/**
	 * 查询指定用户允许访问的url地址
	 * @param menuIds
	 * @return [{id, url, pid}]
	 */
	Set<String> getUrlSet(Set<Integer> menuIds);

	/**
	 * 查询系统所有url数据
	 * @return
	 */
	List<Map<String, Object>> allUrl();

	/**
	 * 修改url的公共状态
	 * @param id
	 * @param checked
	 * @return
	 */
	int update(String id, Integer checked);

	/**
	 * 修改url的开放状态
	 * @param id
	 * @param checked
	 * @return
	 */
	int updateIsPublic(String id, Integer checked);

	/**
	 * 查询一个url数据
	 * @param id
	 * @return
	 */
	Map<String, Object> get(String id);

}
