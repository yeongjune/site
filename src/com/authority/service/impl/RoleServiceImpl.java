package com.authority.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.RoleDao;
import com.authority.dao.SiteRoleDao;
import com.authority.dao.UserRoleDao;
import com.authority.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao dao;
	@Autowired
	private SiteRoleDao siteRoleDao;
	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public List<Map<String, Object>> load(Integer siteId) {
		return dao.load(siteId);
	}

	@Override
	public Serializable save(Integer siteId, String name, Integer pid, Integer sort) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		map.put("name", name);
		map.put("pid", pid);
		map.put("sort", sort);
		return dao.save(map);
	}

	@Override
	public int delete(Integer id, Integer siteId) {
		int i = dao.delete(id, siteId);
		if(i>0){
			siteRoleDao.deleteByRoleId(id);
			userRoleDao.deleteByRoleId(id);
		}
		return i;
	}

	@Override
	public int update(Integer id, String name, Integer pid, Integer sort) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("pid", pid);
		map.put("sort", sort);
		return dao.update(map);
	}

}
