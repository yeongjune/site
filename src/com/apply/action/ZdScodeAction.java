package com.apply.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.service.ExamService;
import com.apply.service.ZdStudentService;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;

/**
 * 成绩管理
 * @author lfq
 *
 */
@Controller
@RequestMapping("zd_scode")
public class ZdScodeAction {
	@Autowired
	private ZdStudentService zd_studentService;
	@Autowired
	private ExamService examService;
	
	
	/**
	 * 进入面试成绩管理界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping( value="index")
	public String index(ModelMap modelMap){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("examYear", dateFormat.format(new Date()));
		return "apply/scode/index";
	}
	/**
	 * 进入面试成绩编辑界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping( value="toEdit")
	public String toEdit(ModelMap modelMap,Integer id){
		modelMap.put("id", id);
		return "apply/scode/edit";
	}
	
	/**
	 * 进入面考试选择界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping( value="selectExam")
	public String selectExam(HttpServletRequest request,ModelMap modelMap){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			modelMap.put("examList", examService.findExamList(siteId, null));
		}
		return "apply/scode/selectExam";
	}
	
	/**
	 * 查询学生成绩列表
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping( value="save" )
	public void save(HttpServletRequest request,HttpServletResponse response,
			String id,Double interviewScore,Integer admit,Integer  interview){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "操作失败");
		try {
			Map<String, Object> studentMap=new HashMap<String, Object>();
			studentMap.put("id", id);
			studentMap.put("interviewScore", interviewScore);
			studentMap.put("admit", admit);
			studentMap.put("interview", interview);
			int com= this.zd_studentService.update(studentMap);
			if (com>0) {
				result.put("code", Init.SUCCEED);
				result.put("msg", "操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	

	/**
	 * 保存学录取和不录取的状态
	 * @param request
	 * @param response
	 * @param ids
	 * @param admit 录取状态-1待审核、0不录取、1录取 
	 * @param checkRemark  录取备注
	 */
	@RequestMapping( value="saveAdmit" )
	public void saveAdmit(HttpServletRequest request,HttpServletResponse response,
			String ids,Integer admit,String checkRemark){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "操作失败");
		try {
			int com= this.zd_studentService.updateStudentAdmit(ids,checkRemark, admit);
			if (com>0) {
				result.put("code", Init.SUCCEED);
				result.put("msg", "操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	
}
