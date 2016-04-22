package com.weixin.service;

import com.weixin.model.WeiXinMenu;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/12.
 */
public interface MenuService {

    /**
     * 获取网站所有的微信菜单配置信息
     * @param siteId
     * @return
     */
    List<Map<String,Object>> getAll(Integer siteId);

    /**
     * 保存
     * @param menu
     */
    void save(WeiXinMenu menu);

    /**
     * 删除菜单配置，以及子级菜单
     * @param id
     */
    void delete(Integer id);

    Integer getMenuCountByPid(Integer siteId, Integer pid);

    WeiXinMenu get(Integer id);

    WeiXinMenu get(Integer siteId, Integer id);

    void update(WeiXinMenu menu);

    int updateSort(Integer id, Integer tarId, Integer type, Integer siteId);
}
