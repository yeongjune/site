package com.site.service;

import com.site.model.MsgConfig;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SiteService {

	/**
	 * 查询指定域名的站点id
	 * @param domain
	 * @return
	 */
	Integer getSiteId(String domain);

	/**
	 * 分页查询站点信息
	 * @param currentPage
	 * @param pageSize
	 * @param keyword
	 * @return {currentPage,pageSize,totalPage,count,list=[{id,name,domain,createTime,updateTime}]}
	 */
	Map<String, Object> getListByPage(Integer currentPage, Integer pageSize,
			String keyword);

	/**
	 * 保存一个新增的站点
	 * @param name
	 * @param domain
	 * @param directory 
	 * @return
	 */
	Serializable save(String name, String domain, String directory);

	/**
	 * 删除一个站点
	 * @param id
	 * @return
	 */
	int delete(Integer id);

	/**
	 * 修改站点
	 * @param id
	 * @param name
	 * @param domain
	 * @param directory 
	 * @param isUseCheck 
	 * @return
	 */
	int update(Integer id, String name, String domain, String directory, String isUseCheck);

	/**
	 * 查询一个站点信息
	 * @param id
	 * @return
	 */
	Map<String, Object> load(Integer id);

	/**
	 * 查询域名存在的记录数（排除自身以外）
	 * @param id
	 * @param domain
	 * @return
	 */
	long countByDomainWithSelf(Integer id, String domain);

	/**
	 * 查询域名存在的记录数
	 * @param domain
	 * @return
	 */
	long countByDomain(String domain);

	/**
	 * 查询所有站点数据
	 * @return
	 */
	List<Map<String, Object>> getList();

	/**
	 * 修改开启状态
	 * @param id
	 * @param status
	 * @return
	 */
	int updateOpen(Integer id, Integer status);

	/**
	 * 查询所有站点网址及id
	 * @return {domain:id}
	 */
	Map<String, Integer> getSiteDomain();

	/**
	 * 查询页面文件目录存在的记录数（排除自身以外）
	 * @param id
	 * @param directory
	 * @return
	 */
	long countByDirectoryWithSelf(Integer id, String directory);

	/**
	 * 查询页面文件目录存在的记录数
	 * @param directory
	 * @return
	 */
	long countByDirectory(String directory);
	
	/**
	 * 修改站点信息
	 * @param map
	 * @return
	 */
	int update(Map<String, Object> map);
	
	/**
	 * 取得当前网站的session内的siteId,网站的的信息存放在session的key=site
	 * 获取域名，如果没有www开头查找不到数据 则添加www开头再查一次，不能/结尾
	 * @param request
	 * @return
	 */
	Integer getSiteId(HttpServletRequest request);

    /**
     * 保存或更新 通知信息配置的手机号码项
     * @param siteId
     * @param phone
     */
    void saveOrUpdateSMSConfigPhone(Integer siteId, String phone);

    /**
     * 获取通知消息配置
     * @param siteId
     * @return
     */
    MsgConfig getSMSConfig(Integer siteId);

    /**
     * 保存或更新 通知短信配置
     * @param siteId
     * @param number 剩余短信条数
     * @param content 默认短息内容
     */
    void saveOrUpdateSMSConfig(Integer siteId, Integer number, String content);
}
