package com.authority.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.MenuDao;
import com.authority.dao.RoleMenuDao;
import com.authority.model.Menu;
import com.authority.model.RoleMenu;
import com.base.dao.SQLDao;

@Repository
public class RoleMenuDaoImpl implements RoleMenuDao {

	@Autowired
	private SQLDao dao;
	@Autowired
	private MenuDao menuDao;

	@Override
	public int saveOrUpdate(Integer roleId, Integer menuId, Integer checked) {
		int i = dao.update("update "+RoleMenu.tableName+" set checked=? where roleId=? and menuId=?", checked, roleId, menuId);
		if(i>0){
			return i;
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleId", roleId);
			map.put("menuId", menuId);
			map.put("checked", checked);
			Serializable id = dao.save(RoleMenu.tableName, map);
			return id!=null?1:0;
		}
	}

	@Override
	public List<Map<String, Object>> load(Integer roleId) {
		return dao.queryForList("select * from "+RoleMenu.tableName+" where roleId=?", roleId);
	}

	@Override
	public List<Map<String, Object>> loadMenu(Integer roleId) {
		List<Map<String, Object>> mapList = dao.queryForList("select m.* from "+RoleMenu.tableName+" as rm, "+Menu.tableName+" as m where rm.roleId=? and rm.checked=1 and rm.menuId=m.id", roleId);
		if(mapList!=null && mapList.size()>0){
			List<Map<String, Object>> allParentMenus = menuDao.allParentMenu(roleId);
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
