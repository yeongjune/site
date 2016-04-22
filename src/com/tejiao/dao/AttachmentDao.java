package com.tejiao.dao;

import com.base.vo.PageList;
import com.tejiao.model.Attachment;

/**
 * Created by dzf on 2016/2/24.
 */
public interface AttachmentDao {
    /**
     * 保存对象
     * @param attachment
     */
    void save(Attachment attachment);

    /**
     * 获取前端学生档案
     * @param siteId
     * @param declareId
     * @param keyword
     * @return
     */
    PageList getWebPageList(Integer currentPage, Integer pageSize, Integer siteId, Integer declareId, String keyword);

    /**
     * 获取后台学生档案
     * @param siteId
     * @param schoolId
     * @param keyword
     * @return
     */
    PageList getPageList(Integer currentPage, Integer pageSize, Integer siteId, Integer schoolId, String keyword);

    /**
     * 删除
     * @param siteId
     * @param id
     * @return
     */
    int delete(Integer siteId, Integer id);
}
