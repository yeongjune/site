package com.weixin.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/16.
 */
public interface MediaDao {

    /**
     * 获取所有素材类型
     * @param siteId
     * @return
     */
    List<Map<String,Object>> getAllType(Integer siteId);
}
