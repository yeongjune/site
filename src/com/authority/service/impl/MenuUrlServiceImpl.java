package com.authority.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.MenuUrlDao;
import com.authority.dao.UrlDao;
import com.authority.service.MenuUrlService;

@Service
public class MenuUrlServiceImpl implements MenuUrlService {

	@Autowired
	private UrlDao urlDao;
	@Autowired
	private MenuUrlDao dao;

	@Override
	public List<Map<String, Object>> load(Integer menuId) {
		List<Map<String, Object>> urlList = urlDao.load();
		if(urlList!=null && urlList.size()>0){
			List<Map<String, Object>> menuUrlList = dao.load(menuId);
			Map<Object, Map<String, Object>> menuUrlMap = new HashMap<Object, Map<String,Object>>();
			if(menuUrlList!=null && menuUrlList.size()>0){
				for (Map<String, Object> map : menuUrlList) {
					menuUrlMap.put(map.get("urlId"), map);
				}
			}
			for (Map<String,Object> map : urlList) {
				if(map==null || map.isEmpty())continue;
				Map<String, Object> menuUrl = menuUrlMap.get(map.get("id"));
				if(menuUrl!=null && !menuUrl.isEmpty()){
					map.put("checked", menuUrl.get("checked").equals(1)?true:false);
				}else{
					map.put("checked", false);
				}
			}
			return urlList;
		}
		return null;
	}

	@Override
	public int saveOrUpdate(Integer menuId, String urlId,
			Integer checked) {
		return dao.saveOrUpdate(menuId, urlId, checked);
	}

}
