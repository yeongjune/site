package com.apply.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.RecruitDateDao;
import com.apply.model.RecruitDate;
import com.apply.service.RecruitDateService;

@Service
public class RecruitDateServiceImpl implements RecruitDateService {
	@Autowired
	private RecruitDateDao recruitDateDao;

	@Override
	public Serializable save(RecruitDate recruitDate) {
		
		return recruitDateDao.save(recruitDate);
	}

	@Override
	public int delete(String id) {
		
		return recruitDateDao.delete(id);
	}

	@Override
	public int update(RecruitDate recruitDate) {
		
		return recruitDateDao.update(recruitDate);
	}

	@Override
	public RecruitDate get(Integer id) {
		
		return recruitDateDao.get(id);
	}

	@Override
	public RecruitDate load(Integer siteId) {
		
		return recruitDateDao.load(siteId);
	}

}
