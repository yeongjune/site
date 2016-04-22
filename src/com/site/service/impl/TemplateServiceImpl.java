package com.site.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.dao.TempletDao;
import com.site.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TempletDao dao;

	@Override
	public Integer getTemplateId(String name, Integer siteId) {
		return dao.getTemplateId(name, siteId);
	}

	@Override
	public Map<String, Object> getListByPage(Integer siteId, Integer currentPage,
			Integer pageSize, String keyword) {
		return dao.getListByPage(siteId, currentPage, pageSize, keyword);
	}

	@Override
	public Serializable save(String name, Integer siteId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		map.put("name", name);
		return dao.save(map);
	}

	@Override
	public int delete(Integer id) {
		return dao.delete(id);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return dao.load(id);
	}

	@Override
	public int update(Integer id, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		return dao.update(map);
	}

	@Override
	public long countByName(Integer siteId, String name) {
		return dao.countByName(siteId, name);
	}

	@Override
	public long countByNameWithSelf(Integer id, String name) {
		return dao.countByNameWithSelf(id, name);
	}

}
