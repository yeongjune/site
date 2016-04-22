package com.authority.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.MenuDao;
import com.authority.dao.RoleDao;
import com.authority.dao.RoleMenuDao;
import com.authority.service.RoleMenuService;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private RoleMenuDao dao;

	@Override
	public List<Map<String, Object>> load(Integer roleId) {
		List<Map<String, Object>> menuList = menuDao.allParentMenu(roleId);
		Set<Map<String, Object>> set = new HashSet<Map<String, Object>>();
		set.addAll(menuList);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(set);
		if(list!=null && list.size()>0){
			List<Map<String, Object>> roleMenuList = dao.load(roleId);
			Map<Object, Map<String, Object>> roleMenuMap = new HashMap<Object, Map<String,Object>>();
			if(roleMenuList!=null && roleMenuList.size()>0){
				for (Map<String, Object> map : roleMenuList) {
					roleMenuMap.put(map.get("menuId"), map);
				}
			}
			for (Map<String,Object> map : list) {
				if(map==null || map.isEmpty())continue;
				Map<String, Object> menuUrl = roleMenuMap.get(map.get("id"));
				if(menuUrl!=null && !menuUrl.isEmpty()){
					map.put("checked", menuUrl.get("checked").equals(1)?true:false);
				}else{
					map.put("checked", false);
				}
				map.put("open", true);
			}
			return list;
		}
		return null;
	}

	@Override
	public int saveOrUpdate(Integer roleId, Integer menuId, Integer checked, Integer siteId) {
		long count = roleDao.countByIdAndSiteId(roleId, siteId);
		if(count<=0)return 0;
		List<Map<String, Object>> menuList = menuDao.allParentMenu(roleId);
		if(menuList!=null && menuList.size()>0){
			Set<Integer> menuIdSet = new HashSet<Integer>();
			for (Map<String, Object> menu : menuList) {
				if(menu==null || menu.isEmpty() || menu.get("id")==null)continue;
				Integer mid = (Integer) menu.get("id");
				menuIdSet.add(mid);
			}
			if(menuIdSet.contains(menuId)){
				return dao.saveOrUpdate(roleId, menuId, checked);
			}
		}
		return 0;
	}

}
