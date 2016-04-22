package com.tejiao.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.tejiao.dao.UserTypeDao;
import com.tejiao.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by dzf on 15-12-29.
 */
@Repository
public class UserTypeDaoImpl implements UserTypeDao {

    @Autowired
    private HQLDao hqlDao;

    @Autowired
    private SQLDao sqlDao;

    @Override
    public void deleteAll(Integer userId, Integer siteId) {
        String sql = "DELETE FROM " + UserType.tableName + " WHERE userId = ? AND siteId = ?";
        sqlDao.update(sql, userId, siteId);
    }

    @Override
    public void save(UserType userType) {
        hqlDao.save(userType);
    }

    @Override
    public UserType getByUserId(Integer userId, Integer siteId) {
        String hql = "from " + UserType.modelName + " where userId = ? and siteId = ? ";
        return (UserType) hqlDao.getObjectByHQL(hql, userId, siteId);
    }
}
