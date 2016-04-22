package com.tejiao.dao;

import com.tejiao.model.UserType;

/**
 * Created by dzf on 15-12-29.
 */
public interface UserTypeDao {
    void deleteAll(Integer userId, Integer siteId);

    void save(UserType userType);

    UserType getByUserId(Integer userId, Integer siteId);
}
