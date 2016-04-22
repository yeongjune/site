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
public class InterviewDateAndBatchExport2 implements Export {

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		if(siteId==null) return null;
		List<Object> params=new ArrayList<Object>();
		String sql = 	"select " +
								" id,name,name,namePinyin,usedName,gender,birthday,birthdayAddress" +
								",country,nationality,politicsStatus,isOutside,IDCard,graduate,healthyCondition" +
								",rewardHobby,nativePlaceProvince,nativePlaceCity,domiciProvince,domicilCity,domicileArea,addressDetail" +
								",peasant,homeAddress,mailingAddress,homePhone,companyName,remark,interview,interviewScore,admit,interviewDate" +
								",relationship1,fullName1,nationality1,unit1,domicile1,IDCard1" +
								",relationship2,fullName2,nationality2,unit2,domicile2,IDCard2" +
							" from "+ZdStudent2.tableName+" where siteId=? and isDelete = 0"+
							" AND DATE_FORMAT(updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "+
							" AND DATE_FORMAT(updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endTime,'%Y-%m-%d') FROM apply_info r WHERE r.siteId= ? LIMIT 1) "+
							" AND status=1 "+
							" AND (interview = 0 or interview is null ) " +
							" AND ( admit = -1 or admit is null ) " +
							" ORDER BY createTime DESC";
		//导出是当前报名期内已经审核并且尚未录取的学生
		params.add(siteId);
		params.add(siteId);
		params.add(siteId);
		
		List<Map<String,Object>> list = sqlDao.queryForList(sql, params.toArray());
		
		sql = 	"SELECT" +
						" s.id AS id," +
						" (SELECT COUNT(*) + 1  FROM " + ZdStudent2.tableName + "  WHERE createTime < s.createTime) AS number" +
					"  FROM  " +
						" " + ZdStudent2.tableName + " as s" +
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
			
			String interview = ((Integer)map.get("interview")) ==0 ? "否" : "是";
			map.put("interview", interview);
			Integer admit = (Integer)map.get("admit");
			String admitTip = admit == -1 ? "未公布" : (admit == 0 ? "否" : "是");
			map.put("admit", admitTip);
			
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
