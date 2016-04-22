package com.site.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.base.vo.PageList;
import com.site.model.Article;
import com.site.model.ArticleExtra;
import com.site.vo.ArticleSearchVo;

/**
 * 新闻Article的Dao接口
 * @author lifaqiang
 *
 * 2014-3-14
 */
public interface ArticleDao {
	/**
	 * 保存新增的新闻
	 * @param map	article
	 * @return
	 */
	Serializable save(Map<String, Object> article);

	/**
	 *根据ID 批量删除新闻
	 * @param ids 	新闻ID,多个时用逗号隔开
	 * @return		
	 */
	int delete(String ids);

	/**
	 * 修改一个新闻信息
	 * @param article  
	 * @return
	 */
	int update(Map<String, Object> article);
	
	/**
	 * 根据ID获取新闻信息
	 *（该方法返回文章的信息包括  推荐列表、图片，如不需要这些数据请使用ge方法只返回文章表数据）
	 * @param id  
	 * @return
	 */
	Map<String, Object> load(Integer id);
	
	/**
	 * 根据ID只返回新闻表数据，
	 * 如需要包括（新闻推荐、新闻图片的请使用load方法）
	 * @author lifq
	 * @param id
	 * @return
	 */
	Article get(Integer id);
	
	/**
	 * 查询新闻
	 * @author lifqiang
	 * @param searchVo  参数Vo
	 * @return
	 */
	List<Map<String, Object>>	findArticleList(ArticleSearchVo searchVo);
	
	/**
	 * 分页查询新闻
	 * @author lifqiang
	 * @param currentPage 页数
	 * @param pageSize	   每页条数
	 * @param searchVo	参数Vo
	 * @param isSys 是否为后台加载
	 * @return
	 */
	PageList findArticlePageList(Integer currentPage,Integer pageSize,ArticleSearchVo searchVo, boolean isSys);
	
	List<Map<String, Object>> getList(Integer columnId, Integer size,Integer sortType, Integer flag);

	PageList getPageList(Integer columnId, Integer currentPage,Integer size, Integer sortType, Integer flag);

	/**
	 * 删除指定站点所有新闻数据
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);
	
	/**
	 * 获取文章数量
	 * @author lifq
	 * @param siteId  站点ID，可以不传
	 * @param columnId 栏目ID，可以不传
	 * @return
	 */
	int getArticleCount(Integer siteId,Integer columnId);
	
	/**
	 * 获取文章的上一编和下一编文章ID
	 * @author lifq
	 * @param articleId 当前文章ID
	 * @return  map {pre:id,next:id}
	 */
	Map<String, Object> findNextAndPre(Integer articleId);
	
	/**
	 * 根据文章Id获取文章的tempId
	 * @author lifq
	 * @param articleIds 文章ID，多个用逗号隔开
	 * @return
	 */
	List<String> getTempIdByArticleIds(String  articleIds);
	

	/**
	 * 批量修改文章的所属栏目
	 * @author lifq
	 * @param columnId  修改为的 栏目ID
	 * @param articleIds 文章ID，多个用逗号隔开
	 * @return
	 */
	int updateColumn(Integer columnId,String articleIds);
	
	/**
	 * 更新文章的最后修改时间
	 * @author lifq
	 * @param id 文章ID
	 * @param flag 更新标识：1、上移一条;2、下移一条;3、置顶;4、置尾
	 * @return
	 */
	int updateArticleSort(Integer id,Integer flag);
	
	/**
	 * 设置文章投票数, 自增一
	 * @param id 文章ID
	 * @param clickCount 投票数
	 * @return
	 */
	int updateArticleClickCount(Integer id);
	
	/**
	 * 批量更新文章投票数, 每篇自增一票
	 * @param articleIds
	 * @return
	 */
	int batchUpdateArticleClickCount(String articleIds);
	
	/**
	 * 设置文章浏览数
	 * @param id 文章ID
	 * @param viewCount
	 * @return
	 */
	int updateViewCount(Integer id, Long viewCount);
	
	/**
	 * 设定投票文章的截止日期
	 * @param ids
	 * @param lastTime
	 * @return
	 */
	int updateLastTime(String ids, String lastTime);
	
	/**
	 * 设置文章的新旧状态
	 * @param ids
	 * @param status
	 * @return
	 */
	int updateStatus(String ids, String status);
	
	/**
	 * 统计改网站用户发表新闻数排名
	 * @param siteId 网站ID
	 * @param limitUserNum 取出条数
	 * @return
	 */
	List<Map<String, Object>> statisticsUserArticle(Integer siteId, Integer limitUserNum);
	
	/**
	 * 统计改网站用户发表新闻数排名
	 * @param siteId 网站ID
	 * @param limitUserNum 取出条数
	 * @return
	 */
	List<Map<String, Object>> statisticsColumnArticle(Integer siteId, Integer limitUserNum);

    List<ArticleExtra> getArticleExtraList(Integer siteId);

    void saveArticleExtraList(List<Map<String,Object>> extraList);

    /**
     * 删除全部新闻附加信息配置
     * @param siteId
     */
    void deleteAllArticleExtra(Integer siteId);

    List<Map<String,Object>> getArticleExtraListMap(Integer siteId);
}
