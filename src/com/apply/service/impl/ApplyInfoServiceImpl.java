package com.apply.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.ApplyInfoDao;
import com.apply.model.ApplyInfo;
import com.apply.service.ApplyInfoService;

@Service
public class ApplyInfoServiceImpl implements ApplyInfoService {
	
	@Autowired
	private ApplyInfoDao applyInfoDao;

	@Override
	public Integer saveOrUpdate(ApplyInfo applyInfo) {
		
		return applyInfoDao.saveOrUpdate(applyInfo);
	}

	@Override
	public List<ApplyInfo> getApplyInfo(Integer siteId, String applyNo) {
		
		return applyInfoDao.getApplyInfo(siteId, applyNo);
	}

	@Override
	public ApplyInfo get(Integer id) {
		return applyInfoDao.get(id);
	}

	@Override
	public Integer delete(Integer siteId, Integer... id) {
		return applyInfoDao.delete(siteId, id);
	}

}
