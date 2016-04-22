package com.authority.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.UrlDao;
import com.authority.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private UrlDao dao;

	@Override
	public List<Map<String, Object>> load() {
		return dao.allUrl();
	}

	@Override
	public int update(String id, Integer checked) {
		return dao.update(id, checked);
	}

	@Override
	public Map<String, Object> get(String id) {
		return dao.get(id);
	}

}
