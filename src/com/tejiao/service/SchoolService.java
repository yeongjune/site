package com.tejiao.service;

import com.base.vo.PageList;
import com.tejiao.model.School;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-29.
 */
public interface SchoolService {
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
     * 根据学校代号获取
     * @param code 学校代号
     * @param siteId 网站id
     * @return
     */
    School getByCode(String code, Integer siteId);

    /**
     * 获取学校的镇街列表
     * @param siteId
     * @return
     */
    List<Map<String,Object>> getAllTown(Integer siteId);
}
