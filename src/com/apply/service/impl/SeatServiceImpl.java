package com.apply.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.SeatDao;
import com.apply.model.Seat;
import com.apply.service.SeatService;
import com.base.vo.PageList;

@Service
public class SeatServiceImpl implements SeatService {
	@Autowired
	private SeatDao seatDao;

	@Override
	public Serializable save(Seat seat) {
		
		return seatDao.save(seat);
	}

	@Override
	public Serializable save(Map<String, Object> seat) {
		
		return seatDao.save(seat);
	}

	@Override
	public int update(Seat seat) {
		
		return seatDao.update(seat);
	}

	@Override
	public int update(Map<String, Object> seat) {
		
		return seatDao.update(seat);
	}

	@Override
	public int delete(Integer id) {
		
		return seatDao.delete(id);
	}

	@Override
	public Seat get(Integer id) {
		
		return seatDao.get(id);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		
		return seatDao.load(id);
	}

	@Override
	public List<Map<String, Object>> findSeatList(Integer siteId,
			Integer examId, String roomNo, String examName, String studentName,
			String examDate, String examTime, Integer start, Integer limit) {
		
		return seatDao.findSeatList(siteId, examId, roomNo, examName, studentName, examDate, examTime, start, limit);
	}

	@Override
	public List<Map<String, Object>> findSeatList(Integer siteId,
			Integer examId, String roomNo, String examName, String studentName,
			String examDate, String examTime, Integer start, Integer limit, Integer examType) {
		
		return seatDao.findSeatList(siteId, examId, roomNo, examName, studentName, examDate, examTime, start, limit, examType);
	}

	@Override
	public PageList findSeatPageList(Integer currentPage, Integer pageSize,
			Integer siteId, Integer examId, String roomNo, String examName,
			String studentName, String examDate, String examTime) {
		
		return seatDao.findSeatPageList(currentPage, pageSize, siteId, examId, roomNo, examName, studentName, examDate, examTime);
	}

	@Override
	public PageList findSeatPageList(Integer currentPage, Integer pageSize,
			Integer siteId, Integer examId, String roomNo, String examName,
			String studentName, String examDate, String examTime, Integer examType) {
		
		return seatDao.findSeatPageList(currentPage, pageSize, siteId, examId, roomNo, examName, studentName, examDate, examTime, examType);
	}

	@Override
	public int deleteByExam(Integer examId) {
		return seatDao.deleteByExam(examId);
	}

	@Override
	public int deleteByExamAndStudent(Integer examId, String studentIds) {
		return seatDao.deleteByExamAndStudent(examId, studentIds);
	}

	@Override
	public List<Map<String, Object>> getStudentList(Integer siteId,
			Integer studentId) {
		return seatDao.getStudentSeatList(siteId, studentId);
	}

	@Override
	public int getCountByExamId(Integer examId) {
		
		return seatDao.getCountByExamId(examId);
	}

}
