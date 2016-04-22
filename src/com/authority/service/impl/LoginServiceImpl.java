package com.authority.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.MenuDao;
import com.authority.dao.RoleMenuDao;
import com.authority.dao.UrlDao;
import com.authority.dao.UserDao;
import com.authority.dao.UserRoleDao;
import com.authority.service.LoginService;
import com.base.util.CryptUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserDao dao;
	@Autowired
	private UrlDao urlDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private RoleMenuDao roleMenudao;
	@Autowired
	private UserRoleDao userRoledao;

	@Override
	public List<Map<String, Object>> loadForMenu() {
		List<Map<String, Object>> mapList = menuDao.loadByPid();
		if(mapList!=null && mapList.size()>0){
			for (Map<String, Object> map : mapList) {
				if(map==null || map.isEmpty())continue;
				List<Object> secondUrls = new ArrayList<Object>();
				List<Map<String, Object>> children = menuDao.loadByPid((Integer) map.get("id"));
				children = sort(children);
				map.put("children", children);
				if(children!=null && children.size()>0){
					for (Map<String, Object> map2 : children) {
						List<Object> thirdUrls = new ArrayList<Object>();
						List<Map<String, Object>> third = menuDao.loadByPid((Integer) map2.get("id"));
						third = sort(third);
						for (Map<String, Object> map3 : third) {
							thirdUrls.add(map3.get("url"));
						}
						map2.put("children", third);
						map2.put("childrenUrls", thirdUrls);
						if (map2.get("url")!=null && !map2.get("url").toString().trim().equals("") && thirdUrls!=null && !thirdUrls.isEmpty() && !thirdUrls.contains(map2.get("url"))) {
							map2.put("url", thirdUrls.get(0));
						}
						if (secondUrls!=null) {
							if (map2.get("url")!=null && !map2.get("url").toString().trim().equals("")) {
								secondUrls.add(map2.get("url"));
							}
							secondUrls.addAll(thirdUrls);
						}
					}
					if (secondUrls!=null && !secondUrls.isEmpty() && !secondUrls.contains(map.get("url"))) {
						map.put("url", secondUrls.get(0));
					}
				}
			}
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> loadForMenu(Integer userId) {
		List<Integer> roleList = userRoledao.getUserRole(userId);
		if(roleList==null || roleList.isEmpty())return null;
		List<Map<String, Object>> allMenus = new ArrayList<Map<String,Object>>();
		for (Integer roleId : roleList) {
			List<Map<String, Object>> menuList = roleMenudao.loadMenu(roleId);
			if(menuList==null || menuList.isEmpty())continue;
			allMenus.addAll(menuList);
		}
		if(allMenus==null || allMenus.isEmpty())return null;
		
		Set<Map<String, Object>> set = new HashSet<Map<String, Object>>();
		set.addAll(allMenus);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(set);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : list) {
			if(map.get("pid")==null || map.get("pid").equals(0)){
				mapList.add(map);
			}
		}
		mapList = sort(mapList);
		if(mapList.size()>0){
			for (Map<String, Object> map : mapList) {
				if(map==null || map.isEmpty())continue;
				List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
				
				List<Object> secondUrls = new ArrayList<Object>();
				
				for (Map<String, Object> map2 : list) {
					if(map2.get("pid")!=null && map2.get("pid").equals(map.get("id"))){
						children.add(map2);
					}
				}
				children = sort(children);
				map.put("children", children);
				if(children!=null && children.size()>0){
					for (Map<String, Object> map2 : children) {
						List<Map<String, Object>> third = new ArrayList<Map<String,Object>>();
						
						List<Object> thirdUrls = new ArrayList<Object>();
						
						for (Map<String, Object> map3 : list) {
							if(map3.get("pid")!=null && map3.get("pid").equals(map2.get("id"))){
								third.add(map3);
							}
						}
						third = sort(third);
						for (Map<String, Object> map3 : third) {
							thirdUrls.add(map3.get("url"));
						}
						map2.put("children", third);
						if (map2.get("url")!=null && !map2.get("url").toString().trim().equals("") && thirdUrls!=null && !thirdUrls.isEmpty() && !thirdUrls.contains(map2.get("url"))) {
							map2.put("url", thirdUrls.get(0));
						}
						map2.put("childrenUrls", thirdUrls);
						if (secondUrls!=null) {
							if (map2.get("url")!=null && !map2.get("url").toString().trim().equals("")) {
								secondUrls.add(map2.get("url"));
							}
							secondUrls.addAll(thirdUrls);
						}
					}
					if (secondUrls!=null && !secondUrls.isEmpty() && !secondUrls.contains(map.get("url"))) {
						map.put("url", secondUrls.get(0));
					}
					map.put("childrenUrls", secondUrls);
				}
			}
		}
		return mapList;
	}

	private List<Map<String, Object>> sort(List<Map<String, Object>> mapList) {
		if(mapList==null || mapList.isEmpty())return mapList;
		for (int i = 0; i < mapList.size(); i++) {
			int sort = (Integer) mapList.get(i).get("sort");
			int min_sort=sort;
			int index = i;
			for (int j = i+i; j < mapList.size(); j++) {
				int sort_2 = (Integer) mapList.get(j).get("sort");
				if(sort_2<min_sort){
					min_sort=sort_2;
					index = j;
				}
			}
			if(index>i){
				Map<String, Object> tmp = mapList.get(i);
				mapList.set(i, mapList.get(index));
				mapList.set(index, tmp);
			}
		}
		return mapList;
	}

	@Override
	public Set<String> getUrlSet() {
		return urlDao.getUrlSet();
	}

	@Override
	public Set<String> getUrlSet(Integer userId) {
		List<Integer> roleList = userRoledao.getUserRole(userId);
		if(roleList==null || roleList.isEmpty())return null;
		Set<Integer> menuIds = new HashSet<Integer>();
		for (Integer roleId : roleList) {
			List<Map<String, Object>> menuList = roleMenudao.loadMenu(roleId);
			if(menuList==null || menuList.isEmpty())continue;
			for (Map<String, Object> map : menuList) {
				if(map==null || map.isEmpty())continue;
				Integer menuId = (Integer) map.get("id");
				menuIds.add(menuId);
			}
		}
		if(menuIds==null || menuIds.isEmpty())return null;
		return urlDao.getUrlSet(menuIds);
	}

	@Override
	public int resetPassword(Integer siteId) {
		Map<String, Object> admin = dao.getAdmin(siteId);
		admin.put("password", CryptUtil.MD5encrypt("123456"));
		admin.put("updateTime", new Date());
		return dao.update(admin);
	}

	@Override
	public int updatePassword(Integer siteId, Integer userId, String password,
			String newPassword) {
		return dao.updatePassword(siteId, userId, password, newPassword);
	}

}
