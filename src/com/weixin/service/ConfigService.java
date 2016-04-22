package com.weixin.service;

import com.weixin.model.Config;

import java.util.Map;

/**
 * Created by dzf on 2015/11/13.
 */
public interface ConfigService {

    /**
     * 获取网站配置
     * @param siteId
     * @return
     */
    Map<String,Object> getBySiteId(Integer siteId);

    /**
     * 更新接口配置信息
     * @param siteId
     * @param appID
     * @param appsecret
     */
    void update(Integer siteId, String appID, String appsecret);

    Config getObjBySiteId(Integer siteId);
}
