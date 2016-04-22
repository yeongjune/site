package com.site.dao;

import java.util.List;

import com.site.model.Label;

public interface LabelDao {

	/**
	 * 新增整站标签功能
	 * @param label
	 * @return
	 */
	String save(Label label);
	
	/**
	 * 查询所有功能标签
	 * @return
	 */
	List<Label> getListByState(Integer state);

	/**
	 * 查询所有功能标签
	 * @return
	 */
	List<Label> getList();
	
	
}
