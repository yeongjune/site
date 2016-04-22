package com.site.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.vo.PageList;
import com.site.dao.MessageDao;
import com.site.model.Message;
import com.site.service.MessageService;

/**
 * 
 * @author ah
 *
 */
@Service
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageDao messageDao;
	
	@Override
	public Integer save(Message message) {
		return messageDao.save(message);
	}

	@Override
	public PageList list(Integer siteId, String keyword, Integer currentPage, Integer pageSize) {
		return messageDao.list(siteId, keyword,currentPage, pageSize);
	}

	@Override
	public void delete(String ids) {
		messageDao.delete(ids);
	}
	
	@Override
	public Map<String, Object> getMsgById(Integer id){
		return messageDao.getMsgById(id);
	}
	
	@Override
	public int updateReply(Integer id, String Content){
		return messageDao.updateReply(id, Content);
	}
	
	
	@Override
	public PageList getHistory(Integer siteId, String name, Date createTime, Integer currentPage, Integer pageSize){
		return messageDao.getHistory(siteId, name, createTime, currentPage, pageSize);
	}
	
	@Override
	public PageList getMsgByName(Integer siteId, String name, Integer currentPage, Integer pageSize){
		return messageDao.getMsgByName(siteId, name, currentPage, pageSize);
	}

}
