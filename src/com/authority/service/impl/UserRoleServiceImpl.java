package com.authority.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.UserDao;
import com.authority.dao.UserRoleDao;
import com.authority.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	private UserRoleDao dao;
	@Autowired
	private UserDao userDao;

	@Override
	public List<Map<String, Object>> getUserList(Integer siteId) {
		return userDao.getList(siteId);
	}

	@Override
	public List<Map<String, Object>> getRoleList(Integer userId) {
		return dao.getRoleList(userId);
	}

	@Override
	public int saveOrUpdate(Integer siteId, Integer userId, Integer roleId, Integer checked) {
		return dao.saveOrUpdate(siteId, userId, roleId, checked);
	}

}
