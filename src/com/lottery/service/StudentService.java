package com.lottery.service;

import java.util.List;
import java.util.Map;

import com.apply.vo.StudentSearchVo;
import com.base.vo.PageList;
import com.lottery.model.Student;

public interface StudentService {

	/**
	 * 保存
	 * @param student
	 * @return
	 */
	Integer save(Student student);

	/**
	 * 修改
	 * @param student
	 */
	void update(Student student);
	
	/**
	 * 删除
	 * @param ids
	 */
	Integer delete(String ids);

	/**
	 * 根据id  查询
	 * @param id
	 * @return
	 */
	Student get(Integer id);

	/**
	 * 分页查询学生
	 * @param currentPage
	 * @param pageSize
	 * @param searchVo
	 * @param lotteryId 
	 * @param order 
	 * @return
	 */
	PageList findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo, Integer lotteryId, Integer order);

	/**
	 * 根据学生姓名和学校查询该学生是否已经录入数据库
	 * @param stuCode
	 * @return
	 */
	int checkStudent(String stuCode, Integer lotteryId);

	/**
	 * 批量保存学生信息
	 * @param mapList
	 */
	Integer saveBatch(List<Map<String, Object>> mapList);

	/**
	 * 根据学生身份证号和参加的摇号查询
	 * @param IDCard
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, Object>> getByIDCard(String IDCard, Integer lotteryId);

	/**
	 * 根据id  查询
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> getByIds(String ids);

	/**
	 * 根据状态查询对应摇号的学生
	 * @param status
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, Object>> getByStatus(Integer status, int lotteryId, Integer order);

}
