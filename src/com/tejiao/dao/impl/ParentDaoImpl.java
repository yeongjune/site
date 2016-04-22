package com.tejiao.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.tejiao.dao.ParentDao;
import com.tejiao.model.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
@Repository
public class ParentDaoImpl implements ParentDao {

    @Autowired
    private HQLDao hqlDao;

    @Autowired
    private SQLDao sqlDao;

    @Override
    public Parent getByAccount(String account, Integer siteId) {
        String hql = "from " + Parent.modelName + " where account = ? and siteId = ?";
        return (Parent) hqlDao.getObjectByHQL(hql, account, siteId);
    }

    @Override
    public void save(Parent parent) {
        hqlDao.save(parent);
    }

    @Override
    public void update(Parent parent) {
        hqlDao.update(parent);
    }

    @Override
    public List<String> getAccountList(Integer siteId) {
        String sql = "SELECT account FROM " + Parent.tableName + " WHERE siteId = ? ";
        return sqlDao.queryForList(sql, String.class, siteId);
    }

    @Override
    public int saveAll(List<Map<String, Object>> saveList) {
        return sqlDao.save(Parent.tableName, saveList);
    }

    @Override
    public PageList getPageList(Integer currentPage, Integer pageSize, String keyword, Integer siteId) {
        List<Object> params = new ArrayList<>();
        String sql = "SELECT  * FROM " + Parent.tableName + " WHERE siteId = ? ";
        params.add(siteId);
        if(StringUtil.isNotEmpty(keyword)){
            sql += " AND ( name like ? OR account like ? )";
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        sql += " order by id desc ";
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public int updatePassword(Integer id, Integer siteId, String password) {
        String sql = "UPDATE " + Parent.tableName + " SET password = ? WHERE id = ? AND siteId = ?";
        return sqlDao.update(sql, password, id, siteId);
    }

    @Override
    public Parent get(Integer id) {
        return hqlDao.get(Parent.class, id);
    }

}
