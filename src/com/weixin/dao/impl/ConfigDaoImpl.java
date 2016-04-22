package com.weixin.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.weixin.dao.ConfigDao;
import com.weixin.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by dzf on 2015/11/13.
 */
@Component
public class ConfigDaoImpl implements ConfigDao {

    @Autowired
    private SQLDao sqlDao;

    @Autowired
    private HQLDao hqlDao;

    @Override
    public Map<String, Object> getBySiteId(Integer siteId) {
        String sql = "SELECT * FROM " + Config.tableName + " WHERE siteId = ?";
        return sqlDao.queryForMap(sql, siteId);
    }

    @Override
    public Config get(Integer siteId) {
        String hql = "from " + Config.modelName + " where siteId = ?";
        return (Config) hqlDao.getObjectByHQL(hql, siteId);
    }

    @Override
    public void save(Config config) {
        hqlDao.save(config);
    }
}
