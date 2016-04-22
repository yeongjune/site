package com.site.dao;

import java.util.List;

import com.site.model.TemplateData;


public interface TemplateDataDao {

	/**
	 * 保存模板跟数据关系
	 * @param template
	 * @return
	 */
	Integer save(TemplateData templateData);

	/**
	 * 删除指定站点所有数据源关联关系
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);
	
	/**
	 * 根据模板ID 删除相关数据源
	 * @param templateId
	 */
	void deleteByTempalteId(Integer templateId, Integer siteId);
	
	/**
	 * 批量保存模板跟数据源关系
	 * @param templateDataList
	 */
	void save(List<TemplateData> templateDataList);
	
}
