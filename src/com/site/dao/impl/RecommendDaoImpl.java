package com.site.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.site.dao.RecommendDao;
import com.site.model.Column;
import com.site.model.Recommend;

@Repository
public class RecommendDaoImpl implements RecommendDao {

	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;
	
	@Override
	public List<Map<String, Object>> list(Integer siteId) {
		String sql = ""
				+ "SELECT "
				+ " recommend.id AS id,"
				+ " recommend.name AS name,"
				+ " c.name AS columnName"
				+ " FROM "
					+ Recommend.tableName +" AS recommend"
					+ " LEFT JOIN " + Column.tableName +" AS c"
					+ " ON recommend.columnId = c.id"
				+ " WHERE"
					+ " recommend.siteId = ?";
		return sqlDao.queryForList(sql, siteId);
	}
	
	@Override
	public List<Map<String, Object>> list(Integer siteId, Integer columnId) {
		String sql = ""
				+ "SELECT "
				+ " recommend.id AS id," 
				+ " recommend.name AS name,"
				+ " c.name AS columnName"
				+ " FROM "
					+ Recommend.tableName +" AS recommend"
					+ " LEFT JOIN " + Column.tableName +" AS c"
					+ " ON recommend.columnId = c.id"
				+ " WHERE"
					+ " recommend.siteId = ?";
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(siteId);
		
		if(columnId != null && columnId.intValue() != -1){
			sql += " AND recommend.columnId = ?";
			paramList.add(columnId);
		}
		
		return sqlDao.queryForList(sql, paramList.toArray());
	}

	@Override
	public Recommend get(Integer id, Integer siteId) {
		return (Recommend) hqlDao.getObjectByHQL("FROM Recommend AS recommend WHERE recommend.id = ? AND recommend.siteId = ?", id, siteId);
	}

	@Override
	public Integer save(Recommend recommend) {
		return (Integer) hqlDao.save(recommend);
	}

	@Override
	public void update(Recommend recommend) {
		hqlDao.update(recommend);
	}

	@Override
	public void delete(Integer id, Integer siteId) {
		String hql = "DELETE FROM Recommend AS recommend WHERE recommend.id = ? AND recommend.siteId = ?";
		hqlDao.updateByHQL(hql, id, siteId);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return sqlDao.delete(Recommend.tableName, "siteId", siteId);
	}

	

	
	
}
