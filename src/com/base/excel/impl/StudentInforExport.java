package com.base.excel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.model.Certificate;
import com.apply.model.Student;
import com.authority.model.User;
import com.base.dao.SQLDao;
import com.base.excel.Export;
import com.base.util.StringUtil;

@Service
public class StudentInforExport implements Export {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		String name=request.getParameter("graduate");
		String graduate=request.getParameter("name");
		String domicile=request.getParameter("domicile");
		String status=request.getParameter("status");
		String admit = request.getParameter("admit");
		String examYear = request.getParameter("examYear");
		if(siteId==null)return null;
		String sql = "select name,gender,certificateType,certificate,enrollmentNumbers,nationality,"+
		"nativePlace,domicile,homeAddress,graduate,birthday,phoneNumber,chinese,maths,english,interviewScore,"+
		"(total+0.2*(IF(interviewScore IS NULL,0,interviewScore))) AS allTotal,IF(interview=1,'是','否') interview, IF(admit=1,'是',IF(admit=-1,'待公布','否')) admit,createTime,interviewDate,fullName1,relationship1,telephone1,unit1,position1,"+
		"fullName2,relationship2,telephone2,unit2,position2,rewardHobby from "+Student.tableName+" where siteId=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(siteId);
		if (!StringUtil.isEmpty(name)) {
			sql += " and name like ? ";
			params.add("%"+name+"%");
		}
		if (!StringUtil.isEmpty(graduate)) {
			sql += " and ( graduate like ?  or  graduateProvince like ?  or  graduateCity like ?  or  graduateArea like ?  ) ";
			params.add("%"+graduate+"%");
			params.add("%"+graduate+"%");
			params.add("%"+graduate+"%");
			params.add("%"+graduate+"%");
		}
		if (!StringUtil.isEmpty(domicile)) {
			sql += " and (domicile like ? or  domiciProvince like ? or  domicilCity like ? or  domicileArea like ? )";
			params.add("%"+domicile+"%");
			params.add("%"+domicile+"%");
			params.add("%"+domicile+"%");
			params.add("%"+domicile+"%");
		}
		if (!StringUtil.isEmpty(status)) {
			sql += " and status= ? ";
			params.add(Integer.parseInt(status));
		}
		if (!StringUtil.isEmpty(admit)) {
			sql += " and admit= ? ";
			params.add(admit);
		}
		if (!StringUtil.isEmpty(examYear)) {
			sql+=" and YEAR(updateTime) = ? ";
			params.add(examYear);
		}

		List<Map<String, Object>> mapList =  sqlDao.queryForList(sql, params.toArray());
		return mapList;
	}
	  
	public List<String> certificateType(HttpServletRequest request){
		Integer siteId = User.getCurrentSiteId(request);
		return sqlDao.queryForList("select name from "+Certificate.tableName +" where siteId= ? ", String.class,siteId);
	}


}
