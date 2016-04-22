package com.lottery.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apply.vo.StudentSearchVo;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.lottery.dao.StudentDao;
import com.lottery.model.Lottery;
import com.lottery.model.Student;

@Repository("lotteryStudentDao")
public class StudentDaoImpl implements StudentDao{

	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;

	@Override
	public Integer save(Student student) {
		return (Integer) hqlDao.save(student);
	}

	@Override
	public Integer delete(String ids) {
		
		String sql = 	"DELETE FROM " + Student.tableName + "  WHERE id IN(" ;
		List<Object> params = new ArrayList<Object>();
		if(ids != null && ids.trim().length() > 0){
			String[] idArr = ids.split(",");
			for(String id : idArr){
				sql += "?,";
				params.add(Integer.parseInt(id));
			}
		}
		System.out.println(sql);
		sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
		return sqlDao.update(sql, params.toArray());
	}

	@Override
	public void update(Student student) {
		hqlDao.update(student);
	}
	
	@Override
	public Student get(Integer id){
		return hqlDao.get(Student.class, id);
	}

	@Override
	public PageList findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo, Integer lotteryId, Integer order) {
		String sql = 	" select s.*  from  " + Student.tableName +" as s "+
								" where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if (searchVo != null) {
			if (!StringUtil.isEmpty(searchVo.getName())) {
				sql += " and (s.name like ? or s.stuCode LIKE ?)";
				params.add( "%"+ searchVo.getName()+ "%");
				params.add( "%"+ searchVo.getName()+ "%");
			}
			if (searchVo.getStatus()!=null) {
				sql += " and s.status = ? ";
				params.add(searchVo.getStatus());
			}
		}
		sql += " AND s.lotteryId = ?";
		params.add(lotteryId);
		if(order != null && order > 1){//升序
			sql += " ORDER BY s.randomNum DESC";
		}else if(order != null && order > 0){//降序
			sql += " ORDER BY s.randomNum ";
		}
		return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
	}

	@Override
	public int checkStudent(String stuCode, Integer lotteryId) {
		
		String sql =	" SELECT" +
								" COUNT(*)" +
							" FROM" +
								" " + Student.tableName + " AS s" +
							" WHERE" +
								" s.stuCode = ?" +
								" AND s.lotteryId = ?";
		
		return sqlDao.queryForInt(sql, stuCode,  lotteryId);
	}

	@Override
	public Integer saveBatch(List<Map<String, Object>> list) {
		return sqlDao.save(Student.tableName, list);
	}

	@Override
	public List<Map<String, Object>> getByLotteryId(Integer lotteryId) {
		
		String sql = 	" SELECT" +
								" *" +
							" FROM" +
								" " + Student.tableName + " AS s" +
							" WHERE" +
								" s.lotteryId = ?" +
								" AND s.status = 0";
		
		return sqlDao.queryForList(sql, lotteryId);
	}

	@Override
	public List<Map<String, Object>> getByLotteryId1(Integer lotteryId) {
		
		String sql = 	" SELECT" +
				" *" +
				" FROM" +
				" " + Student.tableName + " AS s" +
				" WHERE" +
				" s.lotteryId = ?" +
				" AND s.status = 0" +
				" AND s.randomNum IS NULL";
		
		return sqlDao.queryForList(sql, lotteryId);
	}

	@Override
	public Integer saveOrUpdateAll(List<Map<String, Object>> list) {
		
		return sqlDao.saveOrUpdate(Student.tableName, "id", list);
		
	}

	@Override
	public List<Map<String, Object>> getByIDCard(String IDCard, Integer lotteryId) {
		
		String sql =	" SELECT" +
								" *" +
							" FROM" +
								" " + Student.tableName + " AS s" +
							" WHERE" +
								" s.IDCard = ?" +
								" AND lotteryId = ?";
		
		return sqlDao.queryForList(sql, IDCard, lotteryId);
	}

	@Override
	public List<Map<String, Object>> getByIds(String ids) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(ids != null && ids.trim().length() > 0){
			String[] idArr = ids.split(",");
			String sql =	" SELECT" +
									" *" +
								" FROM" +
									" " + Student.tableName + " AS s" +
								" WHERE" +
									" s.id IN(";
			List<Object> params = new ArrayList<Object>();
			for(String id : idArr){
				sql +=	" ?,";
				params.add(Integer.parseInt(id));
			}
			
			sql = sql.substring(0, sql.length() - 1) + ") ORDER BY s.randomNum";
			list = sqlDao.queryForList(sql, params.toArray());
		}
		
		return list;
	}

	@Override
	public List<Map<String, Object>> getByStatus(Integer status, int lotteryId, Integer order) {
		
		String sql =	" SELECT" +
								" *" +
							" FROM" +
								" " + Student.tableName + " AS s" +
							" WHERE" +
								" s.status = ?" +
								" AND s.lotteryId = ?";
		
		if(order == null || (order > 0)){
			sql += " ORDER BY s.randomNum";
		}
		return sqlDao.queryForList(sql, status, lotteryId);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		
		String sql =	" SELECT" +
								" *," +
								" (SELECT SUM(CASE WHEN s.status=1 THEN 1 ELSE 0 END ) FROM " + Student.tableName + " AS s WHERE s.lotteryId = l.id)  AS selected," +
								" (SELECT COUNT(*) FROM " + Student.tableName + " AS s WHERE s.lotteryId = l.id)  AS stuNum" +
							" FROM" +
								" " + Lottery.tableName + " AS l" +
							" WHERE" +
								" l.id = ?";
		return sqlDao.queryForMap(sql, id);
	}

	@Override
	public Integer deleteByLotteryId(String ids) {
		String sql = 	" DELETE  FROM " +
								" " + Student.tableName + 
							" WHERE" +
								" lotteryId IN(";

		if(ids != null && ids.trim().length() > 0){
			
			List<Object> params = new ArrayList<Object>();
			String[] idArr = ids.split(",");
			for(String id : idArr){
				sql += "?,";
				params.add(Integer.parseInt(id));
			}
			
			sql = sql.substring(0, sql.length() - 1) + ")";
			return sqlDao.update(sql, params.toArray());
		}else{
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> getOrderByRandomNum(Integer lotteryId) {
		
		String sql = 	" SELECT" +
				" *" +
				" FROM" +
				" " + Student.tableName + " AS s" +
				" WHERE" +
				" s.lotteryId = ?" +
				" AND s.status = 0" +
				" ORDER BY s.randomNum";
		
		return sqlDao.queryForList(sql, lotteryId);
	}

	@Override
	public List<Map<String, Object>> getForSerial(Integer lotteryId,
			Integer order) {
		
		String sql =	" SELECT" +
								" *" +
							" FROM" +
								" " + Student.tableName + " AS s " +
							" WHERE" +
								" s.lotteryId = ?";
		
		if(order > 1){//降序
			sql += " ORDER BY s.randomNum DESC";
		}else if(order > 0){//升序
			sql += " ORDER BY s.randomNum";
		}
		
		return sqlDao.queryForList(sql, lotteryId);
	}
	
}
