package com.apply.dao;

import java.util.List;
import java.util.Map;

import com.apply.model.CxStudent;
import com.apply.vo.CxStudentSearchVo;
import com.base.vo.PageList;

public interface CxStudentDao {
	/**
	 * 新增或更新长兴中学学生报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param cxStudent
	 * @return
	 */
	Integer saveOrUpdate(CxStudent cxStudent);
	
	/**
	 * 根据id删除长兴中学学生报名信息(该删除为假删除)
	 * @author lfq
	 * @time 2015-3-16
	 * @param siteId
	 * @param id
	 * @return
	 */
	Integer delete(Integer siteId,Integer ...id);
	
	/**
	 * 根据id获取长兴中学学生报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param id
	 * @return
	 */
	CxStudent getCxStudent(Integer id);
	
	/**
	 * 根据关键字分页获取长兴中学学生报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param cxStudentSearchVo		
	 * @return
	 */
	PageList<CxStudent> getCxStudentPageList(CxStudentSearchVo cxStudentSearchVo);
	
	/**
	 * 根据关键子和创建年份查询长兴中学学生报名信息列表
	 * @author lfq
	 * @time 2015-3-17
	 * @param cxStudentSearchVo
	 * @return
	 */
	List<Map<String, Object>> getCxStudentList(CxStudentSearchVo cxStudentSearchVo);
}
