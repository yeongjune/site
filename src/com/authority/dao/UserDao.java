package com.authority.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface UserDao {

	/**
	 * 根据站点id，帐号、密码查询用户数据，用于登录
	 * @param siteId
	 * @param account
	 * @param password
	 * @return {id,account,password,name,siteId,enable,createTime,updateTime}
	 */
	Map<String, Object> getUserByIdAndPassword(Integer siteId, String account, String password);

	/**
	 * 保存一个新增的用户
	 * @param map {account,name,siteId}
	 * @return
	 */
	Serializable save(Map<String, Object> map);

	/**
	 * 删除一个用户
	 * @param id
	 * @param siteId 
	 * @return
	 */
	int delete(Integer id, Integer siteId);

	/**
	 * 修改一个用户信息
	 * @param map {id,name}
	 * @return
	 */
	int update(Map<String, Object> map);

	/**
	 * 按站点id分页查询用户数据
	 * @param siteId
	 * @param currentPage
	 * @param pageSize
	 * @param keyword
	 * @return {currentPage, pageSize, totalPage, count, list=[{id,account,password,name,siteId,enable,createTime,updateTime}]}
	 */
	Map<String, Object> getListByPage(Integer siteId, Integer currentPage,
			Integer pageSize, String keyword);

	/**
	 * 查询一个用户数据
	 * @param id
	 * @return {id,account,password,name,siteId,enable,createTime,updateTime}
	 */
	Map<String, Object> load(Integer id);

	/**
	 * 查询指定站点的指定帐号数量，用于判断重复帐号
	 * @param siteId
	 * @param account
	 * @return
	 */
	long countByAccount(Integer siteId, String account);

	/**
	 * 查询全站指定帐号数量，用户判断某帐号是否存在
	 * @param account
	 * @return
	 */
	long countByAccount(String account);

	/**
	 * 启用或禁用指定站点指定帐号
	 * @param siteId
	 * @param id
	 * @param status
	 * @return
	 */
	int updateEnable(Integer siteId, Integer id, Integer status);

	/**
	 * 查询指定站点的所有用户
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> getList(Integer siteId);

	/**
	 * 获得指定网站管理员密码
	 * @param siteId
	 * @return
	 */
	String getAdminPassword(Integer siteId);

	/**
	 * 查询指定网站管理员帐号信息
	 * @param siteId
	 * @return
	 */
	Map<String, Object> getAdmin(Integer siteId);

	/**
	 * 修改指定网站指定用户的密码
	 * @param siteId
	 * @param userId
	 * @param password
	 * @param newPassword
	 * @return
	 */
	int updatePassword(Integer siteId, Integer userId, String password,
			String newPassword);

	/**
	 * 删除指定站点下所有用户
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);

    /**
     * 根据站点id，帐号查询用户数据，用于登录
     * @param siteId
     * @param account
     * @return {id,account,password,name,siteId,enable,createTime,updateTime}
     */
    Map<String, Object> getUserById(Integer siteId, String account);

}
