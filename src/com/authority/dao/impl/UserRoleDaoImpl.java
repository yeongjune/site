package com.authority.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.UserRoleDao;
import com.authority.model.Role;
import com.authority.model.SiteRole;
import com.authority.model.User;
import com.authority.model.UserRole;
import com.base.dao.SQLDao;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> getRoleList(Integer userId) {
		Integer siteId = dao.queryForInt("select siteId from "+User.tableName+" where id=?", userId);
		String sql = "select r.id,r.name,r.pid,r.sort,IF(ur.checked, true, false) as checked from "+Role.tableName+" as r left join "+UserRole.tableName+" as ur on r.id=ur.roleId and ur.userId=? where r.siteId=?";
		List<Map<String, Object>> mapList = dao.queryForList(sql, userId, siteId);
		return mapList;
	}

	@Override
	public int saveOrUpdate(Integer siteId, Integer userId, Integer roleId, Integer checked) {
		Map<String, Object> user = dao.queryForMap("select * from "+User.tableName+" where id=? and siteId=?", userId, siteId);
		if(user==null || user.isEmpty())return 0;
		Map<String, Object> role = dao.queryForMap("select * from "+Role.tableName+" where id=? and siteId=?", roleId, siteId);
		if(role==null || role.isEmpty())return 0;
		int i = dao.update("update "+UserRole.tableName+" set checked=? where userId=? and roleId=?", checked, userId, roleId);
		if(i<=0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("siteId", siteId);
			map.put("userId", userId);
			map.put("roleId", roleId);
			map.put("checked", checked);
			Serializable id = dao.save(UserRole.tableName, map);
			if(id!=null)i = 1;
		}
		return i;
	}

	@Override
	public List<Integer> getUserRole(Integer userId) {
		Map<String, Object> user = dao.queryForMap("select * from "+User.tableName+" where id=?", userId);
		if(user==null || user.isEmpty())return null;
		String account = (String) user.get("account");
		if(account!=null && account.equals("admin")){
			Integer siteId = (Integer) user.get("siteId");
			return dao.queryForList("select roleId from "+SiteRole.tableName+" where siteId=? and checked=1", Integer.class, siteId);
		}else{
			return dao.queryForList("select roleId from "+UserRole.tableName+" where userId=? and checked=1", Integer.class, userId);
		}
	}

	@Override
	public int deleteByUserId(Integer userId) {
		return dao.delete(UserRole.tableName, "userId", userId);
	}

	@Override
	public int deleteByRoleId(Integer roleId) {
		return dao.delete(UserRole.tableName, "roleId", roleId);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return dao.delete(UserRole.tableName, "siteId", siteId);
	}

}
