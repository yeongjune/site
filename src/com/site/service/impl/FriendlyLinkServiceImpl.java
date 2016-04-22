package com.site.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.util.EffectiveUrl;
import com.base.vo.PageList;
import com.site.dao.FriendlyLinkDao;
import com.site.model.FriendlyLink;
import com.site.service.FriendlyLinkService;

@Service
public class FriendlyLinkServiceImpl implements FriendlyLinkService {
	
	@Autowired FriendlyLinkDao friendlyLinkDao;

	@Override
	public Serializable save(FriendlyLink friendlyLink) {
		
		return friendlyLinkDao.save(friendlyLink);
	}

	@Override
	public Serializable save(Map<String, Object> friendlyLink) {
		
		return friendlyLinkDao.save(friendlyLink);
	}

	@Override
	public int delete(Integer id) {
		
		return friendlyLinkDao.delete(id);
	}

	@Override
	public int delete(Integer siteId,String ids) {
		
		return friendlyLinkDao.delete(siteId,ids);
	}

	@Override
	public int update(Map<String, Object> friendlyLink) {//假如修改一个链接  那么默认先让他是一个有效的链接
		this.updateEffective(Long.valueOf(friendlyLink.get("id").toString()), EffectiveUrl.EFFECTIVE_PASS);
		return friendlyLinkDao.update(friendlyLink);
	}

	@Override
	public int update(FriendlyLink friendlyLink) {
		
		return friendlyLinkDao.update(friendlyLink);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		
		return friendlyLinkDao.load(id);
	}

	@Override
	public FriendlyLink get(Integer id) {
		
		return friendlyLinkDao.get(id);
	}

	@Override
	public List<Map<String, Object>> findFriendlyLinkList(Integer siteId,
			String keyword, Integer start, Integer limit) {
		
		return friendlyLinkDao.findFriendlyLinkList(siteId, keyword, start, limit,null);
	}

	@Override
	public PageList findFriendlyLinkPageList(Integer currentPage,
			Integer pageSize, Integer siteId, String keyword) {
		
		return friendlyLinkDao.findFriendlyLinkPageList(currentPage, pageSize, siteId, keyword);
	}

	@Override
	public List<Map<String, Object>> findEffectiveFriendlyLinkList() {
		// TODO Auto-generated method stub
		return friendlyLinkDao.findEffectiveFriendlyLinkList();
	}

	@Override
	public int updateEffective(long id, int status) {
		// TODO Auto-generated method stub
		return friendlyLinkDao.updateEffective(id, status);
	}

}
