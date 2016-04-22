package com.tejiao.service;

import com.base.vo.PageList;
import com.tejiao.model.Parent;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
public interface ParentService {
    /**
     * 根据账号获取家长用户
     * @param account
     * @param siteId
     * @return
     */
    Parent getByAccount(String account, Integer siteId);

    void save(Parent parent);

    /**
     * 获得当前登录的家长用户
     * @param session
     * @return
     */
    Parent getCurrent(HttpSession session);

    void setCurrent(HttpSession session, Parent parent);

    void update(Parent parent);

    /**
     * 获取该网站所有的用户名
     * @param siteId
     * @return
     */
    List<String> getAccountList(Integer siteId);

    /**
     * 保存多个
     * @param saveList
     * @return
     */
    int saveAll(List<Map<String,Object>> saveList);

    /**
     * 加载列表
     * @param siteId
     * @return
     */
    PageList getPageList(Integer currentPage, Integer pageSize, String keyword, Integer siteId);

    /**
     * 更改密码
     * @param id
     * @param siteId
     * @param password
     * @return
     */
    int updatePassword(Integer id, Integer siteId, String password);

    Parent get(Integer id);
}
