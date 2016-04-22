package com.tejiao.dao;

import com.base.vo.PageList;
import com.tejiao.model.School;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-29.
 */
public interface SchoolDao {
    void save(School school);

    PageList getPageList(Integer currentPage, Integer pageSize, Integer siteId, String keyword);

    School get(Integer id);

    void update(School school);

    int delete(Integer id, Integer siteId);

    List<Map<String,Object>> getBySiteId(Integer siteId);
    /**批量保存学校
     * @param saveList
     * @return
     */
    int saveAll(List<Map<String, Object>> saveList);
    /**
     * 获取
     * @param code
     * @param siteId
     * @return
     */
    School getByCode(String code, Integer siteId);

    /**
     * 获取所有镇街
     * @param siteId
     * @return
     */
    List<Map<String,Object>> getAllTown(Integer siteId);
}
