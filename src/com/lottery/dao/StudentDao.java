package com.lottery.dao;

import java.util.List;
import java.util.Map;

import com.apply.vo.StudentSearchVo;
import com.base.vo.PageList;
import com.lottery.model.Student;

public interface StudentDao {

	/**
	 * 保存
	 * @param student
	 * @return
	 */
	Integer save(Student student);

	/**
	 * 删除
	 * @param ids
	 */
	Integer delete(String ids);

	/**
	 * 修改
	 * @param student
	 */
	void update(Student student);

	/**
	 * 根据id  查询
	 * @param id
	 * @return
	 */
	Student get(Integer id);

	/**
	 * 分页查询学生信息
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
	 * 根据学生id  查询该学生是否已录入数据库
	 * @param stuCode
	 * @param lotteryId 
	 * @return
	 */
	int checkStudent(String stuCode, Integer lotteryId);

	/**
	 * 批量导入学生
	 * @param list
	 */
	Integer saveBatch(List<Map<String, Object>> list);

	/**
	 * 查询参加id对应摇号的学生（未被摇号选中的学生）
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, Object>> getByLotteryId(Integer lotteryId);

	/**
	 * 查询参加id对应摇号的学生（未被摇号选中的学生）教育局用
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, Object>> getByLotteryId1(Integer lotteryId);

	/**
	 * 保存列表中的所有数据
	 * @param list
	 */
	Integer saveOrUpdateAll(List<Map<String, Object>> list);

	/**
	 * 根据学生的身份证号和参加的摇号进行查询
	 * @param idCard
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, Object>> getByIDCard(String IDCard, Integer lotteryId);

	/**
	 * 根据id 查询
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

	/**
	 * 查询摇号的信息
	 * @param id
	 * @return
	 */
	Map<String, Object> load(Integer id);

	/**
	 * 删除指定摇号中的学生
	 * @param ids
	 * @return
	 */
	Integer deleteByLotteryId(String ids);

	/**
	 * 根据随机数排序查询
	 * @param lotteryId
	 * @return
	 */
	List<Map<String, Object>> getOrderByRandomNum(Integer lotteryId);

	/**
	 * 查询某次摇号中的所有学生
	 * @param lotteryId
	 * @param order	排序方式：0 不排序  1升序 2降序
	 * @return
	 */
	List<Map<String, Object>> getForSerial(Integer lotteryId, Integer order);
	
}
