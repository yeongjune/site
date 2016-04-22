package com.weixin.service;

import com.base.vo.PageList;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/16.
 */
public interface MediaService {

    /**
     * 获取所有素材类型
     * @param siteId
     * @return
     */
    List<Map<String,Object>> getAllType(Integer siteId);

    /**
     * 获取素材列表
     * @param siteId 网站id
     * @param type 素材类型
     * @param keyword 搜索
     * @return
     */
    PageList getPageList(Integer siteId, Integer type, String keyword);
}
