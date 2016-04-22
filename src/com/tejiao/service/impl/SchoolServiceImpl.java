package com.tejiao.service.impl;

import com.base.vo.PageList;
import com.tejiao.dao.SchoolDao;
import com.tejiao.model.School;
import com.tejiao.service.SchoolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-29.
 */
@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolDao schoolDao;

    @Override
    public void save(School school) {
        schoolDao.save(school);
    }
    @Override
    public int saveAll(List<Map<String, Object>> saveList) {
        return schoolDao.saveAll(saveList);
    }
    @Override
    public PageList getPageList(Integer currentPage, Integer pageSize, Integer siteId, String keyword) {
        return schoolDao.getPageList(currentPage, pageSize, siteId, keyword);
    }

    @Override
    public School get(Integer id) {
        return schoolDao.get(id);
    }

    @Override
    public void update(School school) {
        schoolDao.update(school);
    }

    @Override
    public int delete(Integer id, Integer siteId) {
        return schoolDao.delete(id, siteId);
    }

    @Override
    public List<Map<String, Object>> getBySiteId(Integer siteId) {
        return schoolDao.getBySiteId(siteId);
    }

    @Override
    public School getByCode(String code, Integer siteId) {
        return schoolDao.getByCode(code, siteId);
    }

    @Override
    public List<Map<String, Object>> getAllTown(Integer siteId) {
        return schoolDao.getAllTown(siteId);
    }
}
