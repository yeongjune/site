package com.authority.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.SiteRoleDao;
import com.authority.service.SiteRoleService;

@Service
public class SiteRoleServiceImpl implements SiteRoleService {

	@Autowired
	private SiteRoleDao dao;

	@Override
	public List<Map<String, Object>> getRoleList(Integer siteId) {
		return dao.getRoleList(siteId);
	}

	@Override
	public int saveOrUpdate(Integer siteId, Integer roleId, Integer checked) {
		return dao.saveOrUpdate(siteId, roleId, checked);
	}

}
