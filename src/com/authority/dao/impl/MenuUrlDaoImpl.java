package com.authority.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.MenuUrlDao;
import com.authority.model.AuthorityUrl;
import com.authority.model.Menu;
import com.authority.model.MenuUrl;
import com.base.dao.SQLDao;

@Repository
public class MenuUrlDaoImpl implements MenuUrlDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> load(Integer menuId) {
		return dao.queryForList("select * from "+MenuUrl.tableName+" where menuId=?", menuId);
	}

	@Override
	public int saveOrUpdate(Integer menuId, String urlId, Integer checked) {
		int i = dao.update("update "+MenuUrl.tableName+" set checked=? where menuId=? and urlId=?", checked, menuId, urlId);
		if(i>0){
			if(checked==0){
				dao.update("update "+Menu.tableName+" set url='' where id=? and url=(select url from "+AuthorityUrl.tableName+" where id=?)", menuId, urlId);
			}
			return i;
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("menuId", menuId);
			map.put("urlId", urlId);
			map.put("checked", checked);
			Serializable id = dao.save(MenuUrl.tableName, map);
			return id!=null?1:0;
		}
	}

}
