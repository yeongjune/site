package com.lottery.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.vo.StudentSearchVo;
import com.base.vo.PageList;
import com.lottery.dao.StudentDao;
import com.lottery.model.Student;
import com.lottery.service.StudentService;

@Service("lotteryStudentService")
public class StudentServiceImpl implements StudentService{

	@Autowired
	private StudentDao studentDao;

	@Override
	public Integer save(Student student) {
		return studentDao.save(student);
	}

	@Override
	public void update(Student student) {
		studentDao.update(student);
	}
	
	@Override
	public Integer delete(String ids) {
		return studentDao.delete(ids);
	}

	@Override
	public Student get(Integer id) {
		return studentDao.get(id);
	}

	@Override
	public PageList findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo, Integer lotteryId, Integer order) {
		return studentDao.findStudentPageList(currentPage, pageSize, searchVo, lotteryId, order);
	}

	@Override
	public int checkStudent(String stuCode, Integer lotteryId) {
		return studentDao.checkStudent(stuCode, lotteryId);
	}

	@Override
	public Integer saveBatch(List<Map<String, Object>> list) {
		return studentDao.saveBatch(list);
	}

	@Override
	public List<Map<String, Object>> getByIDCard(String IDCard, Integer lotteryId) {
		return studentDao.getByIDCard(IDCard, lotteryId);
	}

	@Override
	public List<Map<String, Object>> getByIds(String ids) {
		return studentDao.getByIds(ids);
	}

	@Override
	public List<Map<String, Object>> getByStatus(Integer status, int lotteryId, Integer order) {
		return studentDao.getByStatus(status, lotteryId, order);
	}

}
