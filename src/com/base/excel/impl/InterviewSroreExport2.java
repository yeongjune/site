package com.base.excel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.model.ZdStudent2;
import com.authority.model.User;
import com.base.dao.SQLDao;
import com.base.excel.Export;

@Service
public class InterviewSroreExport2 implements Export {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		String ids = request.getParameter("ids");
		String[] idArr = ids.split(",");
		if(siteId==null) return null;
		List<Object> params=new ArrayList<Object>();
		String sql = "select " +
								"graduate, name, usedName, gender,homePhone,interviewDate" +
								",IDCard,birthday,homeAddress,country,nationality,isOutside,politicsStatus,mailingAddress" +
								",companyName,interviewDate,interview,interviewScore,admit,roomNo" +
								",relationship1,fullName1,nationality1,telephone1,domicile1,IDCard1,unit1" +
								",relationship2,fullName2,nationality2,telephone2,domicile2,IDCard2,unit2" +
							" from "+ZdStudent2.tableName+" where siteId=? and isDelete = 0"+
//							" AND DATE_FORMAT(updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "+
//							" AND DATE_FORMAT(updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "+
							" AND id IN(";
		params.add(siteId);
//		params.add(siteId);
//		params.add(siteId);
		
		for(String id : idArr){
			if(id != null && id.trim().length() > 0){
				sql += "?,";
				params.add(Integer.parseInt(id));
			}
		}
		sql = sql.substring(0,sql.length() - 1) + ")";
		List<Map<String, Object>> list = sqlDao.queryForList(sql, params.toArray());
		for(Map<String, Object> m : list){
			
			String main1 = m.get("relationship1") + "  " + m.get("fullName1") + "  " + m.get("nationality1") + "  " + m.get("telephone1") + "  " + m.get("domicile1") + "  " + m.get("IDCard1") + "  " + m.get("unit1");
			String main2 = m.get("relationship2") + "  " + m.get("fullName2") + "  " + m.get("nationality2") + "  " + m.get("telephone2") + "  " + m.get("domicile2") + "  " + m.get("IDCard2") + "  " + m.get("unit2");
			m.remove("relationship1");
			m.remove("fullName1");
			m.remove("nationality1");
			m.remove("telephone1");
			m.remove("domicile1");
			m.remove("IDCard1");
			m.remove("unit1");
			m.remove("relationship2");
			m.remove("fullName2");
			m.remove("nationality2");
			m.remove("telephone2");
			m.remove("domicile2");
			m.remove("IDCard2");
			m.remove("unit2");
			m.put("main1", main1);
			m.put("main2", main2);
		}
		
		return list;
	}
}
