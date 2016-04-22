package com.weixin.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.weixin.dao.MediaDao;
import com.weixin.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/16.
 */
@Component
public class MediaDaoImpl implements MediaDao {

    @Autowired
    private SQLDao sqlDao;

    @Autowired
    private HQLDao hqlDao;

    @Override
    public List<Map<String, Object>> getAllType(Integer siteId) {
        String sql = "SELECT * FROM " + MediaType.tableName + " WHERE siteId = ? ";
        return sqlDao.queryForList(sql, siteId);
    }
}
