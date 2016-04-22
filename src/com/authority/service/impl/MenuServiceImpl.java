package com.authority.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.MenuDao;
import com.authority.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao dao;

	@Override
	public List<Map<String, Object>> load() {
		return dao.load();
	}

	@Override
	public Serializable save(String name, Integer pid, Integer sort) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("pid", pid);
		map.put("sort", sort);
		return dao.save(map);
	}

	@Override
	public int delete(Integer id, Integer deleteChildren) {
		return dao.delete(id, deleteChildren);
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

	@Override
	public int updateUrl(Integer id, String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("url", url);
		return dao.update(map);
	}

}
