package com.apply.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apply.dao.MhExamDao;
import com.apply.model.Exam;
import com.apply.model.Seat;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;

@Repository
public class MhExamDaoImpl implements MhExamDao {
	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(Exam exam) {
		
		return hqlDao.save(exam);
	}

	@Override
	public Serializable save(Map<String, Object> exam) {
		
		return sqlDao.save(Exam.tableName, exam);
	}

	@Override
	public int update(Exam exam) {
		 hqlDao.update(exam);
		 return 1;
	}

	@Override
	public int update(Map<String, Object> exam) {
		
		return sqlDao.updateMap(Exam.tableName, "id", exam);
	}

	@Override
	public int delete(Integer id) {
		
		return sqlDao.delete(Exam.tableName, id);
	}
	
	@Override
	public int deleteBySiteAndId(Integer siteId,Integer id) {
		String sql=" delete from "+Exam.tableName +" where siteId = ? and id = ? ";
		return sqlDao.update(sql, siteId,id);
	}

	@Override
	public Exam get(Integer id, Integer examType) {
		
		String hql = 	" FROM" +
								" " + Exam.modelName + " AS e" +
							" WHERE" +
								" e.id = ?" +
								" AND e.examType = ?";
		
		return (Exam) hqlDao.getObjectByHQL(hql, id, examType);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql=" select e.* from " +Exam.tableName+ "  e where e.id = ? ";
		return sqlDao.queryForMap(sql, id);
	}

	@Override
	public List<Map<String, Object>> findExamList(Integer siteId,String examName) {
		if (siteId==null||siteId==0) {
			return new ArrayList<Map<String,Object>>();
		}
		String sql=" select e.* from " +Exam.tableName+ "  e where e.siteId = ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(siteId);
		if (!StringUtil.isEmpty(examName)) {
			sql += " and e.examName like ? ";
			params.add(examName);
		}
		return sqlDao.queryForList(sql,params.toArray());
	}

	@Override
	public PageList findExamPageList(Integer currentPage, Integer pageSize,
			Integer siteId, String examName, Integer examType) {
		if (siteId==null||siteId==0) {
			return null;
		}
		String sql=" SELECT e.*,temp.seatCount FROM " +Exam.tableName+ "  e  "+
					" LEFT JOIN "+
					" (SELECT s.examId,COUNT(s.id) seatCount FROM " +Seat.tableName+ "  s GROUP BY s.examId ) temp "+
					" ON e.id=temp.examId "+
				   " WHERE e.siteId = ? AND e.examType = ?";
		List<Object> params=new ArrayList<Object>();
		params.add(siteId);
		params.add(examType);
		if (!StringUtil.isEmpty(examName)) {
			sql += " and e.examName like ? ";
			params.add(examName);
		}
		sql += " order by e.examDate desc,e.examTime asc ";
		return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
	}


}
