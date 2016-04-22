package com.apply.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.CxStudentDao;
import com.apply.model.CxStudent;
import com.apply.service.CxStudentService;
import com.apply.vo.CxStudentSearchVo;
import com.base.vo.PageList;

@Service
public class CxStudentServiceImpl implements CxStudentService {
	
	@Autowired
	private CxStudentDao cxStudentDao;

	@Override
	public Integer saveOrUpdate(CxStudent cxStudent) {
		
		return cxStudentDao.saveOrUpdate(cxStudent);
	}

	@Override
	public Integer delete(Integer siteId,Integer... id) {
		
		return cxStudentDao.delete(siteId,id);
	}

	@Override
	public CxStudent getCxStudent(Integer id) {
		
		return cxStudentDao.getCxStudent(id);
	}

	@Override
	public PageList<CxStudent> getCxStudentPageList(CxStudentSearchVo cxStudentSearchVo) {
		
		return cxStudentDao.getCxStudentPageList(cxStudentSearchVo);
	}

	@Override
	public List<Map<String, Object>> getCxStudentList(CxStudentSearchVo cxStudentSearchVo) {
		return cxStudentDao.getCxStudentList(cxStudentSearchVo);
	}

}
