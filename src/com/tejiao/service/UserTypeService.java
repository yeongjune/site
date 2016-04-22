package com.tejiao.service;

import com.tejiao.model.UserType;

/**
 * Created by dzf on 15-12-29.
 */
public interface UserTypeService {
    void save(UserType userType);

    UserType get(Integer userId, Integer siteId);
}
