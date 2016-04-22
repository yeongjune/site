package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.site.dao.ArticleClickCountDao;
import com.site.model.Article;
import com.site.model.ArticleClickCount;

@Repository
public class ArticleClickCountImpl implements ArticleClickCountDao{
	
	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(ArticleClickCount articleClickCount) {
		
		//成功保存后返回一个Serializable id
		return hqlDao.save(articleClickCount);
	}

	@Override
	public Integer checkClickCount(Integer articleId,
			Integer userId) {
		String sql = " SELECT count(*) FROM " + ArticleClickCount.tableName + " WHERE articleId = ? AND userId = ?";
		//System.out.println(sql);
		return sqlDao.queryForInt(sql, articleId, userId);
	}
	
	@Override
	public int checkTotalCount(Integer columnId){
		String sql = " SELECT SUM(a.clickCount) FROM " + Article.tableName + " AS a WHERE a.columnId = ? ";
		return sqlDao.queryForInt(sql, columnId);
	}
	
	@Override
	public List<Map<String,Object>> getClickedOption(Integer columnId, Integer userId){
		String sql = "SELECT articleId FROM " + ArticleClickCount.tableName + " WHERE columnId = ? AND userId = ?";
		return sqlDao.queryForList(sql, columnId, userId);
	}
	
	@Override
	public int batchSave(Integer columnId, Integer userId, String articleIds,String mobilePhone){
		String[] articleIds_arr = articleIds.split(",");
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		for(int i = 0; i< articleIds_arr.length; i++){
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("articleId", articleIds_arr[i]);
			tempMap.put("userId", userId);
			tempMap.put("columnId", columnId);
			if(StringUtils.isNotBlank(mobilePhone)) {				
				tempMap.put("mobilePhone",mobilePhone);
			}
			mapList.add(tempMap);
		}
		return sqlDao.save(ArticleClickCount.tableName, mapList);
	}

	@Override
	public int checkExistsMobile(String mobileNo, int columnId) {
		// TODO Auto-generated method stub
		String sql="SELECT COUNT(*) FROM "+ArticleClickCount.tableName+" WHERE  mobilePhone=?  AND columnId=? ";
		return sqlDao.queryForInt(sql,mobileNo,columnId);
	}

}
