package com.base.excel.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.model.Student;
import com.authority.model.User;
import com.base.dao.SQLDao;
import com.base.excel.Export;

@Service
public class InterviewDateAndBatchExport implements Export {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		if(siteId==null) return null;
		List<Object> params=new ArrayList<Object>();
		String sql = "select graduate, name, gender,phoneNumber,interviewDate " +
					" from "+Student.tableName+" where siteId=? "+
					" AND DATE_FORMAT(updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startDate,'%Y-%m-%d') FROM apply_recruitDate r WHERE r.siteId= ? LIMIT 1) "+
					" AND DATE_FORMAT(updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endDate,'%Y-%m-%d') FROM apply_recruitDate r WHERE r.siteId= ? LIMIT 1) "+
					" AND status=1 "+
					" AND (interview = 0 or interview is null ) " +
					" AND ( admit = -1 or admit is null ) ";
		//导出是当前报名期内已经审核并且尚未录取的学生
		params.add(siteId);
		params.add(siteId);
		params.add(siteId);
		
		return sqlDao.queryForList(sql, params.toArray());
	}

}
