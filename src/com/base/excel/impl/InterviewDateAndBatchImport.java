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
public class InterviewDateAndBatchImport implements Import {

	@Autowired
	private SQLDao dao;

	@Override
	public String invoke(HttpServletRequest request,
			HttpServletResponse response, List<Map<String, Object>> mapList) {
		if(mapList!=null && mapList.size()>0){
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> map : mapList) {
				if(map==null || map.isEmpty())continue;
				//graduate, name, gender,phoneNumber,interviewDate, batch
				list.add(map);
				map.put("status", 1);
			}
			int i = dao.updateMapList(Student.tableName, list, "graduate", "name", "gender","status");
			ExcelImport.updateImportProgress(request, "共"+list.size()+"记录，成功导入"+i+"记录，导入失败"+(list.size()-i-1)+"条");
			//ExcelImport.updateImportProgress(request, "保存数据：100%");
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
