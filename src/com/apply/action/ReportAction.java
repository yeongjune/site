package com.apply.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.service.StudentService;
import com.authority.model.User;
import com.base.util.JSONUtil;

@Controller
@RequestMapping(value="report")
public class ReportAction {
	@Autowired
	private StudentService studentService;
	
	/**
	 * 进入学生信息报表界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping(value="toStudentReport")
	public String toStudentReport(ModelMap modelMap,HttpServletRequest request){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("examYear", dateFormat.format(new Date()));
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			modelMap.put("graduateList", studentService.findGraduatesBySite(siteId));
		}
		
		return "apply/report/studentReport";
	}
	/**
	 * 进入报名情况报表界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping(value="toApplyReport")
	public String toApplyReport(ModelMap modelMap,HttpServletRequest request){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("examYear", dateFormat.format(new Date()));
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			modelMap.put("graduateList", studentService.findGraduatesBySite(siteId));
		}
		return "apply/report/applyReport";
	}
	/**
	 * 进入成绩报表界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping(value="toScodeReport")
	public String toScodeReport(ModelMap modelMap,HttpServletRequest request){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("examYear", dateFormat.format(new Date()));
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			modelMap.put("graduateList", studentService.findGraduatesBySite(siteId));
		}
		return "apply/report/scodeReport";
	}
	
	/**
	 * 获取分数统计列表
	 * @author lifq
	 * @param response
	 * @param graduate 学校名，支持模糊统计
	 * @param 年份
	 */
	@RequestMapping(value="getScopeReportList")
	public void getScopeReportList(HttpServletRequest request,HttpServletResponse response,String graduate,String examYear){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			JSONUtil.printToHTML(response, this.studentService.findScopeReportList(graduate,examYear,siteId));
		}
	}
	/**
	 * 获取报名情况统计列表
	 * @author lifq
	 * @param response
	 * @param graduate 学校名，支持模糊统计
	 * @param 年份
	 */
	@RequestMapping(value="getApplyReportList")
	public void getApplyReportList(HttpServletRequest request,HttpServletResponse response,String graduate,String examYear){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			JSONUtil.printToHTML(response, this.studentService.findApplyReport(graduate, examYear, siteId));
		}
	}
}
