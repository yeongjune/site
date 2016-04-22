package com.apply.dao.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apply.dao.StudentDao;
import com.apply.model.ApplyInfo;
import com.apply.model.RecruitDate;
import com.apply.model.Student;
import com.apply.model.ZdStudent;
import com.apply.model.ZdStudent2;
import com.apply.vo.StudentSearchVo;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;

@Repository
public class StudentDaoImpl implements StudentDao {
	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;

	@Override
	public Serializable save(Student student) {
		
		return hqlDao.save(student);
	}

	@Override
	public int delete(String ids,Integer siteId) {
		if (StringUtil.isEmpty(ids)) {
			return 0;
		}
		String sql=" delete from "+Student.tableName +"  where id in ("; 
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
	public int update(Map<String, Object> student) {
		
		return sqlDao.updateMap(Student.tableName, "id", student);
	}

	@Override
	public Student get(Integer id) {
		
		return hqlDao.get(Student.class, id);
	}
	
	@Override
	public Map<String, Object> load(Integer id,Integer siteId) {
		String sql = " select s.*  from  " + Student.tableName +" as s "+
				" where  s.id= ? ";
		if (siteId!=null) {
			sql += " and s.siteId = ? ";
			return sqlDao.queryForMap(sql, id,siteId);
		}else{
			return sqlDao.queryForMap(sql, id);
		}
	}
	
	@Override
	public Map<String, Object> loadByAccount(String account) {
		String sql = " select s.*  from  " + Student.tableName +" s where  s.account= ? ";
		return sqlDao.queryForMap(sql, account);
	}
	
	@Override
	public String finMaxAccountByName(String name) {
		String sql = " select s.account from  " + Student.tableName + " s where  s.name= ? order by s.account desc limit 1 ";
		return sqlDao.queryForObject(sql, String.class, name);
	}
	@Override
	public List<Map<String, Object>> findCurrentStudentList(Integer siteId) {
		String sql=" SELECT s.* FROM " + Student.tableName + " s "+ 
					" WHERE DATE_FORMAT(s.updateTime,'%Y-%m-%d') >= (SELECT DATE_FORMAT(r.startDate,'%Y-%m-%d') FROM "+RecruitDate.tableName+" r WHERE r.siteId= ? LIMIT 1) "+
					" AND DATE_FORMAT(s.updateTime,'%Y-%m-%d') <= (SELECT DATE_FORMAT(r.endDate,'%Y-%m-%d') FROM "+RecruitDate.tableName+" r WHERE r.siteId= ? LIMIT 1) "+
					" AND s.status = 1 "+
					" AND s.interviewDate is not null AND  s.interviewDate <> '' "+
					" AND (s.admit =-1 or s.admit is null )  " +
					" AND (s.interview = 0 or s.interview is null )";
		return sqlDao.queryForList(sql, siteId,siteId);
		
	}

	@Override
	public List<Map<String, Object>> findStudentList(StudentSearchVo searchVo) {
		String sql = " select s.*  from  " + Student.tableName +" as s "+
				" where 1=1 ";
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
	public PageList findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo) {
		String sql = " select s.*  from  " + Student.tableName +" as s "+
				" where 1=1 ";
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
		String sql=" select headPicUrl from " + Student.tableName;
		return sqlDao.queryForList(sql, String.class);
	}

	@Override
	public int update(Student student) {
		 hqlDao.update(student);
		 return student.getId();
	}

	@Override
	public int updateStudentStatus(Integer status,String checkRemark, String ids) {
		String sql=" update "+ Student.tableName+" set status= ?,checkRemark= ?  where id in (";
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
		String sql=" update "+ Student.tableName+" set password= ? where id in (";
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
		String sql=" select s.headPicUrl  from "+Student.tableName +" s  where s.id in ( "; 
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
		String sql=" update "+ Student.tableName+" set admit= ? where id in (";
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
					" (SELECT COUNT(1) FROM apply_student s1 WHERE s1.siteId = s.siteId AND s1.graduate=s.graduate and YEAR(s1.updateTime)=YEAR(s.updateTime) AND s1.interview=1 ) interviewCount,  "+ 
					" (SELECT COUNT(1) FROM apply_student s2 WHERE s2.siteId = s.siteId AND s2.graduate=s.graduate and YEAR(s2.updateTime)=YEAR(s.updateTime) AND s2.admit=1 ) admitCount,  "+
					" (SELECT COUNT(1) FROM apply_student s3 WHERE s3.siteId = s.siteId AND s3.graduate=s.graduate and YEAR(s3.updateTime)=YEAR(s.updateTime) AND s3.admit=0 ) notAdmitCount,  "+
					" (SELECT COUNT(1) FROM apply_student s4 WHERE s4.siteId = s.siteId AND s4.graduate=s.graduate and YEAR(s4.updateTime)=YEAR(s.updateTime) AND s4.admit=-1 ) watingAdmitCount,  "+
					" AVG(s.chinese) avgOfChinese,  "+
					" AVG(s.maths) avgOfMaths,  "+
					" AVG(s.english) avgOfEnglish, "+
					" AVG(s.interviewScore) AvgOfInterview "+
					" FROM apply_student s "+
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
		Student student=this.get(id);
		if (student!=null) {
			if (!StringUtil.isEmpty(student.getTestCard())) {
				testCard=student.getTestCard();
			}else{
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
				String pref=dateFormat.format(examDate);
				String sql=" select max(s.testCard) from "+Student.tableName+" s where s.siteId= ?   ";
				Map<String, Object> maxTestCard= this.sqlDao.queryForMap(sql,student.getSiteId());
				String temp=maxTestCard.get("testcard")+"";
				if (StringUtil.isEmpty(temp)||"null".equals(temp)) {
					testCard=pref+"1001";
				}else{
					int num=1+Integer.parseInt(temp.substring(8, temp.length()));
					testCard=pref+num;
				}
				Map<String, Object> stu=new HashMap<String, Object>();
				stu.put("id", id);
				stu.put("testCard", testCard);
				this.update(stu);
			}
		}
		return testCard;
	}

	@Override
	public List<String> findGraduatesBySite(Integer siteId) {
		String sql=" select graduate from "+Student.tableName +" where siteId = ? group by graduate ";
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
					" FROM apply_student s "+
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
		String sql=" update "+ Student.tableName+" set interviewDate= ? ,batch=?  where id in (";
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
	public Map<String, Object> findByIDCard(String idCard, Integer level) {
		String sql = "select * from ";
		if(level == 0){
			sql += ZdStudent2.tableName + " AS student WHERE student.IDCard = ?";
		}else{
			sql += ZdStudent.tableName + " AS student WHERE student.IDCard = ?";
		}
		return sqlDao.queryForMap(sql, idCard);
	}

	@Override
	public Map<String, Object> load4MH(Integer studentId, Integer siteId,
			Integer level) {
		
		String sql = "select * from ";
		if(level == 0){
			sql += ZdStudent2.tableName + " AS student WHERE student.id = ? AND student.siteId = ?";
		}else{
			sql += ZdStudent.tableName + " AS student WHERE student.id = ?  AND student.siteId = ?";
		}
		
		return sqlDao.queryForMap(sql, studentId, siteId);
	}

	@Override
	public ApplyInfo getApplyInfo(Integer siteId, Integer level) {
		
		String hql = 	" FROM" +
								" " + ApplyInfo.modelName + " AS a" +
							" WHERE" +
								" a.siteId = ?" +
								" AND a.applyNo = ?";
		
		String applyNo = "0" + (2 - level);
		
		return (ApplyInfo) hqlDao.getObjectByHQL(hql, siteId, applyNo);
	}

	@Override
	public Map<String, Object> findByAccount(String account, Integer level) {
		String sql = "select * from ";
		if(level == 0){
			sql += ZdStudent2.tableName + " AS student WHERE student.account = ?";
		}else{
			sql += ZdStudent.tableName + " AS student WHERE student.account = ?";
		}
		return sqlDao.queryForMap(sql, account);
	}

}
