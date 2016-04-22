package com.weixin.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.weixin.dao.MenuDao;
import com.weixin.model.WeiXinMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/12.
 */
@Component("weiXinMenuDaoImpl")
public class MenuDaoImpl implements MenuDao {

    @Autowired
    private SQLDao sqlDao;

    @Autowired
    private HQLDao hqlDao;

    @Override
    public List<Map<String, Object>> getAll(Integer siteId) {
        String sql = "SELECT * FROM " + WeiXinMenu.tableName + " WHERE siteId = ? ORDER BY sort ";
        return sqlDao.queryForList(sql, siteId);
    }

    @Override
    public Integer getMaxSort(Integer siteId, Integer pid) {
        String sql = "SELECT max(sort) FROM " + WeiXinMenu.tableName + " WHERE siteId = ? AND pid = ? ";
        return sqlDao.queryForInt(sql, siteId, pid);
    }

    @Override
    public void save(WeiXinMenu menu) {
        hqlDao.save(menu);
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM " + WeiXinMenu.tableName + " WHERE id = ? or pid = ? ";
        sqlDao.update(sql, id, id);
    }

    @Override
    public Integer getMenuCountByPid(Integer siteId, Integer pid) {
        String sql = "SELECT count(*) FROM " + WeiXinMenu.tableName + " WHERE siteId = ? AND pid = ?";
        return sqlDao.queryForInt(sql, siteId, pid);
    }

    @Override
    public WeiXinMenu get(Integer id) {
        return hqlDao.get(WeiXinMenu.class, id);
    }

    @Override
    public void update(WeiXinMenu menu) {
        hqlDao.update(menu);
    }

    @Override
    public Map<String, Object> getMap(Integer siteId, Integer id) {
        String sql = "SELECT * FROM " + WeiXinMenu.tableName + " WHERE siteId = ? AND id = ?";
        return sqlDao.queryForMap(sql, siteId, id);
    }

    @Override
    public int moveToParent(Integer id, Integer tarId, int maxSort, Integer siteId) {
        String sql = "UPDATE  " +
                WeiXinMenu.tableName +
                "SET " +
                "  pid = ?, " +
                "  sort = ? + 1  " +
                "WHERE id = ? AND siteId = ? ";
        return sqlDao.update(sql, tarId, maxSort, id, siteId);
    }

    @Override
    public void sortPlusOne(Integer pid, Integer sort, Integer siteId) {
        String sql = "UPDATE "+ WeiXinMenu.tableName +" SET sort = sort + 1 WHERE pid = ? AND sort > ? AND siteId = ? ";
        sqlDao.update(sql, pid, sort, siteId);
    }

    @Override
    public int update(Map<String, Object> child) {
        return sqlDao.updateMap(WeiXinMenu.tableName, "id", child);
    }

}
