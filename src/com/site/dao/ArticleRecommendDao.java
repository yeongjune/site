package com.site.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.site.model.ArticleRecommend;

/**
 * @author lfq
 *
 */
public interface ArticleRecommendDao {
	
	/**
	 * 保存文章推荐记录
	 * @param articleRecommend
	 * @return
	 */
	Serializable save(ArticleRecommend articleRecommend);

	/**
	 * 删除文章推荐记录
	 * @param id 	
	 * @return		
	 */
	int delete(Integer id);
	
	
	/**
	 * 根据文章Id删除文章推荐记录
	 * @param ArticleIds 文章ID，多个用逗号隔开 	
	 * @return		
	 */
	int deleteByArticleId(String  ArticleIds);
	
	
	/**
	 * 根据推荐Id删除文章推荐记录
	 * @param rcommendId 	
	 * @return		
	 */
	int deleteByRcommendId(Integer rcommendId);
	
	
	/**
	 * 根据id获取文章推荐记录
	 * @param id  
	 * @return
	 */
	Map<String, Object> load(Integer id);
	
	/**
	 * 查询推荐列表
	 * @param articleId 文章ID
	 * @param recommendId	推荐ID
	 * @return
	 */
	List<Map<String, Object>> findList(Integer articleId,Integer recommendId);
}
