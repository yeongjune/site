package com.apply.service;

import java.util.List;
import java.util.Map;

import com.apply.model.JxStudent;
import com.apply.vo.JxStudentSearchVo;
import com.base.vo.PageList;

public interface JxStudentService {
	/**
	 * 新增或更新集贤学生报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param jxStudent
	 * @return
	 */
	Integer saveOrUpdate(JxStudent jxStudent);
	
	/**
	 * 根据id删除集贤学生报名信息(该删除为假删除)
	 * @author lfq
	 * @time 2015-3-16
	 * @param siteId
	 * @param id
	 * @return
	 */
	Integer delete(Integer siteId,Integer ...id);
	
	/**
	 * 根据id获取集贤学生报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param id
	 * @return
	 */
	JxStudent getJxStudent(Integer id);
	
	/**
	 * 根据关键字分页获取集贤学生报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param jxStudentSearchVo		
	 * @return
	 */
	PageList<JxStudent> getJxStudentPageList(JxStudentSearchVo jxStudentSearchVo);
	
	/**
	 * 根据关键子和创建年份查询集贤学生报名信息列表
	 * @author lfq
	 * @time 2015-3-17
	 * @param jxStudentSearchVo
	 * @return
	 */
	List<Map<String, Object>> getJxStudentList(JxStudentSearchVo jxStudentSearchVo);
}
