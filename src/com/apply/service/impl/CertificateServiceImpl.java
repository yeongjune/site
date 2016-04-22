package com.apply.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.CertificateDao;
import com.apply.model.Certificate;
import com.apply.service.CertificateService;

@Service
public class CertificateServiceImpl implements CertificateService {
	@Autowired
	private CertificateDao certificateDao;

	@Override
	public Serializable save(Certificate certificate) {
		
		return certificateDao.save(certificate);
	}

	@Override
	public int delete(String ids,Integer siteId) {
		
		return certificateDao.delete(ids,siteId);
	}

	@Override
	public int update(Map<String, Object> certificate) {
		
		return certificateDao.update(certificate);
	}

	@Override
	public Certificate get(Integer id) {
		
		return certificateDao.get(id);
	}

	@Override
	public List<Map<String, Object>> findCertificateList(String name,Integer siteId) {
		
		return certificateDao.findCertificateList(name,siteId);
	}

	@Override
	public Map<String, Object>  findByName(String name,Integer siteId) {
		return certificateDao.findByName(name,siteId);
	}

}
