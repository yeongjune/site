package com.authority.service;

import java.io.Serializable;
import java.util.Map;

public interface UserService {

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
	 * @param account
	 * @param name
	 * @param siteId
	 * @return
	 */
	Serializable save(String account, String name, Integer siteId);

	/**
	 * 删除一个用户
	 * @param id
	 * @param siteId 
	 * @return
	 */
	int delete(Integer id, Integer siteId);

	/**
	 * 修改一个用户信息
	 * @param id
	 * @param name
	 * @return
	 */
	int update(Integer id, String name);

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
	 * 启用或禁用指定站点指定帐号
	 * @param siteId
	 * @param id
	 * @param status
	 * @return
	 */
	int updateEnable(Integer siteId, Integer id, Integer status);

	/**
	 * 获得指定站点管理员密码
	 * @param siteId
	 * @return
	 */
	String getAdminPassword(Integer siteId);

	/**
	 * 重置改ID管理员的密码
	 * @param id
	 * @return
	 */
	int resetPwd(Integer id);

    /**
     * 根据站点id，帐号,查询用户数据，用于登录
     * @param siteId
     * @param account
     * @return {id,account,password,name,siteId,enable,createTime,updateTime}
     */
    Map<String, Object> getUserById(Integer siteId, String account);
}
