package com.apply.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apply.dao.SeatDao;
import com.apply.model.Exam;
import com.apply.model.Seat;
import com.apply.model.Student;
import com.apply.model.ZdStudent;
import com.apply.model.ZdStudent2;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;

@Repository
public class SeatDaoImpl implements SeatDao {
	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(Seat seat) {
		return hqlDao.save(seat);
	}

	@Override
	public Serializable save(Map<String, Object> seat) {
		return sqlDao.save(Seat.tableName, seat);
	}
	
	@Override
	public int deleteSeatNoByExam(Integer examId) {
		String sql=" update "+Seat.tableName +" set seatNo=null and roomNo=null where examId = ? ";
		return sqlDao.update(sql, examId);
	}

	@Override
	public int update(Seat seat) {
		 hqlDao.update(seat);
		 return 1;
	}

	@Override
	public int update(Map<String, Object> seat) {
		return sqlDao.updateMap(Seat.tableName, "id", seat);
	}
	
	@Override
	public int update(List<Map<String, Object>> seatList) {
		return sqlDao.updateMapList(Seat.tableName, "id", seatList);
	}

	@Override
	public int delete(Integer id) {
		
		return sqlDao.delete(Seat.tableName, id);
	}

	@Override
	public Seat get(Integer id) {
		
		return hqlDao.get(Seat.class, id);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql=" select s.* from " +Seat.tableName+ "  s where e.id = ? ";
		return sqlDao.queryForMap(sql, id);
	}

