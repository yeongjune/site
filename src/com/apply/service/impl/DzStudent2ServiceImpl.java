package com.apply.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.DzStudent2Dao;
import com.apply.model.DzStudent2;
import com.apply.service.DzStudent2Service;
import com.base.vo.PageList;

@Service
public class DzStudent2ServiceImpl implements DzStudent2Service {
	
	@Autowired
	private DzStudent2Dao dzStudent2Dao;

	@Override
	public Integer saveOrUpdate(DzStudent2 dzStudent2) {
		
		return dzStudent2Dao.saveOrUpdate(dzStudent2);
	}

	@Override
	public Integer delete(Integer siteId, Integer... id) {
		
		return dzStudent2Dao.delete(siteId, id);
	}

	@Override
	public DzStudent2 getDzStudent2(Integer id) {
		
		return dzStudent2Dao.getDzStudent2(id);
	}

	@Override
	public PageList<DzStudent2> getDzStudent2PageList(Integer siteId,
			Integer pageSize, Integer currentPage, String keyword, String year) {
		
		return dzStudent2Dao.getDzStudent2PageList(siteId, pageSize, currentPage, keyword, year);
	}

	@Override
	public List<Map<String, Object>> getDzStudent2List(Integer siteId,
			String year, String keyword, Integer... id) {
		
		return dzStudent2Dao.getDzStudent2List(siteId, year, keyword, id);
	}

}
