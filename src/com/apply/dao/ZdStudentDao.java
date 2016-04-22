package com.apply.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.apply.model.ApplyInfo;
import com.apply.model.DzStudent;
import com.apply.model.ZdStudent;
import com.apply.vo.StudentSearchVo;
import com.base.vo.PageList;

public interface ZdStudentDao {
	/**
	 * 保存新增的学生
	 * @param student
	 * @return
	 */
	Serializable save(ZdStudent zdStudent);
	
	/**
	 * 修改学生
	 * @author lifq
	 * @param zdStudent
	 * @return
	 */
	int update(ZdStudent zdStudent);
	
	/**
	 * 修改学生状态
	 * @author lifq
	 * @param status 新状态
	 * @param checkRemark 审核备注
	 * @param ids   学生ID，多个逗号隔开
	 * @return
	 */
	int updateStudentStatus(Integer status,String checkRemark,String ids);

	/**
	 *根据ID 批量删除学生
	 * @param ids 	学生ID,多个时用逗号隔开
	 * @param siteId
	 * @return		
	 */
	int delete(String ids,Integer siteId);

	/**
	 * 修改一个学生信息
	 * @param student  
	 * @return
	 */
	int update(Map<String, Object> student);
	
	
	/**
	 * 根据ID只返回学生数据，
	 * @author lifq
	 * @param id
	 * @return Student对象
	 */
	ZdStudent get(Integer id);
	
	
	/**
	 * 根据ID只返回学生数据，
	 * @author lifq
	 * @param id
	 * @param siteId
	 * @return map
	 */
	Map<String, Object> load(Integer id,Integer siteId);
	
	/**
	 * 根据account只返回学生数据，
	 * @author lifq
	 * @param account
	 * @return map
	 */
	Map<String, Object> loadByAccount(String account);
	
	/**
	 * 根据account只返回学生数据，
	 * @author lifq
	 * @param name
	 * @return
	 */
	String finMaxAccountByName(String name);

	/**
	 * 查询学生
	 * @author lifq
	 * @param searchVo
	 * @return
	 */
	List<Map<String, Object>>	findStudentList(StudentSearchVo searchVo);
	
	/**
	 * 分页查询学生
	 * @author lifqiang
	 * @param currentPage 页数
	 * @param pageSize	   每页条数
	 * @param searchVo	参数Vo
	 * @return
	 */
	PageList<ZdStudent> findStudentPageList(Integer currentPage,Integer pageSize,StudentSearchVo searchVo);

	
	/**
	 * 返回所有的学生头像
	 * @author lifq
	 * @return
	 */
	List<String> findAllHeadPicUrl();
	
	/**
	 * 修改用户密码
	 * @author lifq
	 * @param ids 用户ID，多个用逗号隔开
	 * @param password 加密后的密码
	 * @return
	 */
	int updateStudentPassword(String ids,String password);
	
	/**
	 * 根据用户id返回用户的头像列表
	 * @author lifq
	 * @param ids 用户ID,多个逗号隔开
	 * @return
	 */
	List<String> findHeadPicUrlByIds(String ids);
	
	/**
	 * 修改用户录取状态
	 * @author lifq
	 * @param ids 用户ID，多个用逗号隔开
	 * @param admit 录取状态
	 * @return
	 */
	int updateStudentAdmit(String ids,Integer admit);
	
	/**
	 * 更新学的学生的面试日期
	 * @author lifq
	 * @param ids 用户ID，多个用逗号隔开
	 * @param interviewDate 设置为的面试日期时间：格式yyyy-MM-dd HH:mm-HH:mm
	 * @param batch 面试批次
	 * @return
	 */
	int updateStudentInterviewDate(String ids,String interviewDate,String batch);
	
	/**
	 * 统计分数
	 * @author lifq
	 * @param graduate 学校名，模糊查询
	 * @param examYear 年份
	 * @param siteId
	 * @return 统计列表{graduate：学校名,applyCount:报名人数,interviewCount：面试人数,admitCount：录取人数,notAdmitCount：未录取人数, watingAdmit待公布录取人数
	 * avgOfChinese：平均语文成绩,avgOfMaths:平均数学成绩,avgOfEnglish：平均英语成绩,AvgOfInterview：平均面试成绩}
	 */
	List<Map<String, Object>> findScopeReportList(String graduate,String examYear,Integer siteId);
	
	/**
	 * 统计报名情况
	 * @author lifq
	 * @param graduate 学校名，模糊查询
	 * @param examYear 年份
	 * @param siteId
	 * @return 统计列表{graduate：学校名,applyCount:报名人数,interviewCount：面试人数,admitCount：录取人数,notAdmitCount：未录取人数, watingAdmit待公布录取人数
	 * watingCheck：待审核人数,passCheck:审核通过人数,notPassCheck：审核不通过人数,backCheck：审核回退人数}
	 */
	List<Map<String, Object>> findApplyReport(String graduate,String examYear,Integer siteId);
	
	
	/**
	 * 获取学生准考证号，如果没有会生成一个准考证号给学生
	 * 注意：一个学生只有一个准考证号
	 * @author lfq
	 * @param id 学生ID
	 * @param examDate 考试日期，不包括时间
	 * @return 准考证号=考试日期+4位数字从1001算起的数字，格式：201406081001
	 */
	String  getStudentTestCard(Integer id,Date examDate);

	/**
	 * 查询在当前报名时间报名的学生并且尚未参加考试未录取的考生
	 * @author lifq
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> findCurrentStudentList(Integer siteId, Integer examType);
	
	/**
	 * 获取站点的毕业院校
	 * @param siteId
	 * @return
	 */
	List<String> findGraduatesBySite(Integer siteId);

	/**
	 * 新增或更新民航子弟报名信息
	 * @param zdStudent
	 * @return
	 */
	Integer saveOrUpdate(ZdStudent zdStudent);
	
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
	PageList<ZdStudent> getZdStudentPageList(Integer siteId,Integer pageSize,Integer currentPage,String keyword,String year);

	/**
	 * 根据关键子和创建年份查询民航子弟报名信息列表
	 * @author lfq
	 * @time 2015-3-17
	 * @param siteId
	 * @param year		报名年份：格式yyyy
	 * @param keyword	关键字
	 * @param id		学生id
	 * @return
	 */
	List<Map<String, Object>> getZdStudentList(Integer siteId, String year,
			String keyword, Integer... id);

	/**
	 * 根据身份证号查询报名的学生
	 * @param iDCard
	 * @return
	 */
	ZdStudent getByIDCard(String iDCard);

	List<Map<String, Object>> getZdStudent2List(Integer siteId, String year,
			String keyword, Integer... id);

	/**
	 * 设置学生的面试考室
	 * @param ids
	 * @param roomNo
	 * @return
	 */
	int updateStudentRoomNo(String ids, String roomNo);

	/**
	 * 重置面试时间
	 * @param ids
	 * @param batch
	 * @return
	 */
	int resetStudentInterviewDate(String ids, String batch);
	
}
