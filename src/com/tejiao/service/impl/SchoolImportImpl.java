package com.tejiao.service.impl;

import com.authority.model.User;
import com.base.config.Init;
import com.base.excel.Import;
import com.base.util.CryptUtil;
import com.base.util.StringUtil;
import com.tejiao.model.School;
import com.tejiao.service.ParentService;
import com.tejiao.service.SchoolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by riicy on 16-1-22.
 */
@Service
public class SchoolImportImpl implements Import {

    @Autowired
    private ParentService parentService;
    @Autowired
    private SchoolService schoolService;
    @Override
    public String invoke(HttpServletRequest request, HttpServletResponse response, List<Map<String, Object>> mapList) {
        Integer siteId = User.getCurrentSiteId(request);
        List<Map<String, Object>> saveList = new ArrayList<>(mapList.size());
        int col = 2;
        for (Map<String, Object> map : mapList) {
            String name = (String) map.get("name");
            String town = (String) map.get("town");
            String area = (String) map.get("area");
            String code = (String) map.get("code");
            System.out.println(name+"--"+code);
            if(StringUtil.isEmpty(name)) return "第" +col+ "行学校为空";
            if(StringUtil.isEmpty(town)) return "第" +col+ "行所属镇街为空";
            if(StringUtil.isEmpty(area)) return "第" +col+ "行区域/县为空";
            if(StringUtil.isEmpty(code)) return "第" +col+ "行学校代号为空";
            if (code.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$") && code.contains(".")) {
            	code = code.substring(0,code.indexOf("."));
            	if(code.trim().length()!=3) return "第" +col+ "行 学校代号必须为3位";
			}
            
            School s = schoolService.getByCode(code.trim(), siteId);
            if (s != null) {
            	return "第" +col+ "行学校代号已存在";
            }
            Map<String, Object> saveMap = new HashMap<>();
            saveMap.put("name", name);
            saveMap.put("town", town);
            saveMap.put("area", getArea(area));
            saveMap.put("code", code);
            saveMap.put("siteId", siteId);
            saveList.add(saveMap);
            col++;
        }
        schoolService.saveAll(saveList);
        return Init.SUCCEED;
    }
    
    private String getArea(String areaName){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("番禺区", "13");
    	map.put("越秀区", "04");
    	map.put("荔湾区", "03");
    	map.put("海珠区", "05");
    	map.put("天河区", "06");
    	map.put("白云区", "11");
    	map.put("黄埔区", "12");
    	map.put("花都区", "14");
    	map.put("南沙区", "15");
    	map.put("萝岗区", "16");
    	map.put("从化市", "84");
    	map.put("增城市", "83");
    	return StringUtil.isEmpty(areaName)?null:map.get(areaName.trim()).toString();
    }
    @Override
    public String checkedMethod(HttpServletRequest request, Map<String, Object> map) {
        return Init.SUCCEED;
    }

}
