package com.site.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.base.vo.PageList;
import com.site.model.Article;
import com.site.model.ArticleExtra;
import com.site.vo.ArticleSearchVo;

/**
 * 新闻Service接口
 * @author lifaqiang
 *
 * 2014-3-14
 */
public interface ArticleService {
	/**
	 * 保存新增的新闻
	 * @param map	article
	 * @return
	 */
	Serializable save(Map<String, Object> article);
	
	
	/**
	 * 保存新闻信息（包括新闻内容，推荐，图片）
	 * @author lifq
	 * @param article
	 * @param remmendIds 推荐ID，多个用逗号隔开
	 * @param imageIds 图片id,多个用逗号隔开
	 * @param request
	 * @return
	 */
	Serializable save(Map<String, Object> article,String remmendIds,String imageIds,HttpServletRequest request);
	

	/**
	 * 批量删除新闻
	 *（注意，该方法会连新闻关联的推荐和图片记录以及图片附件一起删除）
	 * @param ids 	新闻ID，多个用逗号隔开
	 * @param request 传入该参数时会连附件一起删除掉，传入null不删除附件
	 * @return		
	 */
	int delete(String ids,HttpServletRequest request);
	
	/**
	 * 修改一个新闻信息
	 * @param article  
	 * @return
	 */
	int update(Map<String, Object> article);
	
	/**
	 * 修改一个新闻信息以及推荐
	 * @author lifq
	 * @param article 文章
	 * @param remmendIds 文章推荐ID，多个逗号隔开
	 * @param imageIds 图片id,多个逗号隔开
	 * @return
	 */
	int update(Map<String, Object> article,String remmendIds,String imageIds);
	/**
	 * 根据ID获取新闻信息
	 * （该方法返回文章的信息包括  推荐列表、图片，如不需要这些数据请使用ge方法只返回文章表数据）
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
	
	/**
	 * 获取文章数量
	 * @author lifq
	 * @param siteId  站点ID，可以不传
	 * @param columnId 栏目ID，可以不传
	 * @return
	 */
	int getArticleCount(Integer siteId,Integer columnId);
	
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
	 * 设置文章的浏览次数
	 * @param id
	 * @param viewCount
	 * @return
	 */
	int updateviewCount(Integer id, Long viewCount);
	
	/**
	 * 设定投票新闻的截止时间
	 * @param ids
	 * @param lastTime
	 * @return
	 */
	int updateLastTime(String ids, String lastTime);
	
	/**
	 * 修改新闻的新旧状态
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

	/**
	 * 更改新闻审核状态
	 * @param id
	 * @param state true为通过
	 * @return
	 */
	int updateCheckedStatus(Integer id, boolean state);

    /**
     * 获取新闻附加信息 自定义配置
     * @param siteId
     * @return
     */
    List<ArticleExtra> getArticleExtraList(Integer siteId);

    void saveArticleExtraList(Integer siteId, List<Map<String, Object>> extraList);

    List<Map<String, Object>> getArticleExtraListMap(Integer siteId);
}
