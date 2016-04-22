package com.weixin.dao;

import com.weixin.model.WeiXinMenu;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/12.
 */
public interface MenuDao {

    /**
     * 获取网站所有的微信菜单配置
     * @param siteId
     * @return
     */
    List<Map<String,Object>> getAll(Integer siteId);

    /**
     * 获取当前同级菜单最大排序
     * @param siteId
     * @param pid
     * @return
     */
    Integer getMaxSort(Integer siteId, Integer pid);

    void save(WeiXinMenu menu);

    void delete(Integer id);

    Integer getMenuCountByPid(Integer siteId, Integer pid);

    WeiXinMenu get(Integer id);

    void update(WeiXinMenu menu);

    Map<String,Object> getMap(Integer siteId, Integer id);

    int moveToParent(Integer id, Integer tarId, int maxSort, Integer siteId);

    void sortPlusOne(Integer pid, Integer sort, Integer siteId);

    int update(Map<String, Object> child);
}
