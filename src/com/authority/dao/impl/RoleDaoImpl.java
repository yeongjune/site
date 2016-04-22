package com.authority.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.RoleDao;
import com.authority.model.Role;
import com.authority.model.RoleMenu;
import com.base.dao.SQLDao;

@Repository
public class RoleDaoImpl implements RoleDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> load(Integer siteId) {
		return dao.queryForList("select * from "+Role.tableName+" where siteId=? order by sort", siteId);
	}

	@Override
	public Serializable save(Map<String, Object> map) {
		return dao.save(Role.tableName, map);
	}

	@Override
	public int delete(Integer id, Integer siteId) {
		int i = dao.update("delete from "+Role.tableName+" where id=? and siteId=?", id, siteId);
		if(i>0){
			List<Map<String, Object>> allRole = dao.queryForList("select * from "+Role.tableName);
			List<Integer> selfAndChildrenId = selfAndChildren(id, allRole);
			for (Integer roleId : selfAndChildrenId) {
				if(roleId==null)continue;
				dao.delete(RoleMenu.tableName, "roleId", roleId);
				i += dao.delete(Role.tableName, "id", roleId);
			}
		}
		return i;
	}

	private List<Integer> selfAndChildren(Integer id,
			List<Map<String, Object>> allRole) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(id);
		for (Map<String, Object> map : allRole) {
			if(map==null || map.isEmpty())continue;
			if(map.get("pid")!=null && map.get("pid").equals(id)){
				list.addAll(selfAndChildren((Integer) map.get("id"), allRole));
			}
		}
		return list;
	}

	@Override
	public int update(Map<String, Object> map) {
		return dao.updateMap(Role.tableName, "id", map);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		List<Integer> ids = dao.queryForList("select id from "+Role.tableName+" where siteId=?", Integer.class, siteId);
		if(ids!=null && ids.size()>0){
			int i = 0;
			List<Map<String, Object>> allRole = dao.queryForList("select * from "+Role.tableName);
			for (Integer id : ids) {
				List<Integer> selfAndChildrenId = selfAndChildren(id, allRole);
				for (Integer roleId : selfAndChildrenId) {
					if(roleId==null)continue;
					dao.delete(RoleMenu.tableName, "roleId", roleId);
					i += dao.delete(Role.tableName, "id", roleId);
				}
			}
			return i;
		}
		return 0;
	}

	@Override
	public long countByIdAndSiteId(Integer roleId, Integer siteId) {
		return dao.queryForLong("select count(id) from "+Role.tableName+" where id=? and siteId=?", roleId, siteId);
	}

}
