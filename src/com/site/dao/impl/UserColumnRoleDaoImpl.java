package com.site.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.model.User;
import com.base.dao.SQLDao;
import com.site.dao.UserColumnRoleDao;
import com.site.model.ColumnRole;
import com.site.model.UserColumnRole;

@Repository
public class UserColumnRoleDaoImpl implements UserColumnRoleDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> getRoleList(Integer userId) {
		Integer siteId = dao.queryForInt("select siteId from "+User.tableName+" where id=?", userId);
		String sql = "select r.id,r.name,r.pid,r.sort,IF(ur.checked, true, false) as checked from "+ColumnRole.tableName+" as r left join "+UserColumnRole.tableName+" as ur on r.id=ur.roleId and ur.userId=? where r.siteId=?";
		List<Map<String, Object>> mapList = dao.queryForList(sql, userId, siteId);
		return mapList;
	}

	@Override
	public int saveOrUpdate(Integer siteId, Integer userId, Integer roleId, Integer checked) {
		Map<String, Object> user = dao.queryForMap("select * from "+User.tableName+" where id=? and siteId=?", userId, siteId);
		if(user==null || user.isEmpty())return 0;
		Map<String, Object> role = dao.queryForMap("select * from "+ColumnRole.tableName+" where id=? and siteId=?", roleId, siteId);
		if(role==null || role.isEmpty())return 0;
		int i = dao.update("update "+UserColumnRole.tableName+" set checked=? where userId=? and roleId=?", checked, userId, roleId);
		if(i<=0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("siteId", siteId);
			map.put("userId", userId);
			map.put("roleId", roleId);
			map.put("checked", checked);
			Serializable id = dao.save(UserColumnRole.tableName, map);
			if(id!=null)i = 1;
		}
		return i;
	}

	@Override
	public List<Integer> getUserRole(Integer userId) {
		return dao.queryForList("select roleId from "+UserColumnRole.tableName+" where userId=? and checked=1", Integer.class, userId);
	}

	@Override
	public int deleteByUserId(Integer userId) {
		return dao.delete(UserColumnRole.tableName, "userId", userId);
	}

	@Override
	public int deleteByRoleId(Integer roleId) {
		return dao.delete(UserColumnRole.tableName, "roleId", roleId);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return dao.delete(UserColumnRole.tableName, "siteId", siteId);
	}

}
