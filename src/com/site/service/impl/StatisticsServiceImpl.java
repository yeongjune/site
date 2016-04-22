package com.site.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.dao.StatisticsDao;
import com.site.service.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService{

	@Autowired
	private StatisticsDao statisticsDao;
	
	@Override
	public void saveOrUpdatePageView(Integer siteId) {
		statisticsDao.saveOrUpdatePageView(siteId);
		
	}

	@Override
	public Integer findPageView(Integer siteId) {
		return statisticsDao.findPageView(siteId);
	}

}
