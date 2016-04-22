package com.authority.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface MenuService {

	/**
	 * 查询所有菜单
	 * @return {id, name, url, pid, sort}
	 */
	List<Map<String, Object>> load();

	/**
	 * 保存一个新增的菜单
	 * @param name
	 * @param pid
	 * @param sort
	 * @return
	 */
	Serializable save(String name, Integer pid, Integer sort);

	/**
	 * 删除一个菜单，如果deleteChildren==1同时删除所有子菜单，同时会删除所有中间关系数据，角色菜单数据、菜单url地址数据
	 * @param id
	 * @param deleteChildren
	 * @return
	 */
	int delete(Integer id, Integer deleteChildren);

	/**
	 * 修改一个菜单数据
	 * @param id
	 * @param name
	 * @param pid
	 * @param sort
	 * @return
	 */
	int update(Integer id, String name, Integer pid, Integer sort);

	/**
	 * 修改菜单自身的url地址
	 * @param id
	 * @param url
	 * @return
	 */
	int updateUrl(Integer id, String url);

}
