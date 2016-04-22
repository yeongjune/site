package com.apply.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apply.dao.ZdStudent2Dao;
import com.apply.model.RecruitDate;
import com.apply.model.ZdStudent;
import com.apply.model.ZdStudent2;
import com.apply.vo.StudentSearchVo;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;

@Repository
public class ZdStudent2DaoImpl implements ZdStudent2Dao {
	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;

	@Override
	public Serializable save(ZdStudent2 zdStudent2) {
		
		return hqlDao.save(zdStudent2);
	}

	@Override
	public int delete(String ids,Integer siteId) {
		if (StringUtil.isEmpty(ids)) {
			return 0;
		}
//		String sql=" delete from "+ZdStudent2.tableName +"  where id in ("; 
		String sql=" update "+ZdStudent2.tableName +" set isDelete = 1 where id in ("; 
		String [] idsArray=ids.split(",");
		List<Object> param=new ArrayList<Object>();
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		if (siteId!=null) {
			sql += " and siteId = ? ";
			param.add(siteId);			
		}
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public int update(Map<String, Object> zdStudent2) {
		
		return sqlDao.updateMap(ZdStudent2.tableName, "id", zdStudent2);
	}

	@Override
	public ZdStudent2 get(Integer id) {
		
		return hqlDao.get(ZdStudent2.class, id);
	}
	
	@Override
	public Map<String, Object> load(Integer id,Integer siteId) {
		String sql = " select s.*  from  " + ZdStudent2.tableName +" as s "+
				" where  s.id= ? AND s.isDelete = 0";
		if (siteId!=null) {
			sql += " and s.siteId = ? ";
			return sqlDao.queryForMap(sql, id,siteId);
		}else{
			return sqlDao.queryForMap(sql, id);
		}
	}
	
	@Override
	public Map<String, Object> loadByAccount(String account) {
		String sql = " select s.*  from  " + ZdStudent2.tableName +" s where  s.account= ?  AND s.isDelete = 0";
		return sqlDao.queryForMap(sql, account);
	}
	
	@Override
	public String finMaxAccountByName(String name) {
		String sql = " select s.account from  " + ZdStudent2.tableName + " s where  s.name= ? AND s.isDelete = 0 order by s.account desc limit 1 ";
		return sqlDao.queryForObject(sql, String.class, name);
	}
	@Override
	public List<Map<String, Object>> findCurrentStudentList(Integer siteId) {
		String sql=" SELECT s.* FROM " + ZdStudent2.tableName + " s "+ 
					" WHERE DATE_FORMAT(s.updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startDate,'%Y-%m-%d') FROM "+RecruitDate.tableName+" r WHERE r.siteId= ? LIMIT 1) "+
					" AND DATE_FORMAT(s.updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endDate,'%Y-%m-%d') FROM "+RecruitDate.tableName+" r WHERE r.siteId= ? LIMIT 1) " +
					"  AND s.isDelete = 0 "+
					" AND s.status = 1 "+
					" AND s.interviewDate is not null AND  s.interviewDate <> '' "+
					" AND (s.admit =-1 or s.admit is null )  " +
					" AND (s.interview = 0 or s.interview is null )";
		return sqlDao.queryForList(sql, siteId,siteId);
		
	}

	@Override
	public List<Map<String, Object>> findStudentList(StudentSearchVo searchVo) {
		String sql = " select s.*  from  " + ZdStudent2.tableName +" as s "+
				" where 1=1  and s.isDelete = 0 ";
		List<Object> params=new ArrayList<Object>();
		if (searchVo != null) {
			if (!StringUtil.isEmpty(searchVo.getName())) {
				sql += " and s.name like ? ";
				params.add( "%"+ searchVo.getName()+ "%");
			}
			if (searchVo.getSiteId()!=null&&searchVo.getSiteId()>0) {
				sql += " and s.siteId = ? ";
				params.add(searchVo.getSiteId());
			}
			if (!StringUtil.isEmpty(searchVo.getGender())) {
				sql += " and s.gender = ? ";
				params.add(searchVo.getGender());
			}
			if (searchVo.getStatus()!=null) {
				sql += " and s.status = ? ";
				params.add(searchVo.getStatus());
			}
			if (!StringUtil.isEmpty(searchVo.getStartTime())) {
				sql += " and  DATE_FORMAT(s.createTime,'%Y-%m-%d') >= ? ";
				params.add(searchVo.getStartTime().trim());
			}
			if (!StringUtil.isEmpty(searchVo.getEndTime())) {
				sql += " and  DATE_FORMAT(s.createTime,'%Y-%m-%d') <= ? ";
				params.add(searchVo.getEndTime().trim());
			}
			
			if (searchVo.getInterview()!=null) {
				if (searchVo.getInterview()==1) {
					sql += " and s.interview = ? ";
				}else if (searchVo.getInterview()==0){
					sql += " and  ( s.interview = ?  or s.interview is null ) ";
				}
				params.add(searchVo.getInterview());
			}
			if (searchVo.getAdmit()!=null) {
				sql += " and s.Admit = ? ";
				params.add(searchVo.getAdmit());
			}
			if (!StringUtil.isEmpty(searchVo.getGraduate())) {
				sql += " and s.graduate like ? ";
				params.add("%"+searchVo.getGraduate()+"%");
			}
			if (searchVo.getMinTotal()!=null) {
				sql += " and s.total >= ? ";
				params.add(searchVo.getMinTotal());
			}
			if (searchVo.getMaxTotal()!=null) {
				sql += " and s.total <= ? ";
				params.add(searchVo.getMaxTotal());
			}
			if (!StringUtil.isEmpty(searchVo.getDomicile())) {
				sql += " and (s.domiciProvince like ? or s.domicilCity like ? or s.domicileArea like ? or s.domicile like ? )";
				params.add("%"+searchVo.getDomicile()+"%");
				params.add("%"+searchVo.getDomicile()+"%");
				params.add("%"+searchVo.getDomicile()+"%");
				params.add("%"+searchVo.getDomicile()+"%");
			}
			if (!StringUtil.isEmpty(searchVo.getInteriewDate())) {
				sql += " and s.interViewDate = ? ";
				params.add(searchVo.getInteriewDate());
			}
			if (!StringUtil.isEmpty(searchVo.getExamYear())) {
				sql += " and DATE_FORMAT(s.updateTime,'%Y') = ? ";
				params.add(searchVo.getExamYear());
			}
			if (!StringUtil.isEmpty(searchVo.getIds())) {
				List<String> idList=Arrays.asList(searchVo.getIds().split(","));
				sql += " and s.id in (";
				for (int i=0;i<idList.size();i++) {
					sql += " ? ";
					if (i !=idList.size()-1) {
						sql += " , ";
					}
					params.add(idList.get(i));
				}
				sql += " ) ";
				
			}
		}
		sql += " order by s.createTime ";
		if (params.size()>0) {
			return sqlDao.queryForList(sql, params.toArray());
		}else{
			return sqlDao.queryForList(sql);
		}
	}

	@Override
	public PageList<ZdStudent2> findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo) {
		String sql = 	" select s.*" +
								" ,(SELECT COUNT(*) + 1  FROM " + ZdStudent2.tableName + "  WHERE createTime < s.createTime) AS number" +
							"  from  " + ZdStudent2.tableName +" as s "+
							" where 1=1 AND isDelete = 0";
		List<Object> params=new ArrayList<Object>();
		if (searchVo != null) {
			if(searchVo.getIsSetRoomNo() != null && searchVo.getIsSetRoomNo() > 1){//没设置
				sql += " and (s.roomNo is null or s.roomNo = '')";
			}else if(searchVo.getIsSetRoomNo() != null && searchVo.getIsSetRoomNo() > 0){//已设置
				sql += " and s.roomNo is not null and s.roomNo != ''";
			}
			if (!StringUtil.isEmpty(searchVo.getName())) {
				sql += " and s.name like ? ";
				params.add( "%"+ searchVo.getName()+ "%");
			}
			if (searchVo.getSiteId()!=null&&searchVo.getSiteId()>0) {
				sql += " and s.siteId = ? ";
				params.add(searchVo.getSiteId());
			}
			if (!StringUtil.isEmpty(searchVo.getGender())) {
				sql += " and s.gender = ? ";
				params.add(searchVo.getGender());
			}
			if (searchVo.getStatus()!=null) {
				sql += " and s.status = ? ";
				params.add(searchVo.getStatus());
			}
			if (!StringUtil.isEmpty(searchVo.getStartTime())) {
				sql += " and  DATE_FORMAT(s.createTime,'%Y-%m-%d') >= ? ";
				params.add(searchVo.getStartTime().trim());
			}
			if (!StringUtil.isEmpty(searchVo.getEndTime())) {
				sql += " and  DATE_FORMAT(s.createTime,'%Y-%m-%d') <= ? ";
				params.add(searchVo.getEndTime().trim());
			}
			if (searchVo.getInterview()!=null) {
				if (searchVo.getInterview()==1) {
					sql += " and s.interview = ? ";
				}else if (searchVo.getInterview()==0){
					sql += " and  ( s.interview = ?  or s.interview is null ) ";
				}
				params.add(searchVo.getInterview());
			}
			if (searchVo.getAdmit()!=null) {
				sql += " and s.Admit = ? ";
				params.add(searchVo.getAdmit());
			}
			if (!StringUtil.isEmpty(searchVo.getGraduate())) {
				sql += " and s.graduate like ? ";
				params.add("%"+searchVo.getGraduate()+"%");
			}
			if (searchVo.getMinTotal()!=null) {
				sql += " and s.total >= ? ";
				params.add(searchVo.getMinTotal());
			}
			if (searchVo.getMaxTotal()!=null) {
				sql += " and s.total <= ? ";
				params.add(searchVo.getMaxTotal());
			}
			if (!StringUtil.isEmpty(searchVo.getDomicile())) {
				sql += " and (s.domiciProvince like ? or s.domicilCity like ? or s.domicileArea like ? or s.domicile like ? )";
				params.add("%"+searchVo.getDomicile()+"%");
				params.add("%"+searchVo.getDomicile()+"%");
				params.add("%"+searchVo.getDomicile()+"%");
				params.add("%"+searchVo.getDomicile()+"%");
			}
			if (!StringUtil.isEmpty(searchVo.getInteriewDate())) {
				sql += " and s.interViewDate = ? ";
				params.add(searchVo.getInteriewDate());
			}
			if (!StringUtil.isEmpty(searchVo.getExamYear())) {
				sql += " and DATE_FORMAT(s.updateTime,'%Y') = ? ";
				params.add(searchVo.getExamYear());
			}
//			if(!searchVo.isCanInterviewDateNull()){
//				sql += " and s.interViewDate IS NOT NULL";
//			}
		}
		sql += " order by s.createTime desc ";
		if (params.size()>0) {
			return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
		}else{
			return sqlDao.getPageList(sql, currentPage, pageSize);
		}
	}
	
