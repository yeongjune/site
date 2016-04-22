package com.weixin.dao;

import com.weixin.model.Config;

import java.util.Map;

/**
 * Created by dzf on 2015/11/13.
 */
public interface ConfigDao {
    /**
     * 获取接口配置
     * @param siteId
     * @return
     */
    Map<String,Object> getBySiteId(Integer siteId);

    /**
     * 获取网站微信接口配置信息
     * @param siteId
     * @return
     */
    Config get(Integer siteId);

    void save(Config config);
}
