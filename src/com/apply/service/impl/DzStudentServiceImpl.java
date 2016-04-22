package com.apply.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.DzStudentDao;
import com.apply.model.DzStudent;
import com.apply.service.DzStudentService;
import com.base.vo.PageList;

@Service
public class DzStudentServiceImpl implements DzStudentService {
	
	@Autowired
	private DzStudentDao dzStudentDao;

	@Override
	public Integer saveOrUpdate(DzStudent dzStudent) {
		
		return dzStudentDao.saveOrUpdate(dzStudent);
	}

	@Override
	public Integer delete(Integer siteId,Integer... id) {
		
		return dzStudentDao.delete(siteId,id);
	}

	@Override
	public DzStudent getDzStudent(Integer id) {
		
		return dzStudentDao.getDzStudent(id);
	}

	@Override
	public PageList<DzStudent> getDzStudentPageList(Integer siteId,Integer pageSize,
			Integer currentPage, String keyword,String year) {
		
		return dzStudentDao.getDzStudentPageList(siteId,pageSize, currentPage, keyword,year);
	}

	@Override
	public List<Map<String, Object>> getDzStudentList(Integer siteId,
			String year, String keyword, Integer... id) {
		return dzStudentDao.getDzStudentList(siteId, year, keyword, id);
	}

}
