package com.site.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 用户投票记录
 * @author Administrator
 *
 */
public interface ArticleClickCountService {
	
	/**
	 * 保存用户投票记录
	 * @param articleId 文章ID
	 * @param userId
	 * @param count 文章点击数
	 * @return
	 */
	int save(Integer articleId, Integer userId);
	

	/**
	 * 查询某栏目下的投票总数
	 * @param columnId 栏目ID
	 * @return
	 */
	int checkTotalCount(Integer columnId);
	
	/**
	 * 获取某用户某栏目下已经进行投票的文章ID
	 * @param columnId
	 * @param userId
	 * @return
	 */
	List<Map<String,Object>> getClickedOption(Integer columnId, Integer userId);
	
	/**
	 * 批量保存投票记录
	 * @param columnId
	 * @param userId
	 * @param articleIds
	 * @param mobilePhone
	 * @return
	 */
	int updateClickCountBatch(Integer columnId, Integer userId, String articleIds,String mobilePhone);
	/**
	 * 
	 * @param mobileNo  手机号码
	 * @param captcha 验证码
	 * @param siteId  网站Id
	 * @return
	 */
	void saveMobileNo(String mobileNo,String captcha,Integer siteId) ;
	/**
	 * 
	 * @param mobileNo 手机号码
	 * @param columnId 所属栏目Id
	 * @return
	 */
	int checkExistsMobile(String mobileNo,int columnId);
}
