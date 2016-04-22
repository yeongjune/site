package com.site.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.site.dao.TemplateDataDao;
import com.site.model.TemplateData;

@Repository
public class TemplateDataDaoImpl implements TemplateDataDao {

	@Autowired
	private HQLDao hqlDao;
	@Autowired
	private SQLDao sqlDao;
	
	@Override
	public Integer save(TemplateData templateData) {
		return (Integer) hqlDao.save(templateData);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return sqlDao.delete(TemplateData.tableName, "siteId", siteId);
	}

	@Override
	public void save(List<TemplateData> templateDataList) {
		hqlDao.saveOrUpdateAll(templateDataList);
	}

	@Override
	public void deleteByTempalteId(Integer templateId, Integer siteId) {
		String hql = ""
				+ " DELETE"
				+ " FROM "
					+ TemplateData.modelName + " AS templateData"
				+ " WHERE"
					+ " templateData.templateId = ?"; 
		hqlDao.updateByHQL(hql, templateId);
	}


}