	@Override
	public List<String> findAllHeadPicUrl() {
		String sql=" select headPicUrl from " + ZdStudent.tableName;
		return sqlDao.queryForList(sql, String.class);
	}

	@Override
	public int update(ZdStudent2 zdStudent2) {
		 hqlDao.update(zdStudent2);
		 return zdStudent2.getId();
	}

	@Override
	public int updateStudentStatus(Integer status,String checkRemark, String ids) {
		String sql=" update "+ ZdStudent2.tableName+" set status= ?,checkRemark= ?  where id in (";
		List<Object> param=new ArrayList<Object>();
		param.add(status);
		param.add(checkRemark);
		String [] idsArray=ids.split(",");
		if (idsArray==null||idsArray.length==0) {
			return 0;
		}
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public int updateStudentPassword(String ids, String password) {
		String sql=" update "+ ZdStudent2.tableName+" set password= ? where id in (";
		List<Object> param=new ArrayList<Object>();
		param.add(password);
		String [] idsArray=ids.split(",");
		if (idsArray==null||idsArray.length==0) {
			return 0;
		}
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		System.out.println(sql);
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public List<String> findHeadPicUrlByIds(String ids) {
		if (StringUtil.isEmpty(ids)) {
			return null;
		}
		String sql=" select s.headPicUrl  from "+ZdStudent2.tableName +" s  where s.isDelete = 0 and  s.id in ( "; 
		String [] idsArray=ids.split(",");
		List<Object> param=new ArrayList<Object>();
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += ",";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		return sqlDao.queryForList(sql, String.class, param.toArray());
	}

	@Override
	public int updateStudentAdmit(String ids, Integer admit) {
		String sql=" update "+ ZdStudent2.tableName+" set admit= ? where id in (";
		List<Object> param=new ArrayList<Object>();
		param.add(admit);
		String [] idsArray=ids.split(",");
		if (idsArray==null||idsArray.length==0) {
			return 0;
		}
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public List<Map<String, Object>> findScopeReportList(String graduate,String examYear,Integer siteId) {
		if (siteId==null) {
			return null;
		}
		List<Object> params=new ArrayList<Object>();
		String sql=" SELECT "+
					" s.graduate, "+
					" COUNT(1) applyCount,  "+
					" (SELECT COUNT(1) FROM zd_student s1 WHERE s1.siteId = s.siteId AND s1.graduate=s.graduate and YEAR(s1.updateTime)=YEAR(s.updateTime) AND s1.interview=1 ) interviewCount,  "+ 
					" (SELECT COUNT(1) FROM zd_student s2 WHERE s2.siteId = s.siteId AND s2.graduate=s.graduate and YEAR(s2.updateTime)=YEAR(s.updateTime) AND s2.admit=1 ) admitCount,  "+
					" (SELECT COUNT(1) FROM zd_student s3 WHERE s3.siteId = s.siteId AND s3.graduate=s.graduate and YEAR(s3.updateTime)=YEAR(s.updateTime) AND s3.admit=0 ) notAdmitCount,  "+
					" (SELECT COUNT(1) FROM zd_student s4 WHERE s4.siteId = s.siteId AND s4.graduate=s.graduate and YEAR(s4.updateTime)=YEAR(s.updateTime) AND s4.admit=-1 ) watingAdmitCount,  "+
					" AVG(s.chinese) avgOfChinese,  "+
					" AVG(s.maths) avgOfMaths,  "+
					" AVG(s.english) avgOfEnglish, "+
					" AVG(s.interviewScore) AvgOfInterview "+
					" FROM zd_student2 s "+
					" WHERE s.siteId= ?  ";
		
		params.add(siteId);

		if (!StringUtil.isEmpty(graduate)) {
			sql+=" and s.graduate like ? ";
			params.add("%"+graduate+"%");
		}
		if (!StringUtil.isEmpty(examYear)) {
			sql+=" and YEAR(s.updateTime) = ? ";
			params.add(examYear);
		}
		sql+=" GROUP BY s.graduate ";
		return sqlDao.queryForList(sql, params.toArray());
	}


	@Override
	public String getStudentTestCard(Integer id,Date examDate) {
		String testCard="";
		ZdStudent2 student2=this.get(id);
		if (student2!=null) {
//			if (!StringUtil.isEmpty(student.getTestCard())) {
//				testCard=student.getTestCard();
//			}else{
//				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
//				String pref=dateFormat.format(examDate);
//				String sql=" select max(s.testCard) from "+Student.tableName+" s where s.siteId= ?   ";
//				Map<String, Object> maxTestCard= this.sqlDao.queryForMap(sql,student.getSiteId());
//				String temp=maxTestCard.get("testcard")+"";
//				if (StringUtil.isEmpty(temp)||"null".equals(temp)) {
//					testCard=pref+"1001";
//				}else{
//					int num=1+Integer.parseInt(temp.substring(8, temp.length()));
//					testCard=pref+num;
//				}
//				Map<String, Object> stu=new HashMap<String, Object>();
//				stu.put("id", id);
//				stu.put("testCard", testCard);
//				this.update(stu);
//			}
		}
		return testCard;
	}

	@Override
	public List<String> findGraduatesBySite(Integer siteId) {
		String sql=" select graduate from "+ZdStudent2.tableName +" where  s.isDelete = 0 and siteId = ? group by graduate ";
		return sqlDao.queryForList(sql, String.class,siteId);
	}

	@Override
	public List<Map<String, Object>> findApplyReport(String graduate,
			String examYear, Integer siteId) {
		if (siteId==null) {
			return null;
		}
		List<Object> params=new ArrayList<Object>();
		String sql=" SELECT "+
					" s.graduate, "+
					" COUNT(1) applyCount,  "+
					" (SELECT COUNT(1) FROM apply_student s1 WHERE s1.siteId = s.siteId AND s1.graduate=s.graduate and YEAR(s1.updateTime)=YEAR(s.updateTime) AND s1.interview=1 ) interviewCount,  "+ 
					" (SELECT COUNT(1) FROM apply_student s2 WHERE s2.siteId = s.siteId AND s2.graduate=s.graduate and YEAR(s2.updateTime)=YEAR(s.updateTime) AND s2.admit=1 ) admitCount,  "+
					" (SELECT COUNT(1) FROM apply_student s3 WHERE s3.siteId = s.siteId AND s3.graduate=s.graduate and YEAR(s3.updateTime)=YEAR(s.updateTime) AND s3.admit=0 ) notAdmitCount,  "+
					" (SELECT COUNT(1) FROM apply_student s4 WHERE s4.siteId = s.siteId AND s4.graduate=s.graduate and YEAR(s4.updateTime)=YEAR(s.updateTime) AND s4.admit=-1 ) watingAdmitCount,  "+
					" (SELECT COUNT(1) FROM apply_student s5 WHERE s5.siteId = s.siteId AND s5.graduate=s.graduate and YEAR(s5.updateTime)=YEAR(s.updateTime) AND s5.`status`=0 ) watingCheck, " +
					" (SELECT COUNT(1) FROM apply_student s6 WHERE s6.siteId = s.siteId AND s6.graduate=s.graduate and YEAR(s6.updateTime)=YEAR(s.updateTime) AND s6.`status`=1 ) passCheck, " +
					" (SELECT COUNT(1) FROM apply_student s7 WHERE s7.siteId = s.siteId AND s7.graduate=s.graduate and YEAR(s7.updateTime)=YEAR(s.updateTime) AND s7.`status`=2 ) notPassCheck, " +
					" (SELECT COUNT(1) FROM apply_student s8 WHERE s8.siteId = s.siteId AND s8.graduate=s.graduate and YEAR(s8.updateTime)=YEAR(s.updateTime) AND s8.`status`=3 ) backCheck " +
					" FROM apply_student2 s "+
					" WHERE s.siteId= ?  ";
		
		params.add(siteId);

		if (!StringUtil.isEmpty(graduate)) {
			sql+=" and s.graduate like ? ";
			params.add("%"+graduate+"%");
		}
		if (!StringUtil.isEmpty(examYear)) {
			sql+=" and YEAR(s.updateTime) = ? ";
			params.add(examYear);
		}
		sql+=" GROUP BY s.graduate ";
		return sqlDao.queryForList(sql, params.toArray());
	}

	@Override
	public int updateStudentInterviewDate(String ids, String interviewDate,String batch) {
		String sql=" update "+ ZdStudent2.tableName+" set interviewDate= ? ,batch=?  where id in (";
		List<Object> param=new ArrayList<Object>();
		param.add(interviewDate);
		param.add(batch);
		String [] idsArray=ids.split(",");
		if (idsArray==null||idsArray.length==0) {
			return 0;
		}
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public Integer saveOrUpdate(ZdStudent2 zdStudent2) {
		hqlDao.saveOrUpdate(zdStudent2);
		return zdStudent2.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<ZdStudent2> getZdStudentPageList(Integer siteId,Integer pageSize,
			Integer currentPage, String keyword,String year) {
		String hql="FROM "+ZdStudent2.modelName+" as s WHERE s.isDelete=0 AND s.siteId= ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(siteId);
		if (!StringUtil.isEmpty(keyword)) {
			hql +=" AND s.name like ? or s.graduate like ? or s.IDCard like ? or s.inSchoolNo like ? ";
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		if (!StringUtil.isEmpty(year)) {
			hql +=" AND DATE_FORMAT(s.createTime,'%Y')= ?  ";
			params.add(year);
		}
		hql +=" order by s.createTime desc ";
		return hqlDao.getPageListByHQL(hql, currentPage, pageSize, params.toArray());
	}
	
	@Override
	public List<Map<String, Object>> getZdStudentList(Integer siteId,String year, String keyword,Integer ...id) {
		String sql=" SELECT s.* "+
				" ,CONCAT(s.nativePlaceProvince,s.nativePlaceCity) AS nativePlace "+
				" ,IF(s.domicileArea IS NULL OR s.domicileArea='',CONCAT(s.domiciProvince,'市',s.domicilCity,'区'),CONCAT(s.domiciProvince,'省',s.domicilCity,'市',s.domicileArea)) AS domicil "+
				" ,IF(s.IsPeasant=0,'否','是') AS IsPeasant "+
				" ,CONCAT(s.relationship1,' 姓名:',s.fullName1,' 工作单位：',s.unit1,' 单位电话：',s.telephone1) AS familyMember1 "+
				" ,CONCAT(s.relationship2,' 姓名:',s.fullName2,' 工作单位：',s.unit2,' 单位电话：',s.telephone2) AS familyMember2 "+
				" ,(SELECT COUNT(*) + 1  FROM " + ZdStudent2.tableName + "  WHERE siteId = ? AND createTime < s.createTime) AS number" +
				" FROM "+ZdStudent2.tableName+" AS s "+
				" WHERE s.isDelete=0 AND s.siteId = ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(siteId);
		params.add(siteId);
		if (!StringUtil.isEmpty(keyword)) {
			sql +=" AND s.name like ? or s.graduate like ? or s.IDCard like ? or s.inSchoolNo like ? ";
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		if (!StringUtil.isEmpty(year)) {
			sql +=" AND DATE_FORMAT(s.createTime,'%Y')= ?  ";
			params.add(year);
		}
		if (id!=null && id.length>0) {
			sql += " AND s.id in ("+StringUtil.replaceCollectionToString(Arrays.asList(id))+")";
		}
		sql +=" order by s.createTime desc ";
		
		return sqlDao.queryForList(sql,params.toArray());
	}

	@Override
	public ZdStudent2 getByIDCard(String IDCard) {

		String hql =  " FROM" +
								" " + ZdStudent2.modelName + " AS zdStudent" +
							" WHERE" +
								"  zdStudent.isDelete = 0" +
								" AND zdStudent.IDCard = ?";

		return (ZdStudent2) hqlDao.getObjectByHQL(hql, IDCard);
	}
	
	@Override
	public int updateStudentRoomNo(String ids, String roomNo) {
		
		String sql =	" UPDATE" +
								" " + ZdStudent2.tableName + " AS  s" +
							" SET" +
								" s.roomNo = ?" +
							" WHERE" +
								" s.isDelete = 0" +
								" AND s.id IN(";
		
		String[] idArr = ids.split(",");
		List<Object> params = new ArrayList<Object>();
		params.add(roomNo);
		for(String id : idArr){
			sql += " ?,";
			params.add(Integer.parseInt(id));
		}
		sql = sql.substring(0, sql.length() - 1) + ")";
		
		return sqlDao.update(sql,  params.toArray());
	}

	@Override
	public ZdStudent2 getByAccount(String account) {
		String hql =  " FROM" +
				" " + ZdStudent2.modelName + " AS zdStudent" +
				" WHERE" +
				"  zdStudent.isDelete = 0" +
				" AND zdStudent.account = ?";
		return (ZdStudent2) hqlDao.getObjectByHQL(hql, account);
	}
}
