package com.site.dao;


/**
 * 统计数据DAO
 * @author Administrator
 *
 */
public interface StatisticsDao {

	/**
	 * 保存或更新页面访问量信息
	 */
	void saveOrUpdatePageView(Integer siteId);
	
	/**
	 * 根据站点ID查询页面访问量
	 * @param siteId
	 * @return
	 */
	Integer findPageView(Integer siteId);
}
