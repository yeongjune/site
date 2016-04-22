package com.weixin.service.impl;

import com.weixin.dao.ConfigDao;
import com.weixin.model.Config;
import com.weixin.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/13.
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public Map<String, Object> getBySiteId(Integer siteId) {
        return configDao.getBySiteId(siteId);
    }

    @Override
    public void update(Integer siteId, String appID, String appsecret) {
        Config config = configDao.get(siteId);
        if(config == null){
            config = new Config();
            config.setSiteId(siteId);
            config.setAppID(appID);
            config.setAppsecret(appsecret);
            configDao.save(config);
        }else{
            config.setAppID(appID);
            config.setAppsecret(appsecret);
        }
    }

    @Override
    public Config getObjBySiteId(Integer siteId) {
        return configDao.get(siteId);
    }
}
