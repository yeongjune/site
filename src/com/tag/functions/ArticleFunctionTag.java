package com.tag.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.base.util.DataUtil;
import com.base.util.JSONUtil;
import com.base.util.ListUtils;
import com.base.util.StringUtil;
import com.site.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.site.service.ArticleService;
import com.site.vo.ArticleSearchVo;

@Component
public class ArticleFunctionTag {
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private ImageService imageService;

	private static ArticleFunctionTag articleFunctionTag;
	
	@PostConstruct
	public void init(){
		articleFunctionTag = this;
		articleFunctionTag.articleService = this.articleService;
		articleFunctionTag.imageService = this.imageService;
	}
	
	/**
	 * 统计改网站用户发表新闻数排名
	 * @param siteId 网站ID
	 * @param limitUserNum 取出条数
	 * @return
	 */
	@SuppressWarnings("unused")
	public static List<Map<String, Object>> getUserArticleStatistics(Integer siteId,
			Integer limitUserNum) {
		List<Map<String, Object>> list = articleFunctionTag.articleService.statisticsUserArticle(siteId, limitUserNum);
		return articleFunctionTag.articleService.statisticsUserArticle(siteId, limitUserNum);
	}

	/**
	 * 统计改网站用户发表新闻数排名
	 * @param siteId 网站ID
	 * @param limitUserNum 取出条数
	 * @return
	 */
	public static List<Map<String, Object>> getColumnArticleStatistics(Integer siteId,
			Integer limitUserNum) {
		return articleFunctionTag.articleService.statisticsColumnArticle(siteId, limitUserNum);
	}
	
	/**
	 * 获取新闻数据
	 * @param siteId 网站id
	 * @param columnId 栏目id
	 * @return
	 */
	public static List<Map<String, Object>> getArticleListByColumnId(Integer siteId, Integer columnId, Integer limit){
		ArticleSearchVo searchVo = new ArticleSearchVo();
		if( limit != null ){
			searchVo.setLimit(limit);
		}
		searchVo.setSiteId(siteId);
		searchVo.setColumnId(columnId);
		searchVo.setIncludeSub(true);
		return transform(articleFunctionTag.articleService.findArticleList(searchVo));
	}

	/**
	 * 获取栏目新闻数据，不加载子栏目数据
	 * @param siteId 网站id
	 * @param columnId 栏目id
	 * @return
	 */
	public static List<Map<String, Object>> getSingleArticleListByColumnId(Integer siteId, Integer columnId, Integer limit){
		ArticleSearchVo searchVo = new ArticleSearchVo();
		if( limit != null ){
			searchVo.setLimit(limit);
		}
		searchVo.setSiteId(siteId);
		searchVo.setColumnId(columnId);
		searchVo.setIncludeSub(false);
		return transform(articleFunctionTag.articleService.findArticleList(searchVo));
	}

	private static List<Map<String, Object>> transform(List<Map<String, Object>> listMap){
		if (listMap!=null&&listMap.size()>0) {
			List<Integer> ids = new ArrayList<Integer>();
			for (Map<String, Object> map : listMap)
				ids.add((Integer) map.get("id"));
			List<Map<String, Object>> imageList = articleFunctionTag.imageService.findList(ids, 1,null, null);
			Map<Object, List<Map<String, Object>>> imageLists = ListUtils.classifyMapList("articleId", imageList);
			List<Map<String, Object>> fileList = articleFunctionTag.imageService.findList(ids, 3,null, null);
			Map<Object, List<Map<String, Object>>> fileLists = ListUtils.classifyMapList("articleId", fileList);
			for (int i=0;i<listMap.size();i++) {
				Map<String, Object> articleMap = listMap.get(i);
				Integer id=Integer.parseInt(articleMap.get("id")+"");
				articleMap.put("imageList", imageLists.get(id));//图片类型的文章图片列表
				articleMap.put("fileList", fileLists.get(id)); //附件列表
				articleMap.put("videoList", DataUtil.findArticleFileList(articleMap.get("content") + "")); //新闻内容包含的视频列表
				// 转化附加信息为map
				String extra = (String) articleMap.get("extra");
				if(StringUtil.isNotEmpty(extra))
					articleMap.put("extra", JSONUtil.getJSONField(extra));
			}
		}
		return listMap;
	}

}
