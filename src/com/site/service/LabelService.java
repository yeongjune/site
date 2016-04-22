package com.site.service;

import java.util.List;
import java.util.Map;

import com.site.model.Label;

public interface LabelService {

	/**
	 * 新建功能标签
	 * 
	 * @param label
	 * @return
	 */
	String save(Label label);

	int delete(Integer id);

	int update(Integer id, String name);

	Map<String, Object> load(Integer id);

	Map<String, Object> getListByPage(Integer currentPage, Integer pageSize,
			String keyword);

	long countByName(String name);

	long countByNameWithSelf(Integer id, String name);
	
	/**
	 * 根据状态查询所有功能标签
	 * @return
	 */
	List<Label> getListByState(Integer state);
	
	/**
	 * 查询所有功能标签
	 * @return
	 */
	List<Label> getList();

}
