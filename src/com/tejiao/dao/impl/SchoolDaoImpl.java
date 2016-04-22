package com.tejiao.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.tejiao.dao.SchoolDao;
import com.tejiao.model.Parent;
import com.tejiao.model.School;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-29.
 */
@Repository
public class SchoolDaoImpl implements SchoolDao {

    @Autowired
    private HQLDao hqlDao;

    @Autowired
    private SQLDao sqlDao;

    @Override
    public void save(School school) {
        hqlDao.save(school);
    }

    @Override
    public PageList getPageList(Integer currentPage, Integer pageSize, Integer siteId, String keyword) {
        List<Object> params = new ArrayList<>();
        String sql = "select * from " + School.tableName + " where siteId = ? ";
        params.add(siteId);
        if(StringUtil.isNotEmpty(keyword)){
            sql += " and name like ? ";
            params.add("%" + keyword.trim() + "%");
        }
        sql += " order by id desc";
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public School get(Integer id) {
        return hqlDao.get(School.class, id);
    }

    @Override
    public void update(School school) {
        hqlDao.update(school);
    }

    @Override
    public int delete(Integer id, Integer siteId) {
        String sql = "DELETE FROM " + School.tableName + " WHERE id = ? AND siteId = ?";
        return sqlDao.update(sql, id, siteId);
    }

    @Override
    public List<Map<String, Object>> getBySiteId(Integer siteId) {
        String sql = "select * from "+School.tableName+" s where s.siteId = ? ;";
        return sqlDao.queryForList(sql, siteId);
    }

    @Override
    public School getByCode(String code, Integer siteId) {
        String hql = "from " + School.modelName + " where siteId = ? and code = ? ";
        return (School) hqlDao.getObjectByHQL(hql, siteId, code);
    }

    @Override
    public List<Map<String, Object>> getAllTown(Integer siteId) {
        String sql = "SELECT town FROM " + School.tableName + " WHERE siteId = ? GROUP BY town ";
        return sqlDao.queryForList(sql, siteId);
    }

    @Override
	public int saveAll(List<Map<String, Object>> saveList) {
		return sqlDao.save(School.tableName, saveList);
	}

}