	@Override
	public List<Map<String, Object>> findSeatList(Integer siteId,
			Integer examId, String roomNo, String examName, String studentName,
			String examDate,String examTime, Integer start, Integer limit) {
		if (siteId==null||siteId==0) {
			return new ArrayList<Map<String,Object>>();
		}
		String sql=" select s.*,e.examName,e.examArea,st.name,st.testCard,st.graduate,st.headPicUrl from " +Seat.tableName+ "  s "+
					" left JOIN "+Exam.tableName+" e on s.examId=e.id "+
					" left JOIN "+Student.tableName+" st on s.studentId=st.id  where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if (siteId!=null||siteId>0) {
			sql += " and s.siteId = ? ";
			params.add(siteId);
		}
		if (examId!=null) {
			sql += " and s.examId = ? ";
			params.add(examId);
		}
		if (!StringUtil.isEmpty(roomNo)) {
			sql += " and s.roomNo = ? ";
			params.add(roomNo);
		}
		if (!StringUtil.isEmpty(examName)) {
			sql += " and e.examName like  ? ";
			params.add("%"+examName+"%");
		}
		if (!StringUtil.isEmpty(studentName)) {
			sql += " and st.name like  ? ";
			params.add("%"+studentName+"%");
		}
		if (!StringUtil.isEmpty(examDate)) {
			sql += " and s.examDate = ? ";
			params.add(examDate);
		}
		if (!StringUtil.isEmpty(examTime)) {
			sql += " and DATE_FORMAT(s.examTime,'%Y-%m-%d') = ? ";
			params.add(examTime);
		}
		sql+=" order by e.examDate desc,e.examTime asc,s.seatNo  asc ";
		if(limit!=null && limit>0){
			if (start !=null && start >=1) {
				sql += " limit ? , ?  ";
				params.add(start-1);
				params.add(limit);
			}else{
				sql += " limit 0,? ";
				params.add(limit);
			}
		}else if (start !=null && start >=1 ){
			  sql += " limit ? ";
			  params.add(start-1);
		}
		return sqlDao.queryForList(sql, params.toArray());
	}

	@Override
	public List<Map<String, Object>> findSeatList(Integer siteId,
			Integer examId, String roomNo, String examName, String studentName,
			String examDate,String examTime, Integer start, Integer limit, Integer examType) {
		if (siteId==null||siteId==0) {
			return new ArrayList<Map<String,Object>>();
		}
		String sql=" select s.*,e.examName,e.examArea,st.name,st.testCard,st.graduate,st.headPicUrl from " +Seat.tableName+ "  s "+
				" left JOIN "+Exam.tableName+" e on s.examId=e.id ";
		
		if(examType != null && examType > 0){
			sql += " left JOIN "+ZdStudent.tableName+" st on s.studentId=st.id  where 1=1 ";
		}else if(examType != null){
			sql += " left JOIN "+ZdStudent2.tableName+" st on s.studentId=st.id  where 1=1 ";
		}
		
		List<Object> params=new ArrayList<Object>();
		if (siteId!=null||siteId>0) {
			sql += " and s.siteId = ? ";
			params.add(siteId);
		}
		if (examId!=null) {
			sql += " and s.examId = ? ";
			params.add(examId);
		}
		if (!StringUtil.isEmpty(roomNo)) {
			sql += " and s.roomNo = ? ";
			params.add(roomNo);
		}
		if (!StringUtil.isEmpty(examName)) {
			sql += " and e.examName like  ? ";
			params.add("%"+examName+"%");
		}
		if (!StringUtil.isEmpty(studentName)) {
			sql += " and st.name like  ? ";
			params.add("%"+studentName+"%");
		}
		if (!StringUtil.isEmpty(examDate)) {
			sql += " and s.examDate = ? ";
			params.add(examDate);
		}
		if (!StringUtil.isEmpty(examTime)) {
			sql += " and DATE_FORMAT(s.examTime,'%Y-%m-%d') = ? ";
			params.add(examTime);
		}
		sql+=" order by e.examDate desc,e.examTime asc,s.seatNo  asc ";
		if(limit!=null && limit>0){
			if (start !=null && start >=1) {
				sql += " limit ? , ?  ";
				params.add(start-1);
				params.add(limit);
			}else{
				sql += " limit 0,? ";
				params.add(limit);
			}
		}else if (start !=null && start >=1 ){
			sql += " limit ? ";
			params.add(start-1);
		}
		return sqlDao.queryForList(sql, params.toArray());
	}
	
	@Override
	public PageList findSeatPageList(Integer currentPage, Integer pageSize,
			Integer siteId, Integer examId, String roomNo, String examName,
			String studentName, String examDate,String examTime) {
		if (siteId==null||siteId==0) {
			return null;
		}
		String sql=" select s.*,e.examName,e.examArea,st.name,st.testCard,st.graduate  from " +Seat.tableName+ "  s "+
					" left JOIN "+Exam.tableName+" e on s.examId=e.id "+
					" left JOIN "+Student.tableName+" st on s.studentId=st.id  where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if (siteId!=null||siteId>0) {
			sql += " and s.siteId = ? ";
			params.add(siteId);
		}
		if (examId!=null) {
			sql += " and s.examId = ? ";
			params.add(examId);
		}
		if (!StringUtil.isEmpty(roomNo)) {
			sql += " and s.roomNo = ? ";
			params.add(roomNo);
		}
		if (!StringUtil.isEmpty(examName)) {
			sql += " and e.examName like  ? ";
			params.add("%"+examName+"%");
		}
		if (!StringUtil.isEmpty(studentName)) {
			sql += " and st.name like  ? ";
			params.add("%"+studentName+"%");
		}
		if (!StringUtil.isEmpty(examDate)) {
			sql += " and s.examDate = ? ";
			params.add(examDate);
		}
		if (!StringUtil.isEmpty(examTime)) {
			sql += " and DATE_FORMAT(s.examTime,'%Y-%m-%d') = ? ";
			params.add(examTime);
		}
		sql+=" order by s.roomNo asc,s.watingRoomNo asc,s.seatNo asc ";
		
		return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
	}

	@Override
	public PageList findSeatPageList(Integer currentPage, Integer pageSize,
			Integer siteId, Integer examId, String roomNo, String examName,
			String studentName, String examDate,String examTime, Integer examType) {
		if (siteId==null||siteId==0) {
			return null;
		}
		String sql=" select s.*,e.examName,e.examArea,st.name,st.testCard,st.graduate  from " +Seat.tableName+ "  s "+
				" LEFT JOIN "+Exam.tableName+" e on s.examId=e.id ";
		
		if(examType > 0){//初中
			sql += " LEFT JOIN "+ZdStudent.tableName+" st on s.studentId=st.id  where 1=1 ";
		}else{//小学
			sql += " LEFT JOIN "+ZdStudent2.tableName+" st on s.studentId=st.id  where 1=1 ";
		}
		
		List<Object> params=new ArrayList<Object>();
		if (siteId!=null||siteId>0) {
			sql += " and s.siteId = ? ";
			params.add(siteId);
		}
		if (examId!=null) {
			sql += " and s.examId = ? ";
			params.add(examId);
		}
		if (!StringUtil.isEmpty(roomNo)) {
			sql += " and s.roomNo = ? ";
			params.add(roomNo);
		}
		if (!StringUtil.isEmpty(examName)) {
			sql += " and e.examName like  ? ";
			params.add("%"+examName+"%");
		}
		if (!StringUtil.isEmpty(studentName)) {
			sql += " and st.name like  ? ";
			params.add("%"+studentName+"%");
		}
		if (!StringUtil.isEmpty(examDate)) {
			sql += " and s.examDate = ? ";
			params.add(examDate);
		}
		if (!StringUtil.isEmpty(examTime)) {
			sql += " and DATE_FORMAT(s.examTime,'%Y-%m-%d') = ? ";
			params.add(examTime);
		}
		sql+=" order by s.roomNo asc,s.watingRoomNo asc,s.seatNo asc ";
		
		return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
	}

	@Override
	public int deleteByExam(Integer examId) {
		if (examId==null||examId<0) {
			return 0;
		}
		return sqlDao.delete(Seat.tableName, "examId", examId);
	}
	
	@Override
	public int deleteByStudentIds(String studentIds) {
		if (StringUtil.isEmpty(studentIds)) {
			return 0;
		}
		String sql=" delete from "+Seat.tableName +" where  studentId in ( ";
		List<Object> params=new ArrayList<Object>();
		String [] arryStudentIds=studentIds.split(",");
		for (int i = 0; i < arryStudentIds.length; i++) {
			sql += " ? ";
			if (i!=arryStudentIds.length-1) {
				sql += " , ";
			}
			params.add(arryStudentIds[i]);
		}
		sql +=" ) ";
		return sqlDao.update(sql, params.toArray());
	}

	@Override
	public int deleteByExamAndStudent(Integer examId, String studentIds) {
		if (examId==null||examId<0||StringUtil.isEmpty(studentIds)) {
			return 0;
		}
		String sql=" delete from "+Seat.tableName +" where examId = ? and studentId in ( ";
		List<Object> params=new ArrayList<Object>();
		params.add(examId);
		String [] arryStudentIds=studentIds.split(",");
		for (int i = 0; i < arryStudentIds.length; i++) {
			sql += " ? ";
			if (i!=arryStudentIds.length-1) {
				sql += " , ";
			}
			params.add(arryStudentIds[i]);
		}
		sql +=" ) ";
		return sqlDao.update(sql, params.toArray());
	}

	@Override
	public int save(List<Map<String, Object>> listSeat) {
		return sqlDao.save(Seat.tableName, listSeat);
	}

	@Override
	public boolean hasExist(Integer siteId, Integer examId, Integer studentId) {
		String sql=" select count(s.id) from "+Seat.tableName +" s where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		int count=0;
		if (siteId!=null && siteId>0) {
			sql += " and s.siteId = ? ";
			params.add(siteId);
		}
		if (examId!=null && examId>0) {
			sql += " and s.examId = ? ";
			params.add(examId);
		}
		if (studentId!=null && studentId>0) {
			sql += " and s.studentId = ? ";
			params.add(studentId);
		}
		if (params.size()>0) {
			count=sqlDao.queryForInt(sql, params.toArray());
		}else{
			count=sqlDao.queryForInt(sql);
		}
		return count>0?true:false;
	}

	@Override
	public Map<String, Object> findMaxSeatByExam(Integer examId) {
		String sql=" select count(s.id) from "+ Seat.tableName+" s where s.examId=? and s.seatNo <> '' and s.seatNo is not null ";
		int count=sqlDao.queryForInt(sql, examId);
		if (count>0) {
			sql=" select s.* from "+ Seat.tableName+" s where s.examId=? amd s.roomNo=(select max(t.roomNo) from "+ Seat.tableName+" t where t.examId= ? )";
			return sqlDao.queryForMap(sql, examId,examId);
		}else{
			return null;
		}
	}

	@Override
	public List<Map<String, Object>> getStudentSeatList(Integer siteId, Integer studentId) {
		String sql=" select s.*,e.examName,e.examDate,e.examTime from "+ Seat.tableName + " as s "+
				" left join  "+ Exam.tableName + " as e on  s.examId = e.id " +
				" WHERE s.studentId = ? " +
				" AND s.siteId = ? order by e.examDate desc,e.examTime asc ";
		return sqlDao.queryForList(sql, studentId,siteId);
	}

	@Override
	public int getCountByExamId(Integer examId) {
		String sql=" select count(s.id) from  " +Seat.tableName +" s where s.examId = ? ";
		return sqlDao.queryForInt(sql, examId);
	}
	
	

}
