package com.authority.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.UrlDao;
import com.authority.model.AuthorityUrl;
import com.authority.model.MenuUrl;
import com.base.dao.SQLDao;

@Repository
public class UrlDaoImpl implements UrlDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> load() {
		return dao.queryForList("select * from "+AuthorityUrl.tableName);
	}

	@Override
	public Set<String> getUrlSet() {
		List<String> urlList = dao.queryForList("select url from "+AuthorityUrl.tableName, String.class);
		if(urlList==null || urlList.isEmpty())return null;
		Set<String> urlSet = new HashSet<String>();
		for (String url : urlList) {
			urlSet.add(url);
		}
		return urlSet;
	}

	@Override
	public Set<String> getUrlSet(Set<Integer> menuIds) {
		List<String> commonList = dao.queryForList("select url from "+AuthorityUrl.tableName+" where common=1", String.class);
		String paramsString = "";
		Object[] params = new Object[menuIds.size()];
		int i = 0;
		for (Integer id : menuIds) {
			if(paramsString.equals("")){
				paramsString += "?";
			}else{
				paramsString += ",?";
			}
			params[i] = id;
			i++;
		}
		String sql = 
			"select u.url from " + 
				MenuUrl.tableName+" as mu, "+AuthorityUrl.tableName+" as u " +
			"where mu.urlId=u.id and mu.checked=1 and mu.menuId in("+paramsString+")";
		List<String> urlList = dao.queryForList(sql, String.class, params);
		if(urlList==null || urlList.isEmpty()){
			if(commonList!=null && commonList.size()>0)return new HashSet<String>(commonList);
			return null;
		}
		Set<String> urlSet = new HashSet<String>(urlList);
		if(commonList!=null && commonList.size()>0)urlSet.addAll(commonList);
		return urlSet;
	}

	@Override
	public List<Map<String, Object>> allUrl() {
		return dao.queryForList("select * from "+AuthorityUrl.tableName);
	}

	@Override
	public int update(String id, Integer checked) {
		return dao.update("update "+AuthorityUrl.tableName+" set common=? where id=? and pid is not null and pid!=''", checked, id);
	}

	@Override
	public int updateIsPublic(String id, Integer checked) {
		return dao.update("update "+AuthorityUrl.tableName+" set isPublic=? where id=? and pid is not null and pid!=''", checked, id);
	}

	@Override
	public Map<String, Object> get(String id) {
		return dao.queryForMap("select * from "+AuthorityUrl.tableName+" where id=?", id);
	}

}
