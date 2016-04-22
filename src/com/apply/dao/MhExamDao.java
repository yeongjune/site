package com.apply.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.apply.model.Exam;
import com.base.vo.PageList;

public interface MhExamDao {
	
	Serializable save(Exam exam);
	
	Serializable save(Map<String, Object> exam);
	
	int update(Exam exam);
	
	int update(Map<String, Object> exam);
	
	int delete(Integer id);
	
	/**
	 * 根据id  查询考试
	 * @param id
	 * @param examType
	 * @return
	 */
	Exam get(Integer id, Integer examType);
	
	Map<String, Object> load(Integer id);
	
	/**
	 * 查询考试列表，不分页
	 * @author lfq
	 * @param siteId 站点ID，必须，否则不返回任何记录
	 * @param examName 考试名，模糊查处
	 * @return
	 */
	List<Map<String, Object>> findExamList(Integer siteId,String examName);

	int deleteBySiteAndId(Integer siteId, Integer id);
	
	/**
	 * 查询考试列表，分页
	 * @author lfq
	 * @param currentPage 	查询第几页
	 * @param pageSize 		页码大小
	 * @param siteId 		站点ID，必须，否则不返回任何记录
	 * @param examName 		考试名，模糊查处
	 * @param examType 
	 * @return PageList
	 */
	PageList findExamPageList(Integer currentPage,Integer pageSize,Integer siteId,String examName, Integer examType);
}
