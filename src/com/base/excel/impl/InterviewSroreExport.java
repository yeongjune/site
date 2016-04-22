package com.base.excel.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.model.ZdStudent;
import com.authority.model.User;
import com.base.dao.SQLDao;
import com.base.excel.Export;
import com.base.util.StringUtil;

@Service
public class InterviewSroreExport implements Export {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		if(!StringUtil.isEmpty(ids)){
			Integer siteId = User.getCurrentSiteId(request);
			String[] idArr = ids.split(",");
			if(siteId==null) return null;
			List<Object> params=new ArrayList<Object>();
			String sql = "select " +
					"graduate, name, gender,homePhone,interviewDate" +
					",IDCard,birthday,fatherPhone,matherPhone,homeAddress" +
					",companyName,interviewDate,interview,interviewScore" +
					",admit,fatherIDCard,motherIDCard,roomNo " +
					" from "+ZdStudent.tableName+" where siteId=? and isDelete = 0"+
					" AND DATE_FORMAT(updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "+
					" AND DATE_FORMAT(updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "+
					" AND status=1" +
					" AND id IN(";
			params.add(siteId);
			params.add(siteId);
			params.add(siteId);
			
			for(String id : idArr){
				if(id != null && id.trim().length() > 0){
					sql += "?,";
					params.add(Integer.parseInt(id));
				}
			}
			sql = sql.substring(0,sql.length() - 1) + ")";
			return sqlDao.queryForList(sql, params.toArray());
		}
		return null;
	}

}
