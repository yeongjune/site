package com.apply.action;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.model.Student;
import com.apply.service.CertificateService;
import com.apply.service.StudentService;
import com.apply.vo.StudentSearchVo;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.util.WordUtil;
import com.base.vo.PageList;
import com.site.model.Data;

@Controller
@RequestMapping( value="student" )
public class StudentAction {
	@Autowired
	private StudentService studentService;
	@Autowired
	private CertificateService certificateService;
	
	/**
	 * 进入学生审核管理界面
	 * @author lifq
	 * @param modelMap
	 * @return
	 */
	@RequestMapping( value="index")
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("examYear", dateFormat.format(new Date()));
		return "apply/student/index";
	}
	/**
	 * 进入学生详情界面
	 * @author lifq
	 * @param modelMap
	 * @return
	 */
	@RequestMapping( value="view")
	public String view(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id){
		modelMap.put("id", id);
		Integer siteId = User.getCurrentSiteId(request);
		try {
			if (siteId>0 && id!=null) {
					Map<String, Object> studentMap=this.studentService.load(id,siteId);
					modelMap.put("student", studentMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/student/view";
	}
	
	/**
	 * 进入填写审核备注界面
	 * @param status  审核状态
	 * @param admit	  录取状态
	 * @param ids	  学生Id，多个用逗号隔开
	 * @param names 学生姓名，多个由逗号隔开
	 * @return
	 */
	@RequestMapping( value="toCheckRemark")
	public String toCheckRemark(ModelMap modelMap,Integer status,Integer admit,String ids,String names){
		modelMap.put("admit", admit);
		modelMap.put("status", status);
		modelMap.put("ids", ids);
		modelMap.put("names", names);
		return "apply/student/checkRemark";
	}
	
	/**
	 * 进入设置学生面试日期时间界面
	 * @param ids	  学生Id，多个用逗号隔开
	 * @param names 学生姓名，多个由逗号隔开
	 * @return
	 */
	@RequestMapping( value="toSetInterViewDate")
	public String toSetInterViewDate(HttpServletRequest request,ModelMap modelMap,Integer status,String ids,String names){
		modelMap.put("ids", ids);
		modelMap.put("names", names);
		if (!ids.contains(",")) {
			Integer siteId = User.getCurrentSiteId(request);
			modelMap.put("student",this.studentService.load(Integer.parseInt(ids), siteId));
		}
		return "apply/student/setInterViewDate";
	}

	/**
	 * 修改学生的审核状态
	 * @author lifq
	 * @param ids 要修改的学生ID，多个逗号隔开
	 * @param status 修改为的状态
	 * @param checkRemark 审核备注
	 */
	@RequestMapping( value="doChangeStatus" )
	public void  doChangeStatus(HttpServletRequest request,HttpServletResponse response,String ids,Integer status,String checkRemark){
		Integer siteId = User.getCurrentSiteId(request);
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "操作失败");
		try {
			if (siteId>0) {
				Integer [] allStatus={0,1,2,3};
				//报名状态：0待审核;1审核通过；2审核不通过；3审核回退
				if (!StringUtil.isEmpty(ids)&&status!=null) {
					if(Arrays.<Integer>asList(allStatus).contains(status)){
						int count= this.studentService.updateStudentStatus(status,checkRemark, ids);
						result.put("code",count>0? Init.SUCCEED :Init.FAIL);
						result.put("msg", "操作成功");
					}
				}
			}else{
				result.put("code",Init.FALSE);
				result.put("msg", "未进入站点管理");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	/**
	 * 初始化密码为888888
	 * @author lifq
	 * @param ids 要修改的学生ID，多个逗号隔开
	 */
	@RequestMapping( value="updatePassword" )
	public void  updatePassword(HttpServletRequest request,HttpServletResponse response,String ids){
		Integer siteId = User.getCurrentSiteId(request);
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "操作失败");
		try {
			if (siteId>0) {
				//报名状态：0待审核;1审核通过；2审核不通过；3审核回退
				if (!StringUtil.isEmpty(ids)) {
					String password=CryptUtil.MD5encrypt("123456");
					int count= this.studentService.updateStudentPassword(ids, password);
					result.put("code",count>0? Init.SUCCEED :Init.FAIL);
					result.put("msg", "123456");
				}
			}else{
				result.put("code",Init.FALSE);
				result.put("msg", "未进入站点管理");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 删除学生信息
	 * @author lifq
	 * @param ids
	 */
	@RequestMapping( value="delete" )
	public void delete(HttpServletRequest request,HttpServletResponse response,String ids){
		Integer siteId = User.getCurrentSiteId(request);
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "操作失败");
		try {
			if (siteId>0) {
				if (!StringUtil.isEmpty(ids)) {
					int count= this.studentService.delete(ids,siteId,request);
					result.put("code",count>0? Init.SUCCEED :Init.FAIL);
					result.put("msg", "操作成功");
				}
			}else{
				result.put("code",Init.FALSE);
				result.put("msg", "未进入站点管理");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 加载一个学生数据
	 * @author lifq
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping( value="load" )
	public void load(HttpServletRequest request,HttpServletResponse response,Integer id){
		Integer siteId = User.getCurrentSiteId(request);
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "加载失败");
		try {
			if (siteId>0) {
				if (id!=null) {
					Map<String, Object> studentMap=this.studentService.load(id,siteId);
					result.put("student",studentMap);
					result.put("code", Init.SUCCEED);
				}
			}else{
				result.put("code", Init.FALSE);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 查询学生成绩列表
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping( value="list" )
	public void list(HttpServletRequest request,HttpServletResponse response,Integer currentPage,Integer pageSize,StudentSearchVo searchVo){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			searchVo.setSiteId(siteId);
			PageList pageList= this.studentService.findStudentPageList(currentPage, pageSize, searchVo);
			JSONUtil.printToHTML(response, pageList);
		}
	}
	
	/**
	 * 更新学生面试日期时间
	 * @param ids	  学生Id，多个用逗号隔开
	 * @param names 学生姓名，多个由逗号隔开
	 * @param interviewDate 面试时间,格式yyyy-MM-dd HH:mm-HH:mm
	 * @param batch 		考试批次
	 * @return
	 */
	@RequestMapping( value="saveInterViewDate")
	public void saveInterViewDate(HttpServletRequest request,HttpServletResponse response,String ids,String interviewDate,String batch){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			if (StringUtil.isEmpty(batch)||StringUtil.isEmpty(ids)||StringUtil.isEmpty(interviewDate)) {
				JSONUtil.print(response, Init.FALSE);
			}else{
				int result= this.studentService.updateStudentInterviewDate(ids, interviewDate,batch);
				JSONUtil.print(response, result>0 ? Init.SUCCEED : Init.FAIL);
			}
		}else{
			JSONUtil.print(response, Init.FALSE);
		}
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Data.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 下载html页面为word文档
	 * @author lifq
	 * @param response
	 * @param request
	 * @param htmlUrl 要下载的页面的地址
	 * @throws IOException
	 */
	@RequestMapping(value="downloadHtml")
	public void downloadHtml(HttpServletResponse response,HttpServletRequest request,String ids) throws IOException{
		try {
			 response.setContentType("application/msword;charset=utf-8");
			 response.setHeader("Cache-Control", "no-cache");
			 response.addHeader("Content-Disposition", "attachment;filename="+(new Date()).getTime()+".doc");//这里指定默认下载的名字，下载的时候用户可以修改
			 String domain = request.getRequestURL().toString().replace(request.getRequestURI(), "");
			 String url=domain+"/student/toPrintPage.action?ids="+ids;
			 //WordUtil.downloadHtmlAsWord(response.getOutputStream(),url);
			 OutputStream outputStream=response.getOutputStream();
			 WordUtil.writeAsWord(outputStream,url);
			 outputStream.flush();
			 outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入打印学生界面
	 * @author lifq
	 * @param searchVo StudentSearchVo 参数对象，如果根据学生ID打印一个或多个学生时传入ids=学生id即可，多个用逗号隔开
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping( value="toPrintPage")
	public String toPrintPage(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,StudentSearchVo searchVo) throws Exception{
		Integer siteId = User.getCurrentSiteId(request);//后台登录的的siteId
		Map<String, Object> applyUser=(Map<String, Object>) request.getSession().getAttribute(Init.APPLY_USER);//报名系统前台登录的帐号
		if (siteId!=null && siteId>0) {
			modelMap.put("studentList", this.studentService.findStudentList(searchVo));
			return "apply/student/printPage";
		}else if(applyUser!=null&&searchVo!=null){
			String id=""+applyUser.get("id");
			if ((!StringUtil.isEmpty(id))&&id.equals(searchVo.getIds())) {
				modelMap.put("studentList", this.studentService.findStudentList(searchVo));
				return "apply/student/printPage";
			}else{
				throw new Exception("无权限访问");
			}
		}else{
			throw new Exception("无权限访问");
		}
		
	}
	
	/**
	 * 进入编辑学生信息界面
	 * @author lfq
	 * @param request
	 * @param modelMap
	 * @param id 学生id
	 * @return
	 */
	@RequestMapping("toEdit")
	public String toEdit(HttpServletRequest request,ModelMap modelMap,Integer id){
		if (id!=null&&id!=0) {
			Student student=this.studentService.get(id);
			Integer siteId=User.getCurrentSiteId(request);
			if (student!=null&&siteId.intValue()==student.getSiteId().intValue()) {
				modelMap.put("student", student);
				List<Map<String, Object>> certificateList=certificateService.findCertificateList(null, siteId);
				modelMap.put("certificateList", certificateList);
			}		
		}
		return "apply/student/edit";
	}
	
	/**
	 * 修改学生信息
	 * @author lfq
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param student
	 */
	@RequestMapping("update")
	public void update(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Student student){
		String result=Init.FAIL;
		try {
			if (student!=null && student.getId()!=null &&student.getId()>0) {
				Student student2=this.studentService.get(student.getId());
				if (student2!=null) {
					String birthday=request.getParameter("birthday1");
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
					student2.setBirthday(dateFormat.parse(birthday));
					student2.setName(student.getName());
					student2.setAccount(student.getAccount());
					student2.setCertificateType(student.getCertificateType());
					student2.setCertificate(student.getCertificate());
					student2.setEnrollmentNumbers(student.getEnrollmentNumbers());
					student2.setGender(student.getGender());
					student2.setNationality(student.getNationality());
					student2.setNativePlace(student.getNativePlace());
					student2.setDomiciProvince(student.getDomiciProvince());
					student2.setDomicilCity(student.getDomicilCity());
					student2.setDomicileArea(student.getDomicileArea());
					student2.setDomicile(student.getDomicile());
					student2.setHomeAddress(student.getHomeAddress());
					student2.setGraduateProvince(student.getGraduateProvince());
					student2.setGraduateCity(student.getGraduateCity());
					student2.setGraduateArea(student.getGraduateArea());
					student2.setGraduate(student.getGraduate());
					student2.setPhoneNumber(student.getPhoneNumber());
					student2.setRewardHobby(student.getRewardHobby());
					student2.setFullName1(student.getFullName1());
					student2.setUnit1(student.getUnit1());
					student2.setPosition1(student.getPosition1());
					student2.setTelephone1(student.getTelephone1());
					student2.setFullName2(student.getFullName2());
					student2.setUnit2(student.getUnit2());
					student2.setPosition2(student.getPosition2());
					student2.setTelephone2(student.getTelephone2());
					student2.setHeadPicUrl(student.getHeadPicUrl());
					int com=(Integer) this.studentService.update(student2);
					result=com>0?Init.SUCCEED:Init.FAIL;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONUtil.print(response, result);
	}
	
	/**
	 * 检索帐号是否已存在
	 * @author lfq
	 * @param account
	 * @param id
	 */
	@RequestMapping("checkAccount")
	public void checkAccount(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String account,Integer id){
		String result=Init.TRUE;
		if (!StringUtil.isEmpty(account)&& id!=null && id>0) {
			Map<String, Object> student= this.studentService.loadByAccount(account);
			if (student==null) {
				result=Init.FALSE;
			}else if ((student.get("id")+"").equals(id+"")) {
				result=Init.FALSE;
			}
		}
		
		JSONUtil.print(response, result);
	}
}
