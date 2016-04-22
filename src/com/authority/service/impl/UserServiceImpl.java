package com.authority.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.UserDao;
import com.authority.dao.UserRoleDao;
import com.authority.service.UserService;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.site.dao.SiteDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private SiteDao siteDao;

	@Override
	public Map<String, Object> getUserByIdAndPassword(Integer siteId, String account, String password) {
		Map<String, Object> user = dao.getUserByIdAndPassword(siteId, account, password);
		if(user!=null){
			if(siteId>0){
				Map<String, Object> site= siteDao.load(siteId);
				String organizationName = siteDao.getName(siteId);
				user.put("siteName", organizationName);
				user.put("siteDomain", site.get("domain"));
			}else{
				user.put("siteName", Init.get("title").get("value"));
			}
		}
		return user;
	}

	@Override
	public Serializable save(String account, String name, Integer siteId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account", account);
		map.put("password", CryptUtil.MD5encrypt("123456"));
		map.put("name", name);
		map.put("siteId", siteId);
		map.put("enable", 1);
		Date d = new Date();
		map.put("createTime", d);
		map.put("updateTime", d);
		return dao.save(map);
	}

	@Override
	public int delete(Integer id, Integer siteId) {
		int i = dao.delete(id, siteId);
		if(i>0){
			userRoleDao.deleteByUserId(id);
		}
		return i;
	}

	@Override
	public int update(Integer id, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("updateTime", new Date());
		return dao.update(map);
	}

	@Override
	public Map<String, Object> getListByPage(Integer siteId,
			Integer currentPage, Integer pageSize, String keyword) {
		return dao.getListByPage(siteId, currentPage, pageSize, keyword);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return dao.load(id);
	}

	@Override
	public long countByAccount(Integer siteId, String account) {
		return dao.countByAccount(siteId, account);
	}

	@Override
	public int updateEnable(Integer siteId, Integer id, Integer status) {
		return dao.updateEnable(siteId, id, status);
	}

	@Override
	public String getAdminPassword(Integer siteId) {
		return dao.getAdminPassword(siteId);
	}
	
	@Override
	public int resetPwd(Integer id){
		Map<String, Object> user = new HashMap<String, Object>();
		user.put("id", id);
		user.put("password", CryptUtil.MD5encrypt("123456"));
		return dao.update(user);
	}

    @Override
    public Map<String, Object> getUserById(Integer siteId, String account) {
        Map<String, Object> user = dao.getUserById(siteId, account);
        if(user!=null){
            if(siteId>0){
                Map<String, Object> site= siteDao.load(siteId);
                String organizationName = siteDao.getName(siteId);
                user.put("siteName", organizationName);
                user.put("siteDomain", site.get("domain"));
            }else{
                user.put("siteName", Init.get("title").get("value"));
            }
        }
        return user;
    }

}
