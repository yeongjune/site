package com.site.service;

import java.util.List;

import com.site.model.TemplateData;

public interface TemplateDataService {
	
	/**
	 * 变更模板文件和数据源中间表关系
	 * 先清空之前关系，再新增中间表关系
	 * @param templateDataList
	 * @param siteId
	 * @param templateId
	 */
	void update(List<TemplateData> templateDataList, Integer siteId, Integer templateId);
	
}
