package com.site.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.site.dao.StatisticsDao;
import com.site.model.Statistics;

@Repository
public class StatisticsDaoImpl implements StatisticsDao{

	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;
	
	@Override
	public void saveOrUpdatePageView(Integer siteId) {
		String sql = " SELECT id FROM " + Statistics.tableName + " WHERE siteId = ? ";
		int id = sqlDao.queryForInt(sql, siteId);
		//已有该站点记录则更新，否则保存
		if(id != 0){
			String updateSql = " UPDATE " + Statistics.tableName + " SET pageView = pageView + 1 WHERE siteId = ?";
			sqlDao.update(updateSql, siteId);
		}else{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("siteId", siteId);
			paramMap.put("pageView", 1);
			sqlDao.save(Statistics.tableName, paramMap);
		}
	}

	@Override
	public Integer findPageView(Integer siteId) {
		String sql = " SELECT pageView FROM " + Statistics.tableName + " WHERE siteId = ?";
		return sqlDao.queryForInt(sql, siteId);
	}

}
