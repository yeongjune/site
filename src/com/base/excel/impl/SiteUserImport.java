package com.base.excel.impl;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.model.User;
import com.base.excel.Import;
import com.base.util.CryptUtil;
import com.site.service.SiteUserService;

@Service
public class SiteUserImport implements Import{

	@Autowired
	private SiteUserService siteUserService;
	
	@Override
	public String invoke(HttpServletRequest request,
			HttpServletResponse response, List<Map<String, Object>> mapList) {
		Integer siteId = User.getCurrentSiteId(request);
		Date date = new Date();
		//去重复帐号
		for(int i = 0; i < mapList.size() - 1; i++){
			for(int j = i + 1; j < mapList.size(); j++){
				if(mapList.get(i).get("acount").toString().equals(mapList.get(j).get("acount").toString())){
					mapList.remove(j);
				}
			}
		}
		//检查是否已有相应帐号
		ListIterator<Map<String, Object>> listIterator = mapList.listIterator();
		while(listIterator.hasNext()){
			Map<String, Object> map = listIterator.next();
			int count = siteUserService.checkAccount(siteId, map.get("acount").toString());
			if(count <= 0){
				map.put("siteId", siteId);
				map.put("createTime", date);
			}else{
				listIterator.remove();
			}
		}
		siteUserService.saveBatch(mapList);
		return "succeed";
	}

	@Override
	public String checkedMethod(HttpServletRequest request,
			Map<String, Object> map) {
		if(map.get("帐号") != null && map.get("密码") != null && !map.get("帐号").toString().equals("") && !map.get("密码").toString().equals("")){
				map.put("密码", CryptUtil.MD5encrypt(map.get("密码").toString()));
				return "succeed";
		}
		return "fail";
	}

}
