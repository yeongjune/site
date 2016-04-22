package com.site.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.dao.ArticleClickCountDao;
import com.site.dao.ArticleDao;
import com.site.model.ArticleClickCount;
import com.site.service.ArticleClickCountService;
import com.site.service.SendMsgService;

@Service
public class ArticleClickCountServiceImpl implements ArticleClickCountService{

	@Autowired
	private ArticleClickCountDao articleClickCountDao;
	
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private SendMsgService sendMsgService;
	
	@Override
	public int save(Integer articleId, Integer userId) {
		int num = articleClickCountDao.checkClickCount(articleId, userId);
		if(num <= 0){
			//如果没有此条点击投票记录，则组装保存
			ArticleClickCount articleClickCount = new ArticleClickCount();
			articleClickCount.setArticleId(articleId);
			articleClickCount.setUserId(userId);
			Serializable sNum = articleClickCountDao.save(articleClickCount);
			int aNum = articleDao.updateArticleClickCount(articleId);
			if(sNum != null && aNum > 0){
				return aNum;
			}
		}else{
			int aNum = articleDao.updateArticleClickCount(articleId);
			return aNum;
		}
		return 0;
	}

	@Override
	public int checkTotalCount(Integer columnId) {
		return articleClickCountDao.checkTotalCount(columnId);
	}
	
	@Override
	public List<Map<String,Object>> getClickedOption(Integer columnId, Integer userId){
		return articleClickCountDao.getClickedOption(columnId, userId);
	}

	@Override
	public int updateClickCountBatch(Integer columnId, Integer userId, String articleIds,String mobilePhone) {
		int updateResutl = articleDao.batchUpdateArticleClickCount(articleIds);
		int saveResutl = articleClickCountDao.batchSave(columnId, userId, articleIds,mobilePhone);
		if(updateResutl <= 0 || saveResutl <= 0){
			return 0;
		}else{
			return updateResutl;
		}
		
	}

	@Override
	public void saveMobileNo(String mobileNo,String captcha, Integer siteId) {
		// TODO Auto-generated method stub
		sendMsgService.sendMsg(mobileNo,"【广州市番禺区来穗人员服务管理局】您好，此次验证码是:"+captcha,siteId);    		
    	
	}

	@Override
	public int checkExistsMobile(String mobileNo, int columnId) {
		// TODO Auto-generated method stub
		return articleClickCountDao.checkExistsMobile(mobileNo,columnId);
	}

	
}
