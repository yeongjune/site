package com.authority.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.SiteRoleDao;
import com.authority.model.Role;
import com.authority.model.SiteRole;
import com.base.dao.SQLDao;

@Repository
public class SiteRoleDaoImpl implements SiteRoleDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> getRoleList(Integer siteId) {
		String sql = "select r.id,r.name,r.pid,r.sort,IF(sr.checked, true, false) as checked from "+Role.tableName+" as r left join "+SiteRole.tableName+" as sr on r.id=sr.roleId and sr.siteId=? where r.siteId is null or r.siteId=0 order by r.sort";
		List<Map<String, Object>> mapList = dao.queryForList(sql, siteId);
		return mapList;
	}

	@Override
	public int saveOrUpdate(Integer siteId, Integer roleId, Integer checked) {
		int i = dao.update("update "+SiteRole.tableName+" set checked=? where siteId=? and roleId=?", checked, siteId, roleId);
		if(i<=0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("siteId", siteId);
			map.put("roleId", roleId);
			map.put("checked", checked);
			Serializable id = dao.save(SiteRole.tableName, map);
			if(id!=null)i = 1;
		}
		return i;
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return dao.delete(SiteRole.tableName, "siteId", siteId);
	}

	@Override
	public int deleteByRoleId(Integer roleId) {
		return dao.delete(SiteRole.tableName, "roleId", roleId);
	}

}
