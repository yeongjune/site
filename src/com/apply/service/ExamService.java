package com.apply.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.apply.model.Exam;
import com.base.vo.PageList;

public interface ExamService {
	
	Serializable save(Exam exam);
	
	Serializable save(Map<String, Object> exam);
	
	int update(Exam exam);
	
	int update(Map<String, Object> exam);
	
	int delete(Integer id);
	
	int delete(Integer siteId, Integer id);
	
	Exam get(Integer id);
	
	Map<String, Object> load(Integer id);
	
	/**
	 * 查询考试列表,不分页
	 * @author lfq
	 * @param siteId 站点ID，必须，否则不返回任何记录
	 * @param examName 考试名，模糊查处
	 * @return
	 */
	List<Map<String, Object>> findExamList(Integer siteId,String examName);
	
	/**
	 * 查询考试列表，分页
	 * @author lfq
	 * @param currentPage 	查询第几页
	 * @param pageSize 		页码大小
	 * @param siteId 		站点ID，必须，否则不返回任何记录
	 * @param examName 		考试名，模糊查处
	 * @return PageList
	 */
	PageList findExamPageList(Integer currentPage,Integer pageSize,Integer siteId,String examName);
	
	/**
	 * 保存参考学生
	 * @author lfq
	 * @param examId 	参加的考试ID
	 * @param studentIds 学生ID，多个用逗号隔开
	 * @return
	 */
	public int saveStudentSeat(Integer examId,String studentIds);
	
	/**
	 * 生成座位号或更新座位号
	 * @author lfq
	 * @param examId
	 * @param clearAllSeatNO 是否全部重新生成座位号
	 * @return
	 */
	public int updateSeatNoAndRoomNo(Integer examId,Boolean clearAllSeatNO);

	/**
	 * 分配考场
	 * @author lifq
	 * @param examId
	 * @return
	 */
	int updateDeployRoom(Integer examId);
}
