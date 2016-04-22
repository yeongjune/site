package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.SQLDao;
import com.site.dao.ColumnRoleDao;
import com.site.model.Column;
import com.site.model.ColumnRole;
import com.site.model.RoleColumn;

@Repository
public class ColumnRoleDaoImpl implements ColumnRoleDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> load(Integer siteId) {
		return dao.queryForList("select * from "+ColumnRole.tableName+" where siteId=? order by sort", siteId);
	}

	@Override
	public Serializable save(Map<String, Object> map) {
		return dao.save(ColumnRole.tableName, map);
	}

	@Override
	public int delete(Integer id, Integer siteId) {
		int i = dao.update("delete from "+ColumnRole.tableName+" where id=? and siteId=?", id, siteId);
		if(i>0){
			List<Map<String, Object>> allRole = dao.queryForList("select * from "+ColumnRole.tableName);
			List<Integer> selfAndChildrenId = selfAndChildren(id, allRole);
			for (Integer roleId : selfAndChildrenId) {
				if(roleId==null)continue;
				dao.delete(RoleColumn.tableName, "roleId", roleId);
				i += dao.delete(ColumnRole.tableName, "id", roleId);
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
		return dao.updateMap(ColumnRole.tableName, "id", map);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		List<Integer> ids = dao.queryForList("select id from "+ColumnRole.tableName+" where siteId=?", Integer.class, siteId);
		if(ids!=null && ids.size()>0){
			int i = 0;
			List<Map<String, Object>> allRole = dao.queryForList("select * from "+ColumnRole.tableName);
			for (Integer id : ids) {
				List<Integer> selfAndChildrenId = selfAndChildren(id, allRole);
				for (Integer roleId : selfAndChildrenId) {
					if(roleId==null)continue;
					dao.delete(RoleColumn.tableName, "roleId", roleId);
					i += dao.delete(ColumnRole.tableName, "id", roleId);
				}
			}
			return i;
		}
		return 0;
	}

	@Override
	public long countByIdAndSiteId(Integer roleId, Integer siteId) {
		return dao.queryForLong("select count(id) from "+ColumnRole.tableName+" where id=? and siteId=?", roleId, siteId);
	}

	@Override
	public List<Map<String, Object>> allParentMenu(Integer roleId) {
		Map<String, Object> role = dao.queryForMap("select * from "+ColumnRole.tableName+" where id=?", roleId);
		if(role==null)return new ArrayList<Map<String,Object>>();
		if(role.get("pid")==null || role.get("pid").toString().equals("0")){
			Integer siteId = (Integer) role.get("siteId");
			return dao.queryForList("select * from "+Column.tableName+" where siteId=?", siteId);
		}else{
			Integer pid = (Integer) role.get("pid");
			List<Map<String, Object>> mapList = dao.queryForList("select m.* from "+RoleColumn.tableName+" as rm, "+Column.tableName+" as m where rm.roleId=? and rm.checked=1 and rm.columnId=m.id", pid);
			if(mapList==null || mapList.isEmpty())return new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> allParentMenus = allParentMenu(pid);
			if(allParentMenus==null){
				return mapList;
			}else if(allParentMenus.isEmpty()){
				return null;
			}else{
				mapList.retainAll(allParentMenus);
				return mapList;
			}
		}
	}

}
