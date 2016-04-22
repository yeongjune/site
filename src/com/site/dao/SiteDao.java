package com.site.dao;

import com.site.model.MsgConfig;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface SiteDao {

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
	 * @param map {name:, domain:}
	 * @return
	 */
	Serializable save(Map<String, Object> map);

	/**
	 * 删除一个站点
	 * @param id
	 * @return
	 */
	int delete(Integer id);

	/**
	 * 修改站点信息
	 * @param map {id, name, domain}
	 * @return
	 */
	int update(Map<String, Object> map);

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
	 * 查询指定网站名称
	 * @param siteId
	 * @return
	 */
	String getName(Integer siteId);

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
     * 获取通知短信配置信息
     * @param siteId
     * @return
     */
    MsgConfig getSMSConfig(Integer siteId);

    /**
     * 保存通知短信配置消息
     * @param config
     */
    void saveSMSConfig(MsgConfig config);
}
