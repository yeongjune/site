package com.site.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.site.model.ArticleClickCount;

/**
 * 新闻点击投票记录
 * @author Administrator
 *
 */
public interface ArticleClickCountDao {

	/**
	 * 保存一条点击关系记录
	 * @param articleClickCount 
	 * @return
	 */
	Serializable save(ArticleClickCount articleClickCount);
	
	/**
	 * 根据文章id和用户id查找对应的点击记录
	 * @param articleId 文章ID
	 * @param userId 用户ID
	 * @return
	 */
	Integer checkClickCount(Integer articleId, Integer userId);
	
	/**
	 * 查询某栏目下投票总数
	 * @param columnId
	 * @return
	 */
	int checkTotalCount(Integer columnId);
	
	/**
	 * 获取某用户某栏目下的已投票文章ID列表
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
	 * @return
	 */
	int batchSave(Integer columnId, Integer userId, String articleIds,String mobilePhone);
	/**
	 * 
	 * @param mobileNo 手机号码
	 * @param columnId 所属栏目Id
	 * @return
	 */
	int checkExistsMobile(String mobileNo,int columnId);
}
