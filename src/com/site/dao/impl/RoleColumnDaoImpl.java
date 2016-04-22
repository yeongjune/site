package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.model.Menu;
import com.base.dao.SQLDao;
import com.site.dao.ColumnRoleDao;
import com.site.dao.RoleColumnDao;
import com.site.model.RoleColumn;

@Repository
public class RoleColumnDaoImpl implements RoleColumnDao {

	@Autowired
	private SQLDao dao;
	@Autowired
	private ColumnRoleDao roleDao;

	@Override
	public int saveOrUpdate(Integer roleId, Integer columnId, Integer checked) {
		int i = dao.update("update "+RoleColumn.tableName+" set checked=? where roleId=? and columnId=?", checked, roleId, columnId);
		if(i>0){
			return i;
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleId", roleId);
			map.put("columnId", columnId);
			map.put("checked", checked);
			Serializable id = dao.save(RoleColumn.tableName, map);
			return id!=null?1:0;
		}
	}

	@Override
	public List<Map<String, Object>> load(Integer roleId) {
		return dao.queryForList("select * from "+RoleColumn.tableName+" where roleId=?", roleId);
	}

	@Override
	public List<Map<String, Object>> loadMenu(Integer roleId) {
		List<Map<String, Object>> mapList = dao.queryForList("select m.* from "+RoleColumn.tableName+" as rm, "+Menu.tableName+" as m where rm.roleId=? and rm.checked=1 and rm.columnId=m.id", roleId);
		if(mapList!=null && mapList.size()>0){
			List<Map<String, Object>> allParentMenus = roleDao.allParentMenu(roleId);
			if(allParentMenus==null){
			}else if(allParentMenus.isEmpty()){
				return null;
			}else{
				mapList.retainAll(allParentMenus);
			}
		}
		Set<Map<String, Object>> set = new HashSet<Map<String, Object>>();
		set.addAll(mapList);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(set);
		return list;
	}

}
