package com.apply.dao.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apply.dao.RecruitDateDao;
import com.apply.model.RecruitDate;
import com.base.dao.HQLDao;

@Repository
public class RecruitDateDaoImpl implements RecruitDateDao {
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(RecruitDate recruitDate) {
		return hqlDao.save(recruitDate);
	}

	@Override
	public int delete(String id) {
		 hqlDao.delete(RecruitDate.class, id);
		 return 1;
	}

	@Override
	public int update(RecruitDate recruitDate) {
		hqlDao.update(recruitDate);
		return 1;
	}

	@Override
	public RecruitDate get(Integer id) {
		return hqlDao.get(RecruitDate.class, id);
	}

	@Override
	public RecruitDate load(Integer siteId) {
		String hql=" from RecruitDate as r where r.siteId= ? ";
		return (RecruitDate) hqlDao.getObjectByHQL(hql, siteId);
	}

}
