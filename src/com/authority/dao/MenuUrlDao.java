package com.authority.dao;

import java.util.List;
import java.util.Map;

public interface MenuUrlDao {

	/**
	 * 加载所有url数据及指定菜单关联url的数据
	 * @param menuId
	 * @return [{id, url, pid, checked}]
	 */
	List<Map<String, Object>> load(Integer menuId);

	/**
	 * 保存或修改指定菜单与url的关联关系
	 * @param menuId
	 * @param urlId
	 * @param checked
	 * @return
	 */
	int saveOrUpdate(Integer menuId, String urlId, Integer checked);

}
