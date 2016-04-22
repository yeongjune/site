package com.base.excel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.model.Student;
import com.base.dao.SQLDao;
import com.base.excel.ExcelImport;
import com.base.excel.Import;

@Service
public class ResultImport implements Import {

	@Autowired
	private SQLDao dao;

	@Override
	public String invoke(HttpServletRequest request,
			HttpServletResponse response, List<Map<String, Object>> mapList) {
		if(mapList!=null && mapList.size()>0){
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> map : mapList) {
				if(map==null || map.isEmpty())continue;
				map.remove("telephones");
				map.remove("batch");
				map.remove("interviewDate");
				if(map.get("interview")!=null && map.get("interview").equals("是")){
					map.put("interview", 1);
				}else{
					map.put("interview", 0);
				}
				if(map.get("admit")!=null && map.get("admit").equals("是")){
					map.put("admit", 1);
				}else if(map.get("admit")!=null && map.get("admit").equals("否")){
					map.put("admit", 0);
				}else{//待公布
					map.put("admit", -1);
				}
				map.put("status", 1);
				list.add(map);
			}
			int i = dao.updateMapList(Student.tableName, list, "graduate", "name", "gender","phoneNumber","status");
			ExcelImport.updateImportProgress(request, ""+i);
			
			ExcelImport.updateImportProgress(request, "保存数据：100%");
			return "succeed";
		}
		return "导入的数据为空";
	}

	@Override
	public String checkedMethod(HttpServletRequest request,
			Map<String, Object> map) {
		return "succeed";
	}

}
