package com.base.excel.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.model.Seat;
import com.apply.model.Student;
import com.authority.model.User;
import com.base.dao.SQLDao;
import com.base.excel.Export;
import com.base.util.StringUtil;

@Service
public class ResultExport implements Export {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		String interviewDate = request.getParameter("interviewDate");
		String examId = request.getParameter("examId");
		if(siteId==null) return null;
		List<Object> params=new ArrayList<Object>();//chinese,maths,english,
		String sql = "select graduate, name, gender,interviewDate, interviewScore,phoneNumber,batch," +
					" IF(interview=1,'是','否') AS interview, " +
					" IF(admit=1,'是',IF(admit=-1,'待公布','否')) AS admit "+
					//" CONCAT(telephone1, IF(telephone2 IS NULL OR telephone2='','',CONCAT(',',telephone2))) AS telephones "+
					" from "+Student.tableName+" where siteId=? "+
					" AND DATE_FORMAT(updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startDate,'%Y-%m-%d') FROM apply_recruitDate r WHERE r.siteId= ? LIMIT 1) "+
					" AND DATE_FORMAT(updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endDate,'%Y-%m-%d') FROM apply_recruitDate r WHERE r.siteId= ? LIMIT 1) "+
					" AND status=1 "+
					" AND (interview = 0 or interview is null )" +
					" AND ( admit = -1 or admit is null ) ";
		//不加条件是导出是当前报名期内已经审核并且尚未录取的学生
		params.add(siteId);
		params.add(siteId);
		params.add(siteId);
		if (!StringUtil.isEmpty(interviewDate)) {
			String [] temp=interviewDate.split(",");
			sql += " and interviewDate in ( ";
			for (int i = 0; i < temp.length; i++) {
				sql+="?";
				if (i!=temp.length-1) {
					sql+=",";
				}
				params.add(temp[i]);
			}
			sql += " ) ";
		}
		if (!StringUtil.isEmpty(examId)) {
			String [] temp=examId.split(",");
			sql += " and id in ( select studentId s from "+Seat.tableName+" s where s.examId in ( ";
			for (int i = 0; i < temp.length; i++) {
				sql+=" ? ";
				if (i!=temp.length-1) {
					sql+=",";
				}
				params.add(temp[i]);
			}
			sql += " ) ) ";
		}
		sql +=" order by interviewDate asc,batch asc ";
		return sqlDao.queryForList(sql, params.toArray());
	}

}
