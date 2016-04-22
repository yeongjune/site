package com.apply.dao;

import java.util.List;
import java.util.Map;

import com.apply.model.DzStudent;
import com.base.vo.PageList;

/**
 * @author lfq
 * @time 2015-3-16
 *
 */
public interface DzStudentDao {
	
	/**
	 * 新增或更新民航子弟初中报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param dzStudent
	 * @return
	 */
	Integer saveOrUpdate(DzStudent dzStudent);
	
	/**
	 * 根据id删除民航子弟初中报名信息(该删除为假删除)
	 * @author lfq
	 * @time 2015-3-16
	 * @param siteId
	 * @param id
	 * @return
	 */
	Integer delete(Integer siteId,Integer ...id);
	
	/**
	 * 根据id获取民航子弟初中报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param id
	 * @return
	 */
	DzStudent getDzStudent(Integer id);
	
	/**
	 * 根据关键字分页获取民航子弟初中报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param siteId
	 * @param pageSize
	 * @param currentPage
	 * @param keyword	关键字
	 * @param year		创建年份，格式yyyy,可以传null
	 * @return
	 */
	PageList<DzStudent> getDzStudentPageList(Integer siteId,Integer pageSize,Integer currentPage,String keyword,String year);
	
	/**
	 * 根据关键子和创建年份查询民航子弟初中报名信息列表
	 * @author lfq
	 * @time 2015-3-17
	 * @param siteId
	 * @param year		报名年份：格式yyyy
	 * @param keyword	关键字
	 * @param id		学生id
	 * @return
	 */
	List<Map<String, Object>> getDzStudentList(Integer siteId,String year,String keyword,Integer ...id);
}
