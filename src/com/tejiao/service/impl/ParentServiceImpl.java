package com.tejiao.service.impl;

import com.base.vo.PageList;
import com.tejiao.dao.ParentDao;
import com.tejiao.model.Parent;
import com.tejiao.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    private ParentDao parentDao;

    @Override
    public Parent getByAccount(String account, Integer siteId) {
        return parentDao.getByAccount(account, siteId);
    }

    @Override
    public void save(Parent parent) {
        parentDao.save(parent);
    }

    @Override
    public Parent getCurrent(HttpSession session) {
        return (Parent) session.getAttribute("TeJiaoParent");
    }

    @Override
    public void setCurrent(HttpSession session, Parent parent) {
        session.setAttribute("TeJiaoParent", parent);
    }

    @Override
    public void update(Parent parent) {
        parentDao.update(parent);
    }

    @Override
    public List<String> getAccountList(Integer siteId) {
        return parentDao.getAccountList(siteId);
    }

    @Override
    public int saveAll(List<Map<String, Object>> saveList) {
        return parentDao.saveAll(saveList);
    }

    @Override
    public PageList getPageList(Integer currentPage, Integer pageSize, String keyword, Integer siteId) {
        return parentDao.getPageList(currentPage, pageSize, keyword, siteId);
    }

    @Override
    public int updatePassword(Integer id, Integer siteId, String password) {
        return parentDao.updatePassword(id, siteId, password);
    }

    @Override
    public Parent get(Integer id) {
        return parentDao.get(id);
    }

}
