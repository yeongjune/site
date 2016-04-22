package com.site.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.dao.LabelDao;
import com.site.model.Label;
import com.site.service.LabelService;

@Service
public class LabelServiceImpl implements LabelService {
	
	@Autowired
	private LabelDao labelDao;

	@Override
	public String save(Label label) {
		return labelDao.save(label);
	}

	@Override
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Integer id, String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Object> load(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getListByPage(Integer currentPage,
			Integer pageSize, String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countByName(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long countByNameWithSelf(Integer id, String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Label> getListByState(Integer state) {
		return labelDao.getListByState(state);
	}

	@Override
	public List<Label> getList() {
		return labelDao.getList();
	}

}
