package com.site.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.site.dao.DataDao;
import com.site.model.Column;
import com.site.model.Data;
import com.site.model.Label;
import com.site.model.Recommend;

@Repository
public class DataDaoImpl implements DataDao {

	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;
	
	@Override
	public Integer save(Data data) {
		return (Integer) hqlDao.save(data);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Data> getList(Integer templateId, Integer siteId) {
		String hql = ""
				+ " SELECT"
					+ " data" 
				+ " FROM"
					+ " TemplateData AS templateData,"
					+ " Template AS template,"
					+ " Data AS data"
				+ " WHERE"
					+ " data.id = templateData.dataId"
					+ " AND template.id = templateData.templateId"
					+ " AND templateData.siteId = ?"
					+ " AND templateData.templateId = ?";
		return hqlDao.getListByHQL(hql, siteId, templateId);
	}

	@Override
	public void delete(Integer id, Integer siteId) {
		hqlDao.updateByHQL("DELETE FROM Data AS data WHERE data.id = ? AND data.siteId = ?", id, siteId);
	}

	@Override
	public void update(Data data) {
		hqlDao.update(data);
	}

	@Override
	public List<Map<String, Object>> listByList(Integer siteId) {
		String sql = ""
				+ " SELECT"
					+ " data.id AS id,"
					+ " data.columnIds AS columnIds,"
					+ " data.name AS name,"
					+ " data.labelId AS labelId,"
					+ " data.size AS size,"
					+ " data.displayPage AS displayPage,"
					+ " data.everyColumn AS everyColumn,"
					+ " data.sortType AS sortType"
				+ " FROM "
					+ Data.tableName + " AS data"
				+ " WHERE"
					+ " data.labelId = ?"
					+ " AND data.siteId = ?";
		return sqlDao.queryForList(sql, Label.Item.List, siteId);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return sqlDao.delete(Data.tableName, "siteId", siteId);
	}

	@Override
	public List<Map<String, Object>> listByColumn(Integer siteId) {
		String sql = ""
				+ " SELECT"
					+ " data.id AS id,"
					+ " data.name AS name,"
					+ " data.columnIds AS columnIds,"
					+ " c.name AS columnName,"
					+ " data.labelId AS labelId"
				+ " FROM "
					+ Data.tableName + "  AS data"
					+ " LEFT JOIN " + Column.tableName + " AS c"
					+ " ON data.columnId = c.id"
				+ " WHERE"
					+ " data.labelId = ?"
					+ " AND data.siteId = ?" ;
		return sqlDao.queryForList(sql, Label.Item.Column, siteId);
	}

	@Override
	public List<Map<String, Object>> listByRecommend(Integer siteId) {
		String sql = ""
				+ " SELECT"
					+ " data.id AS id,"
					+ " data.name AS name,"
					+ " recommend.name AS recommendName,"
					+ " data.labelId AS labelId"
				+ " FROM "
					+ Data.tableName + "  AS data"
					+ " LEFT JOIN " + Recommend.tableName + " AS recommend"
					+ " ON data.recommendId = recommend.id"
				+ " WHERE"
					+ " data.labelId = ?"
					+ " AND data.siteId = ?" ;
		return sqlDao.queryForList(sql, Label.Item.Recommend, siteId);
	}

	@Override
	public List<Map<String, Object>> getList(Integer siteId) {
		String sql = ""
				+ " SELECT * "
				+ " FROM " + Data.tableName + " AS data"
				+ " WHERE data.siteId = ? OR data.siteId = 0 ORDER BY data.siteId, data.id " ;
		return sqlDao.queryForList(sql, siteId);
	}

	@Override
	public List<Map<String, Object>> listByOther(Integer siteId, String labelId) {
		String sql = ""
				+ " SELECT"
					+ " data.id AS id,"
					+ " data.name AS name,"
					+ " data.labelId AS labelId"
				+ " FROM "
					+ Data.tableName + "  AS data"
				+ " WHERE"
					+ " data.labelId = ?"
					+ " AND data.siteId = ?" ;
		return sqlDao.queryForList(sql, labelId, siteId);
	}


	@Override
	public Map<String, Object> load(Integer id, Integer siteId) {
		String sql = "SELECT * FROM " + Data.tableName + " AS data WHERE data.id = ? AND data.siteId = ?";
		return sqlDao.queryForMap(sql, id, siteId);
	}


	@Override
	public Data getByNameAndSiteId(String name, Integer siteId) {
		return (Data) hqlDao.getObjectByHQL("FROM " + Data.modelName + " AS data WHERE data.labelId = ? AND data.siteId = ?", name, siteId);
	}
}
