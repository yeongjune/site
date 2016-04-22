package com.site.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.vo.PageList;
import com.site.dao.SiteUserDao;
import com.site.model.SiteUser;
import com.site.service.SiteUserService;
import com.site.vo.SiteUserSearchVo;

@Service
public class SiteUserServiceImpl implements SiteUserService {
	
	@Autowired
	private SiteUserDao siteUserDao;

	@Override
	public Serializable save(SiteUser siteUser) {
		
		return siteUserDao.save(siteUser);
	}

	@Override
	public Serializable save(Map<String, Object> siteUser) {
		
		return siteUserDao.save(siteUser);
	}
	
	@Override
	public int saveBatch(List<Map<String, Object>> siteUserList) {
		return siteUserDao.saveBatch(siteUserList);
	}

	@Override
	public int update(SiteUser siteUser) {
		
		return siteUserDao.update(siteUser);
	}

	@Override
	public int update(Map<String, Object> siteUser) {
		
		return siteUserDao.update(siteUser);
	}

	@Override
	public int delete(Integer... id) {
		
		return siteUserDao.delete(id);
	}

	@Override
	public SiteUser get(Integer id) {
		
		return siteUserDao.get(id);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		
		return siteUserDao.load(id);
	}

	@Override
	public PageList findSiteUserPageList(SiteUserSearchVo searchVo,Integer pageSize,Integer currentPage) {
		
		return siteUserDao.findSiteUserPageList(searchVo, pageSize, currentPage);
	}

	@Override
	public List<Map<String, Object>> findSiteUserList(
			SiteUserSearchVo searchVo, Integer start, Integer limit) {
		
		return siteUserDao.findSiteUserList(searchVo, start, limit);
	}
	
	@Override
	public int checkPassword(Integer id, String password){
		return siteUserDao.checkPassword(id, password);
	}

	@Override
	public List<Map<String, Object>> findDepartmentTypes(Integer siteId) {
		return siteUserDao.findDepartmentTypes(siteId);
	}

	@Override
	public int checkAccount(Integer siteId, String account) {
		return siteUserDao.checkAccount(siteId, account);
	}

}
