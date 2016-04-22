package com.site.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.dao.TemplateDataDao;
import com.site.model.TemplateData;
import com.site.service.TemplateDataService;

@Service
public class TemplateDataServiceImpl implements TemplateDataService {

	@Autowired
	private TemplateDataDao templateDataDao;
	
	@Override
	public void update(List<TemplateData> templateDataList, Integer siteId, Integer templateId) {
		templateDataDao.deleteByTempalteId(templateId, siteId);
		if (templateDataList!=null&&templateDataList.size()>0) {
			templateDataDao.save(templateDataList);
		}
	}

}
