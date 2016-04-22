package com.site.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.base.vo.PageList;
import com.site.model.Site;
import com.site.model.SiteUser;
import com.site.vo.SiteUserSearchVo;

public interface SiteUserDao {
	
	Serializable save(SiteUser siteUser);
	
	Serializable save(Map<String, Object> siteUser);
	
	/**
	 * 批量保存
	 * @param siteUserList
	 * @return
	 */
	int saveBatch(List<Map<String, Object>> siteUserList);
	
	int update(SiteUser siteUser);
	
	int update(Map<String, Object> siteUser);
	
	int delete(Integer ...id);
	
	SiteUser get(Integer id);
	
	Map<String, Object> load(Integer id);
	
	List<Map<String, Object>> findSiteUserList(SiteUserSearchVo searchVo,Integer start,Integer limit);
	
	PageList findSiteUserPageList(SiteUserSearchVo searchVo,Integer pageSize,Integer currentPage);
	
	/**
	 * 检查当前ID用户的密码是否与传入值对应
	 * @param id
	 * @param password
	 * @return
	 */
	int checkPassword(Integer id, String password);
	
	/**
	 * 检查站点下是否有对应帐号
	 * @param siteId
	 * @param account
	 * @return
	 */
	int checkAccount(Integer siteId, String account);
	
	/**
	 * 查询部门类型
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> findDepartmentTypes(Integer siteId);
}
