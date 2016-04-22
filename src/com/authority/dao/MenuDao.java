package com.authority.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface MenuDao {

	/**
	 * 查询所有菜单
	 * @return {id, name, url, pid, sort, level}
	 */
	List<Map<String, Object>> load();

	/**
	 * 保存一个新增的菜单
	 * @param map {name, pid, sort}
	 * @return
	 */
	Serializable save(Map<String, Object> map);

	/**
	 * 删除一个菜单，如果deleteChildren==1同时删除所有子菜单，同时会删除所有中间关系数据，角色菜单数据、菜单url地址数据
	 * @param id
	 * @param deleteChildren
	 * @return
	 */
	int delete(Integer id, Integer deleteChildren);

	/**
	 * 修改一个菜单数据
	 * @param map {id, name, pid, sort}
	 * @return
	 */
	int update(Map<String, Object> map);

	/**
	 * 查询所有一级菜单
	 * @return [{id, name, url, pid, sort}]
	 */
	List<Map<String, Object>> loadByPid();

	/**
	 * 查询指定pid的所有菜单
	 * @param pid
	 * @return [{id, name, url, pid, sort}]
	 */
	List<Map<String, Object>> loadByPid(Integer pid);

	/**
	 * 查询父级角色的所有菜单
	 * @param roleId 
	 * @return [{id, name, url, pid, sort}]
	 */
	List<Map<String, Object>> allParentMenu(Integer roleId);

}
