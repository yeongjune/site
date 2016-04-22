package com.site.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.site.dao.LabelDao;
import com.site.model.Label;

@Repository
public class LabelDaoImpl implements LabelDao {

	@Autowired
	private HQLDao hqlDao;
	
	@Override
	public String save(Label label) {
		return  (String) hqlDao.save(label);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Label> getListByState(Integer state){
		return hqlDao.getListByHQL("FROM " + Label.modelName + " AS label WHERE label.state = ? ORDER BY label.sort DESC", state);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Label> getList() {
		return hqlDao.getListByHQL("FROM " + Label.modelName + " AS label ORDER BY label.sort DESC");
	}

}
