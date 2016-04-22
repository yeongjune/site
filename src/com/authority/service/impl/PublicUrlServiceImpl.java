package com.authority.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.UrlDao;
import com.authority.service.PublicUrlService;
import com.base.config.Init;

@Service("AuthorityPublicService")
public class PublicUrlServiceImpl implements PublicUrlService {

	@Autowired
	private UrlDao dao;

	@Override
	public List<Map<String, Object>> load() {
		return dao.allUrl();
	}

	@Override
	public int update(HttpServletRequest request, String id, Integer checked) {
		int i = dao.updateIsPublic(id, checked);
		if(i>0){
			updateApplicationCommonUrl(request, id);
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	private void updateApplicationCommonUrl(HttpServletRequest request, String id) {
		Map<String, Object> map = dao.get(id);
		if(map==null || map.isEmpty())return ;
		Object obj = request.getSession().getServletContext().getAttribute(Init.APPLICATION_URL_KEY);
		Map<Object, Object> commonUrl = null;
		if(obj==null || !(obj instanceof Map)){
			commonUrl = new HashMap<Object, Object>();
		}else{
			commonUrl = (Map<Object, Object>) obj;
		}
		commonUrl.put(map.get("url"), map);
		request.getSession().getServletContext().setAttribute(Init.APPLICATION_URL_KEY, commonUrl);
	}

}
