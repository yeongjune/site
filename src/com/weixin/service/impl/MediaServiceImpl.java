package com.weixin.service.impl;

import com.base.vo.PageList;
import com.weixin.dao.MediaDao;
import com.weixin.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/16.
 */
@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaDao mediaDao;

    @Override
    public List<Map<String, Object>> getAllType(Integer siteId) {
        return mediaDao.getAllType(siteId);
    }

    @Override
    public PageList getPageList(Integer siteId, Integer type, String keyword) {
        return null;
    }
}
