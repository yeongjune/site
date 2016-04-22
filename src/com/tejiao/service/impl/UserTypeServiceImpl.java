package com.tejiao.service.impl;

import com.tejiao.dao.UserTypeDao;
import com.tejiao.model.UserType;
import com.tejiao.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dzf on 15-12-29.
 */
@Service
public class UserTypeServiceImpl implements UserTypeService {

    @Autowired
    private UserTypeDao userTypeDao;

    @Override
    public void save(UserType userType) {
        userTypeDao.deleteAll(userType.getUserId(), userType.getSiteId());
        userTypeDao.save(userType);
    }

    @Override
    public UserType get(Integer userId, Integer siteId) {
        return userTypeDao.getByUserId(userId, siteId);
    }

}
