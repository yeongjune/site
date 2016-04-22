package com.apply.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.model.Exam;
import com.apply.service.MhExamService;
import com.apply.service.SeatService;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;

/**
 * 考试(考场)管理 控制器
 * @author lfq
 * @2014-5-10
 */
@Controller
@RequestMapping(value="mh_exam")
public class MhExamAction {
	@Autowired
	private MhExamService mhExamService;

	@Autowired
	private SeatService seatService;
	
	/**
	 * 进入考场分配管理首页(初中)
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		return "apply/exam/mhIndex";
	}

	/**
	 * 进入考场分配管理首页(小学)
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="index2")
	public String index2(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		return "apply/exam/mhIndex2";
	}
	
	/**
	 * 进入编辑(新增、修改)考试信息界面
	 * @author lfq
	 * @param id 考试ID，修改时需要传入
	 * @return
	 */
	@RequestMapping(value="toEdit")
	public String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id, Integer examType){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			if (id!=null&&id>0) {
				Exam exam=this.mhExamService.get(id, examType);
				modelMap.put("exam", exam);	
				modelMap.put("seatCount", this.seatService.getCountByExamId(id));
			}
		}
		
		Map<String, Object> site = User.getCurrentUser(request);
		String siteName = (String) site.get("siteName");
		if(examType > 0){
			modelMap.put("examName", siteName + "初中入学考试");
		}else{
			modelMap.put("examName", siteName + "小学入学考试");
		}
		modelMap.put("examType", examType);	
		return "apply/exam/mhEdit";
	}
	/**
	 * 保存(新增、修改)考试信息
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="save")
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String examDate1,Exam exam){
		Integer siteId = User.getCurrentSiteId(request);
		int result=0;
		try {
			if (siteId>0) {
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
				exam.setExamDate(dateFormat.parse(examDate1));
				if (exam.getId()!=null && exam.getId()>0) {//修改
						Map<String, Object> tempExam=new HashMap<String, Object>();
						tempExam.put("id", exam.getId());
						tempExam.put("examName", exam.getExamName());
						tempExam.put("examArea", exam.getExamArea());
						tempExam.put("examDate", exam.getExamDate());
						tempExam.put("examTime", exam.getExamTime());
						tempExam.put("remark", exam.getRemark());
						tempExam.put("roomCount", exam.getRoomCount());
						result= this.mhExamService.update(tempExam);
				}else{//新增
						exam.setSiteId(siteId);
						result= (Integer) this.mhExamService.save(exam);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.print(response,result>0 ? Init.SUCCEED : Init.FAIL);
	}
	
	/**
	 * 删除考试信息
	 * @author lfq
	 * @param id 考试ID
	 * @return
	 */
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,Integer id){
		int result=0;
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId != null && siteId>0 && id != null && id>0) {
				  result=this.mhExamService.delete(siteId, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 JSONUtil.print(response, result>0?Init.SUCCEED : Init.FAIL);
	}
	
	/**
	 * 查询考试列表(分页)
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="list")
	public void list(HttpServletRequest request,HttpServletResponse response,Integer currentPage,Integer pageSize,String examName, Integer examType){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId != null && siteId>0) {
				  PageList pageList=this.mhExamService.findExamPageList(currentPage, pageSize, siteId, examName, examType);
				  JSONUtil.printToHTML(response, pageList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询参加考试的学生列表(分页)
	 * @author lfq
	 * @param currentPage
	 * @param pageSize
	 * @param id 	考试ID
	 * @param studentName 学生名，模糊查询
	 */
	@RequestMapping(value="listSeat")
	public void listSeat(HttpServletRequest request,HttpServletResponse response,Integer currentPage,Integer pageSize,Integer id,String studentName, Integer examType){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId != null && siteId>0) {
				  PageList seatList=this.seatService.findSeatPageList(currentPage, pageSize, siteId, id, null, null, studentName, null, null, examType);
				  JSONUtil.printToHTML(response, seatList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 取消学生的某场考试
	 * @author lfq
	 * @param id  考试ID
	 * @param studentIds 选择参考的学生ID，多个用逗号隔开
	 */
	@RequestMapping(value="deleteSeat")
	public void deleteSeat(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id,String studentIds){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			int result=this.seatService.deleteByExamAndStudent(id, studentIds);
			JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
		}
	}
	
	/**
	 * 生成座位
	 * @author lfq
	 * @param id 考试ID
	 * @param clearAllSeatNO 是否全部重新生成座位号1是，其他时则只生成还 没有座位的座位号
	 */
	@RequestMapping(value="generateSeatNo")
	public void generateSeatNo(HttpServletRequest request,HttpServletResponse response,Integer id,String  clearAllSeatNO, Integer examType){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId!=null&&siteId>0) {
				Boolean com=(StringUtil.isEmpty(clearAllSeatNO)||!clearAllSeatNO.equals("1"))?false:true;
				int result=this.mhExamService.updateSeatNoAndRoomNo(id, com, examType);
				JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FALSE);
		}
		
	}
	/**
	 * 查看/打印座位表
	 * @author lfq
	 * @param id 考试ID
	 */
	@RequestMapping(value="toShowSeatTab")
	public String toShowSeatTab(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id, Integer examType){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId!=null&&siteId>0&&id!=null&&id>0) {
				Exam exam=this.mhExamService.get(id, examType);
				if (exam!=null) {
					modelMap.put("exam", exam);
					//本次考试已添加了的参考学生
					List<Map<String, Object>> seatList=this.seatService.findSeatList(siteId, id, null, null, null, null, null, null, null);
					modelMap.put("seatList", seatList);
				}
			}
			
		} catch (Exception e) {
			
		}
		return "apply/exam/mhSeatTable";
	}
	/**
	 * 查看/打印桌贴
	 * @author lfq
	 * @param id 考试ID
	 */
	@RequestMapping(value="toShowDescTab")
	public String toShowDescTab(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id, Integer examType){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId!=null&&siteId>0&&id!=null&&id>0) {
				Exam exam=this.mhExamService.get(id, examType);
				if (exam!=null) {
					modelMap.put("exam", exam);
					//本次考试已添加了的参考学生
					List<Map<String, Object>> seatList=this.seatService.findSeatList(siteId, id, null, null, null, null, null, null, null);
					modelMap.put("seatList", seatList);
				}
			}
		} catch (Exception e) {
			
		}
		return "apply/exam/mhDesckTable";
	}
	
	/**
	 *分配考场
	 * @author lifq
	 * @param id 考试ID
	 * @param 
	 */
	@RequestMapping(value="deployRoom")
	public void deployRoom(HttpServletRequest request,HttpServletResponse response,Integer id, Integer examType){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId!=null&&siteId>0) {
				this.mhExamService.updateDeployRoom(id, examType);
				JSONUtil.print(response, Init.SUCCEED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONUtil.print(response, Init.FALSE);
		}
	}
	
	/**
	 * 查看考场分配情况
	 * @author lfq
	 * @param id 考试ID
	 */
	@RequestMapping(value="showDeployRoom")
	public String showDeployRoom(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id, Integer examType){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId!=null&&siteId>0&&id!=null&&id>0) {
				Exam exam=this.mhExamService.get(id, examType);
				if (exam!=null) {
					modelMap.put("exam", exam);
					//本次考试已添加了的参考学生
					List<Map<String, Object>> seatList=this.seatService.findSeatList(siteId, id, null, null, null, null, null, null, null, examType);
					Map<String, List<Map<String, Object>>> seatMapList=new HashMap<String, List<Map<String,Object>>>();
					for (int i = 0; i < seatList.size(); i++) {
						if (seatMapList.containsKey(seatList.get(i).get("roomNo"))) {
							seatMapList.get(seatList.get(i).get("roomNo")).add(seatList.get(i));
						}else{
							List<Map<String, Object>> tempSeatList=new ArrayList<Map<String,Object>>();
							tempSeatList.add(seatList.get(i));
							seatMapList.put(seatList.get(i).get("roomNo")+"", tempSeatList);
						}
					}
					
					modelMap.put("seatMapList", seatMapList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/exam/mhShowDeployRoom";
	}
	/**
	 * 查看考场分配的考生
	 * @author lfq
	 * @param id 考试ID
	 */
	@RequestMapping(value="toShowExamStudent")
	public String toShowExamStudent(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id, Integer examType){
		modelMap.put("exam", this.mhExamService.get(id, examType));
		return "apply/exam/mhShowExamStudent";
	}
	
	/**
	 * 进入打印考场的页面
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param examId
	 * @param examType
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value="toPrint")
	public String toPrint(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer examId, Integer examType) throws Exception{
//		Integer siteId = siteService.getSiteId(request);//后台登录的的siteId
//		modelMap.put("dzStudentList", zdStudentService.getZdStudentList(siteId, year, keyword, StringUtil.splitToArray(ids, ",")));
		
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId!=null&&siteId>0&&examId!=null&&examId>0) {
				Exam exam=this.mhExamService.get(examId, examType);
				if (exam!=null) {
					modelMap.put("exam", exam);
					//本次考试已添加了的参考学生
					List<Map<String, Object>> seatList=this.seatService.findSeatList(siteId, examId, null, null, null, null, null, null, null, examType);
					Map<String, List<Map<String, Object>>> seatMapList=new HashMap<String, List<Map<String,Object>>>();
					for (int i = 0; i < seatList.size(); i++) {
						if (seatMapList.containsKey(seatList.get(i).get("roomNo"))) {
							seatMapList.get(seatList.get(i).get("roomNo")).add(seatList.get(i));
						}else{
							List<Map<String, Object>> tempSeatList=new ArrayList<Map<String,Object>>();
							tempSeatList.add(seatList.get(i));
							seatMapList.put(seatList.get(i).get("roomNo")+"", tempSeatList);
						}
					}
					
					modelMap.put("seatMapList", seatMapList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/exam/mhShowDeployRoom";
	}
}
