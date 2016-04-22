package com.apply.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.JxStudentDao;
import com.apply.model.JxStudent;
import com.apply.service.JxStudentService;
import com.apply.vo.JxStudentSearchVo;
import com.base.vo.PageList;

@Service
public class JxStudentServiceImpl implements JxStudentService {
	
	@Autowired
	private JxStudentDao jxStudentDao;

	@Override
	public Integer saveOrUpdate(JxStudent jxStudent) {
		
		return jxStudentDao.saveOrUpdate(jxStudent);
	}

	@Override
	public Integer delete(Integer siteId,Integer... id) {
		
		return jxStudentDao.delete(siteId,id);
	}

	@Override
	public JxStudent getJxStudent(Integer id) {
		
		return jxStudentDao.getJxStudent(id);
	}

	@Override
	public PageList<JxStudent> getJxStudentPageList(JxStudentSearchVo jxStudentSearchVo) {
		
		return jxStudentDao.getJxStudentPageList(jxStudentSearchVo);
	}

	@Override
	public List<Map<String, Object>> getJxStudentList(JxStudentSearchVo jxStudentSearchVo) {
		return jxStudentDao.getJxStudentList(jxStudentSearchVo);
	}

}
