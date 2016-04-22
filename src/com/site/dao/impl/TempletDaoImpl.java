package com.site.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.SQLDao;
import com.site.dao.TempletDao;
import com.site.model.Template;

@Repository
public class TempletDaoImpl implements TempletDao {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Integer getTemplateId(String name, Integer siteId) {
		return sqlDao.queryForInt("select id from "+Template.tableName+" where name=? and siteId=?", name, siteId);
	}

	@Override
	public int save(List<Map<String, Object>> mapList) {
		return sqlDao.save(Template.tableName, mapList);
	}

	@Override
	public Map<String, Object> getListByPage(Integer siteId, Integer currentPage,
			Integer pageSize, String keyword) {
		return sqlDao.getPageMap("select * from "+Template.tableName+" where siteId=? and name like ? order by id desc", currentPage, pageSize, siteId, keyword==null||keyword.trim().equals("")?"%":keyword+"%");
	}

	@Override
	public Serializable save(Map<String, Object> map) {
		return sqlDao.save(Template.tableName, map);
	}

	@Override
	public int delete(Integer id) {
		return sqlDao.delete(Template.tableName, id);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return sqlDao.queryForMap("select * from "+Template.tableName+" where id=?", id);
	}

	@Override
	public int update(Map<String, Object> map) {
		return sqlDao.updateMap(Template.tableName, "id", map);
	}

	@Override
	public long countByName(Integer siteId, String name) {
		return sqlDao.queryForLong("select count(id) from "+Template.tableName+" where siteId=? and name=?", siteId, name);
	}

	@Override
	public long countByNameWithSelf(Integer id, String name) {
		Integer siteId = sqlDao.queryForInt("select siteId from "+Template.tableName+" where id=?", id);
		return sqlDao.queryForLong("select count(id) from "+Template.tableName+" where siteId=? and id!=? and name=?", siteId, id, name);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return sqlDao.delete(Template.tableName, "siteId", siteId);
	}

}
