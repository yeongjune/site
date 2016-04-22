package com.base.excel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.model.ZdStudent;
import com.authority.model.User;
import com.base.dao.SQLDao;
import com.base.excel.Export;

@Service
public class InterviewDateAndBatchExport1 implements Export {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		String status = request.getParameter("status");
		if (siteId == null)
			return null;
		List<Object> params = new ArrayList<Object>();
		String sql = "select " +
								"id, name, gender, IDCard, birthday, inSchoolNo, isGZSchoolNo, inGzSchoolNo,position" +
								",nativePlaceProvince,nativePlaceCity,domiciProvince,domicilCity,domicileArea,addressDetail" +
								",IsPeasant,rewardHobby,homePhone,fatherPhone,fatherIDCard,matherPhone,motherIDCard" +
								",homeAddress,graduate,companyName,interviewDate,interview,interviewScore,admit,roomNo" +
								",relationship1,fullName1,unit1,telephone1,relationship2,fullName2,unit2,telephone2,testCard "
				+ " from "
				+ ZdStudent.tableName
				+ " where siteId=? and isDelete = 0"
				+ " AND DATE_FORMAT(updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "
				+ " AND DATE_FORMAT(updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "
				+ " AND (interview = 0 or interview is null ) "
				+ " AND ( admit = -1 or admit is null )";
				
		// 导出是当前报名期内已经审核并且尚未录取的学生
		params.add(siteId);
		params.add(siteId);
		params.add(siteId);

		if(status != null && status.trim().length() > 0){
			sql += " AND status = ? ";
			params.add(Integer.parseInt(status));
		}
		sql += " ORDER BY createTime ";
		
		List<Map<String, Object>> list = sqlDao.queryForList(sql, params.toArray());
		
		sql = 	"SELECT" +
						" s.id AS id," +
						" (SELECT COUNT(*) + 1  FROM " + ZdStudent.tableName + "  WHERE createTime < s.createTime) AS number" +
					"  FROM  " +
						" " + ZdStudent.tableName + " as s" +
					"  WHERE 1=1  and s.siteId = ?  and DATE_FORMAT(s.updateTime,'%Y') = ?  order by s.createTime";
		List<Map<String, Object>> numbers = sqlDao.queryForList(sql, siteId, "2015");
		
		for(Map<String, Object> map : list){
			for(Map<String, Object> m : numbers){
				if(map.get("id").equals(m.get("id"))){
					Long number = (Long) m.get("number");
					//20150001
					String numStr = "";
					if(number > 999){
						numStr = (2015 + "" + number);
					}else if(number > 99){
						numStr = (2015 + "0" + number);
					}else if(number > 9){
						numStr = (2015 + "00" + number);
					}else{
						numStr = (2015 + "000" + number);
					}
					map.put("number", numStr);
				}
			}
			
			map.put("nativePlace", map.get("nativePlaceProvince") + "省" + map.get("nativePlaceCity") + "市");
			map.put("domicile", map.get("domiciProvince") + "省" + map.get("domicilCity") + "市" + map.get("domicileArea") + "区/县" + map.get("addressDetail"));
			
			String IsPeasant = ((Integer)map.get("IsPeasant")) ==0 ? "否" : "是";
			map.put("IsPeasant",  IsPeasant);
			String interview = ((Integer)map.get("interview")) ==0 ? "否" : "是";
			map.put("interview", interview);
			Integer admit = (Integer)map.get("admit");
			String admitTip = admit == -1 ? "未公布" : (admit == 0 ? "否" : "是");
			map.put("admit", admitTip);
			
			Object isGZSchoolNo =  map.get("isGZSchoolNo");
			if(isGZSchoolNo != null){
				map.put("isGZSchoolNo", ((Integer)isGZSchoolNo) > 0 ? "是" : "否");
			}else{
				map.put("isGZSchoolNo", "否");
			}
			
			map.remove("id");
			map.remove("nativePlaceProvince");
			map.remove("nativePlaceCity");
			map.remove("domiciProvince");
			map.remove("domicilCity");
			map.remove("domicileArea");
			map.remove("addressDetail");
		}
		
		return list;
	}

}
