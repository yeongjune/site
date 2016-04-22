package com.authority.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.MenuDao;
import com.authority.model.Menu;
import com.authority.model.MenuUrl;
import com.authority.model.Role;
import com.authority.model.RoleMenu;
import com.authority.model.SiteRole;
import com.base.dao.SQLDao;

@Repository
public class MenuDaoImpl implements MenuDao {

	@Autowired
	private SQLDao dao;

	@Override
	public List<Map<String, Object>> load() {
		return dao.queryForList("select id,name,pid,sort,IFNULL(url, '') as url from "+Menu.tableName+" order by sort");
	}

	@Override
	public Serializable save(Map<String, Object> map) {
		return dao.save(Menu.tableName, map);
	}

	@Override
	public int delete(Integer id, Integer deleteChildren) {
		if(deleteChildren!=null && deleteChildren>0){
			List<Map<String, Object>> allMenu = dao.queryForList("select * from "+Menu.tableName);
			List<Integer> selfAndChildrenId = selfAndChildren(id, allMenu);
			int i = 0;
			for (Integer menuId : selfAndChildrenId) {
				if(menuId==null)continue;
				dao.delete(MenuUrl.tableName, "menuId", menuId);
				dao.delete(RoleMenu.tableName, "menuId", menuId);
				i += dao.delete(Menu.tableName, "id", menuId);
			}
			return i;
		}else{
			return deleteMenu(id);
		}
	}

	private List<Integer> selfAndChildren(Integer id,
			List<Map<String, Object>> allMenu) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(id);
		for (Map<String, Object> map : allMenu) {
			if(map==null || map.isEmpty())continue;
			if(map.get("pid")!=null && map.get("pid").equals(id)){
				list.addAll(selfAndChildren((Integer) map.get("id"), allMenu));
			}
		}
		return list;
	}

	private int deleteMenu(Integer id) {
		Map<String, Object> map = dao.queryForMap("select * from "+Menu.tableName+" where id=?", id);
		if(map!=null && !map.isEmpty()){
			dao.delete(MenuUrl.tableName, "menuId", map.get("id"));
			dao.update("update "+Menu.tableName+" set pid=? where pid=?", map.get("pid"), map.get("id"));
			return dao.delete(Menu.tableName, id);
		}else{
			return 0;
		}
	}

	@Override
	public int update(Map<String, Object> map) {
		return dao.updateMap(Menu.tableName, "id", map);
	}

	@Override
	public List<Map<String, Object>> loadByPid() {
		return loadByPid(null);
	}

	@Override
	public List<Map<String, Object>> loadByPid(Integer pid) {
		if(pid==null || pid==0){
			return dao.queryForList("select * from "+Menu.tableName+" where pid is null or pid=0 order by sort");
		}else{
			return dao.queryForList("select * from "+Menu.tableName+" where pid=? order by sort", pid);
		}
	}

	@Override
	public List<Map<String, Object>> allParentMenu(Integer roleId) {
		Map<String, Object> role = dao.queryForMap("select * from "+Role.tableName+" where id=?", roleId);
		if(role==null)return new ArrayList<Map<String,Object>>();
		if(role.get("pid")==null || role.get("pid").toString().equals("0")){
			Integer siteId = (Integer) role.get("siteId");
			if(siteId>0){
				Set<Map<String, Object>> sysMenus = new HashSet<Map<String,Object>>();
				List<Map<String, Object>> parents = dao.queryForList("select r.* from "+Role.tableName+" as r, "+SiteRole.tableName+" as sr where sr.siteId=? and sr.roleId=r.id and sr.checked=1", siteId);
				if(parents==null || parents.isEmpty())return new ArrayList<Map<String,Object>>();
				List<Integer> parentIds = new ArrayList<Integer>();
				for (Map<String, Object> map : parents) {
					parentIds.add((Integer) map.get("id"));
				}
				for (Map<String, Object> map : parents) {
					if(parentIds.contains(map.get("pid")))parentIds.remove(map.get("pid"));
				}
				if(parentIds!=null && parentIds.size()>0){
					for (Integer tmp : parentIds) {
						List<Map<String, Object>> mapList = dao.queryForList("select m.* from "+RoleMenu.tableName+" as rm, "+Menu.tableName+" as m where rm.roleId=? and rm.checked=1 and rm.menuId=m.id", tmp);
						if(mapList==null || mapList.isEmpty())continue;
						List<Map<String, Object>> allParentMenus = allParentMenu(tmp);
						if(allParentMenus!=null && allParentMenus.size()>0)mapList.retainAll(allParentMenus);
						if(mapList.size()>0)sysMenus.addAll(mapList);
					}
				}
				return new ArrayList<Map<String,Object>>(sysMenus);
			}
			List<Map<String, Object>> all = dao.queryForList("select * from "+Menu.tableName);
			return all;
		}else{
			Integer pid = (Integer) role.get("pid");
			List<Map<String, Object>> mapList = dao.queryForList("select m.* from "+RoleMenu.tableName+" as rm, "+Menu.tableName+" as m where rm.roleId=? and rm.checked=1 and rm.menuId=m.id", pid);
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
