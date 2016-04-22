package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.site.dao.ArticleRecommendDao;
import com.site.model.ArticleRecommend;

@Repository
public class ArticleRecommendDaoImpl implements ArticleRecommendDao {
	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(ArticleRecommend articleRecommend) {
		
		return hqlDao.save(articleRecommend);
	}

	@Override
	public int delete(Integer id) {
		return sqlDao.delete(ArticleRecommend.tableName, id);
	}
	
	@Override
	public int deleteByArticleId(String  ArticleIds) {
		if (StringUtil.isEmpty(ArticleIds)) {
			return 0;
		}
		String sql=" delete from "+ArticleRecommend.tableName +"  where articleId in ("; 
		String [] idsArray=ArticleIds.split(",");
		List<Object> param=new ArrayList<Object>();
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public int deleteByRcommendId(Integer recommendId) {
		return sqlDao.delete(ArticleRecommend.tableName, "recommendId", recommendId);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql=" select ar.* from " + ArticleRecommend.tableName + " ar where ar.id = ? ";
		return sqlDao.queryForMap(sql, id);
	}

	@Override
	public List<Map<String, Object>> findList(Integer articleId, Integer recommendId) {
		String sql=" select ar.* from " + ArticleRecommend.tableName + " ar where 1=1  ";
		List<Object> param=new ArrayList<Object>();
		if (articleId!=null&&articleId>0) {
			sql+=" and ar.articleId = ? ";
			param.add(articleId);
		}
		if (recommendId!=null&&recommendId>0) {
			sql+=" and ar.recommendId = ? ";
			param.add(recommendId);
		}
		if (param.size()>0) {
			return sqlDao.queryForList(sql, param.toArray());
		}else{
			return sqlDao.queryForList(sql);
		}
	}

	
}
