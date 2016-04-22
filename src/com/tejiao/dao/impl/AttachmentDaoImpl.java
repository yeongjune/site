package com.tejiao.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.tejiao.dao.AttachmentDao;
import com.tejiao.model.Attachment;
import com.tejiao.model.Declare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzf on 2016/2/24.
 */
@Repository
public class AttachmentDaoImpl implements AttachmentDao {

    @Autowired
    private SQLDao sqlDao;

    @Autowired
    private HQLDao hqlDao;

    @Override
    public void save(Attachment attachment) {
        hqlDao.save(attachment);
    }

    @Override
    public PageList getWebPageList(Integer currentPage, Integer pageSize, Integer siteId, Integer declareId, String keyword) {
        List<Object> params = new ArrayList<>();
        String sql = "SELECT  " +
                "  a.*, " +
                "  d.name AS declareName  " +
                " FROM " +
                Attachment.tableName + " a  " +
                "  LEFT JOIN " + Declare.tableName + " d  " +
                "    ON ( a.declareId = d.id AND d.siteId = ? ) " +
                " WHERE a.siteId = ? ";
        params.add(siteId);
        params.add(siteId);
        if(declareId != null){
            sql += " AND a.declareId = ? ";
            params.add(declareId);
        }
        if(StringUtil.isNotEmpty(keyword)){
            sql += " AND ( a.name LIKE ? OR d.name LIKE ? OR d.title LIKE ? ) ";
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        sql += " order by a.date desc";
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public PageList getPageList(Integer currentPage, Integer pageSize, Integer siteId, Integer schoolId, String keyword) {
        List<Object> params = new ArrayList<>();
        String sql = "SELECT  " +
                "  a.*, " +
                "  d.name AS declareName  " +
                " FROM " +
                Attachment.tableName + " a  " +
                "  LEFT JOIN " + Declare.tableName + " d  " +
                "    ON ( a.declareId = d.id AND d.siteId = ? ) " +
                " WHERE a.siteId = ? ";
        params.add(siteId);
        params.add(siteId);
        if(schoolId != null){
            sql += " AND d.schoolId = ? ";
            params.add(schoolId);
        }
        if(StringUtil.isNotEmpty(keyword)){
            sql += " AND ( a.name LIKE ? OR d.name LIKE ? OR d.title LIKE ? ) ";
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public int delete(Integer siteId, Integer id) {
        String sql = "DELETE FROM " + Attachment.tableName + " WHERE id = ? AND siteId = ? ";
        return sqlDao.update(sql, id, siteId);
    }

}
