package com.site.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.site.model.ArticleExtra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.dao.ArticleDao;
import com.site.dao.ArticleRecommendDao;
import com.site.dao.ImageDao;
import com.site.model.Article;
import com.site.model.ArticleRecommend;
import com.site.service.ArticleService;
import com.site.vo.ArticleSearchVo;

/**
 * 文章Service接口的实现类
 * @author lifaqiang
 *
 * 2014-3-14
 */
@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private ArticleRecommendDao articleRecommendDao;
	
	@Autowired
	private ImageDao imageDao;
	
	@Override
	public Serializable save(Map<String, Object> article) {
		return articleDao.save(article);
	}
	

	@Override
	public int delete(String ids,HttpServletRequest request) {
		if (StringUtil.isEmpty(ids)) {
			return 0;
		}
		List<String> tempIdList=articleDao.getTempIdByArticleIds(ids);
		int result=articleDao.delete(ids);//删除掉新闻
		
		//如果删成功则进行新闻关联的信息删除
		if (result>0) {
			if (request != null) {//传入request为null时不删除附件
				//预先存放要删除的文件路径
				List<String> fileList=new ArrayList<String>();
				String root=request.getSession().getServletContext().getRealPath("/");
				//根据tempId完全删除
				for (int i = 0; i < tempIdList.size(); i++) {	
					List<Map<String, Object>> imageList=imageDao.find(null,null, tempIdList.get(i), null);
					if (imageList != null&&imageList.size()>0) {
						for (Map<String, Object> image : imageList) {
							fileList.add(root+image.get("path"));//为了确保删除的记录成功后删除附近，所有不在该处删除删除文件，直到关联的信息删除完后才调用删除文件
						}
					}
				}
				
				imageDao.deleteByArticleId(ids);//删除新闻关联的图片记录
				articleRecommendDao.deleteByArticleId(ids);//删除新闻管理的的推荐记录
				
				//删除关联的附件（包括：新闻缩略图、图片新闻的图片）
				if (fileList.size()>0) {
					for (String filePath : fileList) {
						File file = new File(filePath);
						if (file.exists() && file.isFile() ) {
							file.delete();
						}
					}
				}
			}else{
				imageDao.deleteByArticleId(ids);//删除新闻关联的图片记录
				articleRecommendDao.deleteByArticleId(ids);//删除新闻管理的的推荐记录
			}
		}
		return  result;
	}

	@Override
	public int update(Map<String, Object> article) {
		return articleDao.update(article);
	}

	@Override
	public List<Map<String, Object>> findArticleList(ArticleSearchVo searchVo) {
		return articleDao.findArticleList(searchVo);
	}

	@Override
	public PageList findArticlePageList(Integer currentPage, Integer pageSize,
			ArticleSearchVo searchVo, boolean isSys) {
		return articleDao.findArticlePageList(currentPage, pageSize, searchVo, isSys);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		Map<String, Object> article=articleDao.load(id);
		if (article!=null) {
			article.put("arList", articleRecommendDao.findList(id, null));//文章推荐位置列表
			article.put("imageList", imageDao.find(id, 1,null, null));//图片列表
			article.put("fileList", imageDao.find(id, 3,null, null));//附件列表
		}
		return article;
	}

	@Override
	public Serializable save(Map<String, Object> article, String remmendIds,String imageIds,HttpServletRequest request) {
		Integer id=(Integer) articleDao.save(article);
		if (!StringUtil.isEmpty(remmendIds)) {
			String [] remendIds=remmendIds.split(",");
			for (int i = 0; i < remendIds.length; i++) {
				ArticleRecommend ar=new ArticleRecommend();
				ar.setArticleId(id);
				ar.setRecommendId(Integer.parseInt(remendIds[i]));
				articleRecommendDao.save(ar);
			}
		}
		if (!StringUtil.isEmpty(imageIds)) {//更新图片文章ID
			imageDao.setImageArticleId(id, imageIds);
			//删除没用到的的文件,删除文章的时候统一删除
			/*List<Map<String, Object>> imageList=imageDao.findDisalbeFile(id);
			if (imageList != null && imageList.size()>0) {
				String root=request.getSession().getServletContext().getRealPath("/");
				String ids="";
				for (Map<String, Object> image : imageList) {
					String filePath=root+image.get("path");
					File file=new File(filePath);
					if (file.exists() && file.isFile()) {
						file.delete();
					}
					ids +=image.get("id")+",";
				}
				imageDao.delete(ids);
			}*/
		}
		return id;
	}

	@Override
	public int update(Map<String, Object> article ,String remmendIds,String imageIds) {
		Integer id=Integer.parseInt(article.get("id")+"");
		articleRecommendDao.deleteByArticleId(id+"");//删除之前的推荐信息
		if (!StringUtil.isEmpty(remmendIds)) {
			String [] remendIds=remmendIds.split(",");
			for (int i = 0; i < remendIds.length; i++) {
				ArticleRecommend ar=new ArticleRecommend();
				ar.setArticleId(id);
				ar.setRecommendId(Integer.parseInt(remendIds[i]));
				articleRecommendDao.save(ar);
			}
		}
		if (!StringUtil.isEmpty(imageIds)) {//更新图片文章ID,修改新闻时一般会把image表的articleId一起插入，所有此处可以不调用
			imageDao.setImageArticleId(id, imageIds);
		}
		return articleDao.update(article);
	}

	@Override
	public int getArticleCount(Integer siteId, Integer columnId) {
		return articleDao.getArticleCount(siteId, columnId);
	}

	@Override
	public Article get(Integer id) {
		return articleDao.get(id);
	}


	@Override
	public int updateColumn(Integer columnId, String articleIds) {
		return articleDao.updateColumn(columnId, articleIds);
	}


	@Override
	public int updateArticleSort(Integer id, Integer flag) {
		return articleDao.updateArticleSort(id, flag);
	}
	
	@Override
	public int updateviewCount(Integer id, Long viewCount){
		return articleDao.updateViewCount(id, viewCount);
	}
	
	@Override
	public int updateLastTime(String ids, String lastTime){
		return articleDao.updateLastTime(ids, lastTime);
	}
	
	@Override
	public int updateStatus(String ids, String status){
		return articleDao.updateStatus(ids, status);
	}


	@Override
	public List<Map<String, Object>> statisticsUserArticle(Integer siteId,
			Integer limitUserNum) {
		return articleDao.statisticsUserArticle(siteId, limitUserNum);
	}


	@Override
	public List<Map<String, Object>> statisticsColumnArticle(Integer siteId,
			Integer limitUserNum) {
		return articleDao.statisticsColumnArticle(siteId, limitUserNum);
	}


	@Override
	public int updateCheckedStatus(Integer id, boolean state) {
		Map<String, Object> article = new HashMap<String, Object>(2);
		article.put("id", id);
		article.put("checked", state ? 11 : 10);
		return articleDao.update(article);
	}

    @Override
    public List<ArticleExtra> getArticleExtraList(Integer siteId) {
        return articleDao.getArticleExtraList(siteId);
    }

    @Override
    public void saveArticleExtraList(Integer siteId, List<Map<String, Object>> extraList) {
        articleDao.deleteAllArticleExtra(siteId);
        for (Map<String, Object> map : extraList)
            map.put("siteId", siteId);
        articleDao.saveArticleExtraList(extraList);
    }

    @Override
    public List<Map<String, Object>> getArticleExtraListMap(Integer siteId) {
        return articleDao.getArticleExtraListMap(siteId);
    }
}
