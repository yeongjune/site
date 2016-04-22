package com.site.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.dao.RecommendDao;
import com.site.model.Recommend;
import com.site.service.RecommendService;

@Service
public class RecommendServiceImpl implements RecommendService {
	
	@Autowired
	private RecommendDao recommendDao;

	@Override
	public List<Map<String, Object>> getAll(Integer siteId) {
		return recommendDao.list(siteId);
	}

	@Override
	public List<Map<String, Object>> getAll(Integer siteId, Integer columnId) {
		return recommendDao.list(siteId, columnId);
	}

	@Override
	public Recommend get(Integer id, Integer siteId) {
		return recommendDao.get(id, siteId); 
	}

	@Override
	public Integer save(Recommend recommend) {
		return recommendDao.save(recommend);
	}

	@Override
	public void update(Recommend recommend) {
		recommendDao.update(recommend);
	}

	@Override
	public void delete(Integer id, Integer siteId) {
		recommendDao.delete(id, siteId);
	}

}
