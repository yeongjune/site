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

import com.apply.model.ApplyInfo;
import com.apply.model.ZdStudent;
import com.apply.model.ZdStudent2;
import com.apply.service.ApplyInfoService;
import com.apply.service.CertificateService;
import com.apply.service.ZdStudent2Service;
import com.apply.service.ZdStudentService;
import com.apply.vo.StudentSearchVo;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.util.WordUtil;
import com.base.vo.PageList;
import com.site.model.Data;
import com.site.model.Label;
import com.site.service.DataService;
import com.site.service.SiteService;

/**
 * 民航初中报名系统
 * @author ah
 *
 */
@Controller
@RequestMapping( value="zd_student" )
public class ZdStudentAction {
	@Autowired
	private ZdStudentService zdStudentService;
	@Autowired
	private ZdStudent2Service zdStudent2Service;
	@Autowired
	private CertificateService certificateService;
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private ApplyInfoService applyInfoService;
	
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
		return "apply/zdstudent/index";
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
					Map<String, Object> studentMap=this.zdStudentService.load(id,siteId);
					modelMap.put("student", studentMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/zdstudent/view";
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
		modelMap.put("examType", 1);
		return "apply/zdstudent/checkRemark";
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
			modelMap.put("student",this.zdStudentService.load(Integer.parseInt(ids), siteId));
		}
		return "apply/zdstudent/setInterViewDate";
	}

	/**
	 * 进入设置学生面试日期时间界面
	 * @param ids	  学生Id，多个用逗号隔开
	 * @param names 学生姓名，多个由逗号隔开
	 * @return
	 */
	@RequestMapping( value="toSetRoomNo")
	public String toSetRoomNo(HttpServletRequest request,ModelMap modelMap,Integer status,String ids,String names){
		modelMap.put("ids", ids);
		modelMap.put("names", names);
		if (!ids.contains(",")) {
			Integer siteId = User.getCurrentSiteId(request);
			modelMap.put("student",this.zdStudentService.load(Integer.parseInt(ids), siteId));
		}
		return "apply/zdstudent/setRoomNo";
	}
	
	/**
	 * 更新学生面试考室
	 * @param ids	  学生Id，多个用逗号隔开
	 * @param names 学生姓名，多个由逗号隔开
	 * @param roomNo 面试考室
	 * @return
	 */
	@RequestMapping( value="saveRoomNo")
	public void saveRoomNo(HttpServletRequest request,HttpServletResponse response,String ids,String roomNo){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			if (StringUtil.isEmpty(ids)||StringUtil.isEmpty(roomNo)) {
				JSONUtil.print(response, Init.FALSE);
			}else{
				int result= this.zdStudentService.updateStudentRoomNo(ids, roomNo);
				JSONUtil.print(response, result>0 ? Init.SUCCEED : Init.FAIL);
			}
		}else{
			JSONUtil.print(response, Init.FALSE);
		}
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
						int count= this.zdStudentService.updateStudentStatus(status,checkRemark, ids);
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
	 * 初始化密码为123456
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
					int count= this.zdStudentService.updateStudentPassword(ids, password);
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
					int count= this.zdStudentService.delete(ids,siteId,request);
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
					Map<String, Object> studentMap=this.zdStudentService.load(id,siteId);
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
	 * 分页查询民航子弟初中学生报名信息
	 * @author lfq
	 * @param keyword	关键字模糊查询(学院名)
	 * @param pageSize  每页记录数
	 * @param currentPage 当前页
	 * @param year		创建年份
	 */
//	@RequestMapping("list")
//	public void list(HttpServletRequest request,HttpServletResponse response,
//			Integer pageSize,Integer currentPage,String keyword,String year){
//		currentPage = Init.getCurrentPage(currentPage);
//		pageSize = Init.getPageSize(pageSize);
//		Integer siteId = User.getCurrentSiteId(request);
//		PageList<ZdStudent> pageList = this.zdStudentService.getZdStudentPageList(siteId, pageSize, currentPage, keyword,year);
//		JSONUtil.printDatePatternHTML(response, pageList, "yyyy-MM-dd");
//	}
	
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
			PageList<ZdStudent> pageList= this.zdStudentService.findStudentPageList(currentPage, pageSize, searchVo);
			JSONUtil.printDatePatternHTML(response, pageList, "yyyy-MM-dd");
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
		batch = 1 + "";
		if (siteId>0) {
			if (StringUtil.isEmpty(batch)||StringUtil.isEmpty(ids)||StringUtil.isEmpty(interviewDate)) {
				JSONUtil.print(response, Init.FALSE);
			}else{
				int result= this.zdStudentService.updateStudentInterviewDate(ids, interviewDate,batch);
				JSONUtil.print(response, result>0 ? Init.SUCCEED : Init.FAIL);
			}
		}else{
			JSONUtil.print(response, Init.FALSE);
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
	@RequestMapping( value="resetInterViewDate")
	public void resetInterViewDate(HttpServletRequest request,HttpServletResponse response,String ids,String batch){
		Integer siteId = User.getCurrentSiteId(request);
		batch = 1 + "";
		if (siteId>0) {
			if (StringUtil.isEmpty(batch)||StringUtil.isEmpty(ids)) {
				JSONUtil.print(response, Init.FALSE);
			}else{
				int result= this.zdStudentService.resetStudentInterviewDate(ids, batch);
				JSONUtil.print(response, result>0 ? Init.SUCCEED : Init.FAIL);
			}
		}else{
			JSONUtil.print(response, Init.FALSE);
		}
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
			modelMap.put("studentList", this.zdStudentService.findStudentList(searchVo));
			return "apply/zdstudent/printPage";
		}else if(applyUser!=null&&searchVo!=null){
			String id=""+applyUser.get("id");
			if ((!StringUtil.isEmpty(id))&&id.equals(searchVo.getIds())) {
				modelMap.put("studentList", this.zdStudentService.findStudentList(searchVo));
				return "apply/zdstudent/printPage";
			}else{
				throw new Exception("无权限访问");
			}
		}else{
			throw new Exception("无权限访问");
		}
		
	}
	
	/**
	 * 进入编辑初中学生信息界面
	 * @author lfq
	 * @param request
	 * @param modelMap
	 * @param id 学生id
	 * @return
	 */
	@RequestMapping("toEdit")
	public String toEdit(HttpServletRequest request,ModelMap modelMap,Integer id){
		Integer siteId=User.getCurrentSiteId(request);
		if(siteId == null){
			siteId = siteService.getSiteId(request);
		}
		if (id!=null&&id!=0) {
			ZdStudent zdStudent=this.zdStudentService.get(id);
			if (zdStudent!=null&&siteId.intValue()==zdStudent.getSiteId().intValue()) {
				modelMap.put("student", zdStudent);
				List<Map<String, Object>> certificateList=certificateService.findCertificateList(null, siteId);
				modelMap.put("certificateList", certificateList);
			}		
			// 加载菜单
			Data data =dataService.getByNameAndSiteId(Label.Item.Menu, 0);
			dataService.processMenu(modelMap, data, siteId);
		}
		Map<String, Object> site = siteService.load(siteId);
		return "/template/"+site.get("directory")+"/edit";
	}
	
	/**
	 * 进入编辑初中学生信息界面
	 * @author lfq
	 * @param request
	 * @param modelMap
	 * @param id 学生id
	 * @return
	 */
	@RequestMapping("toEdit2")
	public String toEdit2(HttpServletRequest request,ModelMap modelMap,Integer id){
		Integer siteId=User.getCurrentSiteId(request);
		if(siteId == null){
			siteId = siteService.getSiteId(request);
		}
		if (id!=null&&id!=0) {
			ZdStudent2 zdStudent=this.zdStudent2Service.get(id);
			if (zdStudent!=null&&siteId.intValue()==zdStudent.getSiteId().intValue()) {
				modelMap.put("student", zdStudent);
				List<Map<String, Object>> certificateList=certificateService.findCertificateList(null, siteId);
				modelMap.put("certificateList", certificateList);
			}		
		}
		Map<String, Object> site = siteService.load(siteId);
		
		// 加载菜单
		Data data =dataService.getByNameAndSiteId(Label.Item.Menu, 0);
		dataService.processMenu(modelMap, data, siteId);
		
		return "/template/"+site.get("directory")+"/edit2";
	}
	
	/**
	 * 修改学生信息
	 * @author lfq
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param zdStudent
	 */
	@RequestMapping("update")
	public void update(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,ZdStudent zdStudent){
		String result=Init.FAIL;
		try {
			if (zdStudent!=null && zdStudent.getId()!=null &&zdStudent.getId()>0) {
				ZdStudent zdStudent2=this.zdStudentService.get(zdStudent.getId());
				if (zdStudent2!=null) {
					String birthday=request.getParameter("birthday1");
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
					zdStudent2.setBirthday(dateFormat.parse(birthday));
					zdStudent2.setName(zdStudent.getName());
//					zdStudent2.setAccount(zdStudent.getAccount());
//					zdStudent2.setCertificateType(zdStudent.getCertificateType());
//					zdStudent2.setCertificate(zdStudent.getCertificate());
//					zdStudent2.setEnrollmentNumbers(zdStudent.getEnrollmentNumbers());
					zdStudent2.setGender(zdStudent.getGender());
//					zdStudent2.setNationality(zdStudent.getNationality());
//					zdStudent2.setNativePlace(zdStudent.getNativePlace());
					zdStudent2.setDomiciProvince(zdStudent.getDomiciProvince());
					zdStudent2.setDomicilCity(zdStudent.getDomicilCity());
					zdStudent2.setDomicileArea(zdStudent.getDomicileArea());
//					zdStudent2.setDomicile(zdStudent.getDomicile());
					zdStudent2.setHomeAddress(zdStudent.getHomeAddress());
//					zdStudent2.setGraduateProvince(zdStudent.getGraduateProvince());
//					zdStudent2.setGraduateCity(zdStudent.getGraduateCity());
//					zdStudent2.setGraduateArea(zdStudent.getGraduateArea());
					zdStudent2.setGraduate(zdStudent.getGraduate());
//					zdStudent2.setPhoneNumber(zdStudent.getPhoneNumber());
					zdStudent2.setRewardHobby(zdStudent.getRewardHobby());
					zdStudent2.setFullName1(zdStudent.getFullName1());
					zdStudent2.setUnit1(zdStudent.getUnit1());
//					zdStudent2.setPosition1(zdStudent.getPosition1());
					zdStudent2.setTelephone1(zdStudent.getTelephone1());
					zdStudent2.setFullName2(zdStudent.getFullName2());
					zdStudent2.setUnit2(zdStudent.getUnit2());
//					zdStudent2.setPosition2(zdStudent.getPosition2());
					zdStudent2.setTelephone2(zdStudent.getTelephone2());
					zdStudent2.setHeadPicUrl(zdStudent.getHeadPicUrl());
					int com=(Integer) this.zdStudentService.update(zdStudent2);
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
			Map<String, Object> student= this.zdStudentService.loadByAccount(account);
			if (student==null) {
				result=Init.FALSE;
			}else if ((student.get("id")+"").equals(id+"")) {
				result=Init.FALSE;
			}
		}
		
		JSONUtil.print(response, result);
	}
	
	/**
	 * 保存民航子弟初中报名信息
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param zdStudent
	 */
	@RequestMapping( value="doRegister" )
	public void doRegister(HttpServletRequest request,HttpServletResponse response,ZdStudent zdStudent){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "提交失败，请重试");

		Integer siteId = siteService.getSiteId(request);
		try {
			if (siteId>0) {
				String validCode=request.getSession().getAttribute("validCode")+"";
				String inputValidCode=request.getParameter("validCode")+"";
				if (StringUtil.isEmpty(validCode)||StringUtil.isEmpty(inputValidCode)||!validCode.equals(inputValidCode)) {
					result.put("msg", "提交失败，验证输入有误");
				}else if(zdStudent.getId()!=null&&zdStudent.getId()>0){
					result.put("msg", "抱歉，您已提交过报名");
				}else if(StringUtil.isEmpty(zdStudent.getCompanyName())
						||StringUtil.isEmpty(zdStudent.getDomicilCity())
						||StringUtil.isEmpty(zdStudent.getDomiciProvince())
						||StringUtil.isEmpty(zdStudent.getGender())
						||StringUtil.isEmpty(zdStudent.getGraduate())
						||StringUtil.isEmpty(zdStudent.getHeadPicUrl())
						||StringUtil.isEmpty(zdStudent.getHomeAddress())
						||StringUtil.isEmpty(zdStudent.getIDCard())
						||StringUtil.isEmpty(zdStudent.getName())
						||StringUtil.isEmpty(zdStudent.getNativePlaceCity())
						||StringUtil.isEmpty(zdStudent.getNativePlaceProvince())
						||StringUtil.isEmpty(zdStudent.getFullName1())
						||StringUtil.isEmpty(zdStudent.getRelationship1())
						||StringUtil.isEmpty(zdStudent.getTelephone1())
						||StringUtil.isEmpty(zdStudent.getUnit1())
						||StringUtil.isEmpty(zdStudent.getInSchoolNo())
						||StringUtil.isEmpty(zdStudent.getPassword())
						||StringUtil.isEmpty(zdStudent.getFatherPhone())
						||StringUtil.isEmpty(zdStudent.getMatherPhone())
						||zdStudent.getBirthday()==null){
					result.put("msg", "报名信息不完整，请填写完整报名信息后再提交报名");
				}else if(this.zdStudentService.getByIDCard(zdStudent.getIDCard()) != null){
					result.put("msg", "该身份证号已报名，不能重复报名");
				}else{	
					zdStudent.setAccount(zdStudent.getIDCard());
					zdStudent.setSiteId(siteId);
					zdStudent.setCreateTime(new Date());
					zdStudent.setPassword(CryptUtil.MD5encrypt(zdStudent.getPassword()));
					zdStudentService.saveOrUpdate(zdStudent);
					result.put("id", zdStudent.getId());
					result.put("code", Init.SUCCEED);
					result.put("msg", "恭喜您，成功提交报名");
					request.getSession().setAttribute(Init.APPLY_USER, this.zdStudentService.load(zdStudent.getId(),siteId));
				}
			}else{
				result.put("code", Init.FALSE);
				result.put("msg", "未进入任何站点,请刷新");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printDateTimeHTML(response, result);
	}
	/**
	 * 保存民航子弟初中报名修改信息
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param zdStudent
	 */
	@RequestMapping( value="doEdit" )
	public void doEdit(HttpServletRequest request,HttpServletResponse response,ZdStudent zdStudent){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "提交失败，请重试");
		
		Integer siteId = siteService.getSiteId(request);
		try {
			if (siteId>0) {
				String validCode=request.getSession().getAttribute("validCode")+"";
				String inputValidCode=request.getParameter("validCode")+"";
				if (StringUtil.isEmpty(validCode)||StringUtil.isEmpty(inputValidCode)||!validCode.equals(inputValidCode)) {
					result.put("msg", "提交失败，验证输入有误");
				}else if(zdStudent.getId()==null&&zdStudent.getId()<=0){
					result.put("msg", "抱歉，您还没有报名，不能修改信息");
				}else if(StringUtil.isEmpty(zdStudent.getCompanyName())
						||StringUtil.isEmpty(zdStudent.getDomicilCity())
						||StringUtil.isEmpty(zdStudent.getDomiciProvince())
						||StringUtil.isEmpty(zdStudent.getGender())
						||StringUtil.isEmpty(zdStudent.getGraduate())
						||StringUtil.isEmpty(zdStudent.getHeadPicUrl())
						||StringUtil.isEmpty(zdStudent.getHomeAddress())
						||StringUtil.isEmpty(zdStudent.getIDCard())
						||StringUtil.isEmpty(zdStudent.getName())
						||StringUtil.isEmpty(zdStudent.getNativePlaceCity())
						||StringUtil.isEmpty(zdStudent.getNativePlaceProvince())
						||StringUtil.isEmpty(zdStudent.getFullName1())
						||StringUtil.isEmpty(zdStudent.getRelationship1())
						||StringUtil.isEmpty(zdStudent.getTelephone1())
						||StringUtil.isEmpty(zdStudent.getUnit1())
						||StringUtil.isEmpty(zdStudent.getInSchoolNo())
						||zdStudent.getBirthday()==null){
					result.put("msg", "报名信息不完整，请填写完整报名信息后再提交报名");
				}else{	
					ZdStudent zdStudent2 = this.zdStudentService.get(zdStudent.getId());
					if(!zdStudent.getIDCard().equals(zdStudent2.getIDCard()) && this.zdStudentService.getByIDCard(zdStudent.getIDCard()) != null){
						result.put("msg", "该身份证号已报名，不能重复报名");
					}else{
						zdStudent.setCompanyName(zdStudent.getCompanyName().trim());
						zdStudent.setAccount(zdStudent.getIDCard());
						zdStudent.setUpdateTime(new Date());
						zdStudent.setSiteId(siteId);
						zdStudent.setPassword(zdStudent2.getPassword());
						zdStudentService.saveOrUpdate(zdStudent);
						result.put("id", zdStudent.getId());
						result.put("code", Init.SUCCEED);
						result.put("msg", "恭喜您，修改成功");
						request.getSession().setAttribute(Init.APPLY_USER, this.zdStudentService.load(zdStudent.getId(),siteId));
					}
					
				}
			}else{
				result.put("code", Init.FALSE);
				result.put("msg", "未进入任何站点,请刷新");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printDateTimeHTML(response, result);
	}

	/**
	 * 保存民航子弟小学报名修改信息
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param zdStudent
	 */
	@RequestMapping( value="doEdit2" )
	public void doEdit2(HttpServletRequest request,HttpServletResponse response,ZdStudent2 zdStudent){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "提交失败，请重试");
		
		Integer siteId = siteService.getSiteId(request);
		try {
			if (siteId>0) {
				String validCode=request.getSession().getAttribute("validCode")+"";
				String inputValidCode=request.getParameter("validCode")+"";
				if (StringUtil.isEmpty(validCode)||StringUtil.isEmpty(inputValidCode)||!validCode.equals(inputValidCode)) {
					result.put("msg", "提交失败，验证输入有误");
				}else if(zdStudent.getId()==null&&zdStudent.getId()<=0){
					result.put("msg", "抱歉，您还没有报名，不能修改信息");
				}else if(StringUtil.isEmpty(zdStudent.getCompanyName())
						||StringUtil.isEmpty(zdStudent.getDomicilCity())
						||StringUtil.isEmpty(zdStudent.getDomiciProvince())
						||StringUtil.isEmpty(zdStudent.getGender())
						||StringUtil.isEmpty(zdStudent.getGraduate())
						||StringUtil.isEmpty(zdStudent.getHeadPicUrl())
						||StringUtil.isEmpty(zdStudent.getHomeAddress())
						||StringUtil.isEmpty(zdStudent.getIDCard())
						||StringUtil.isEmpty(zdStudent.getName())
						||StringUtil.isEmpty(zdStudent.getNativePlaceCity())
						||StringUtil.isEmpty(zdStudent.getNativePlaceProvince())
						||StringUtil.isEmpty(zdStudent.getFullName1())
						||StringUtil.isEmpty(zdStudent.getRelationship1())
						||StringUtil.isEmpty(zdStudent.getTelephone1())
						||StringUtil.isEmpty(zdStudent.getUnit1())
						||zdStudent.getBirthday()==null){
					result.put("msg", "报名信息不完整，请填写完整报名信息后再提交报名");
				}else{	
					ZdStudent2 zdStudent2 = this.zdStudent2Service.get(zdStudent.getId());
					if(!zdStudent.getIDCard1().equals(zdStudent2.getAccount()) && this.zdStudent2Service.getByAccount(zdStudent.getIDCard1()) != null){
						result.put("msg", "该身份证号已报名，不能重复报名");
					}else{
						zdStudent.setCompanyName(zdStudent.getCompanyName().trim());
						zdStudent.setAccount(zdStudent.getIDCard1());
						zdStudent.setUpdateTime(new Date());
						zdStudent.setSiteId(siteId);
						zdStudent.setPassword(zdStudent2.getPassword());
						zdStudent2Service.saveOrUpdate(zdStudent);
						result.put("id", zdStudent.getId());
						result.put("code", Init.SUCCEED);
						result.put("msg", "恭喜您，修改成功");
						request.getSession().setAttribute(Init.APPLY_USER2, this.zdStudent2Service.load(zdStudent.getId(),siteId));
					}
					
				}
			}else{
				result.put("code", Init.FALSE);
				result.put("msg", "未进入任何站点,请刷新");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printDateTimeHTML(response, result);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	/**
	 * 进入打印民航子弟初中学生报名详情界面
	 * @author lfq
	 * @time 2015-3-18
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param year		创建年份:格式yyyy
	 * @param keyword	关键字
	 * @param ids		民航子弟学生id,多个用逗号隔开
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value="toPrint")
	public String toPrint(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String year,String keyword,String ids) throws Exception{
		Integer siteId = siteService.getSiteId(request);//后台登录的的siteId
		modelMap.put("dzStudentList", zdStudentService.getZdStudentList(siteId, year, keyword, StringUtil.splitToArray(ids, ",")));
		List<ApplyInfo> applyInfo = applyInfoService.getApplyInfo(siteId, "01");
		modelMap.put("interviewNote", applyInfo.get(0).getInterviewNote());
		return "apply/zdstudent/print";
	}

	/**
	 * 进入打印民航子弟小学学生报名详情界面
	 * @author lfq
	 * @time 2015-3-18
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param year		创建年份:格式yyyy
	 * @param keyword	关键字
	 * @param ids		民航子弟学生id,多个用逗号隔开
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value="toPrint2")
	public String toPrint2(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String year,String keyword,String ids) throws Exception{
		Integer siteId = siteService.getSiteId(request);//后台登录的的siteId
		modelMap.put("dzStudentList", zdStudentService.getZdStudent2List(siteId, year, keyword, StringUtil.splitToArray(ids, ",")));
		List<ApplyInfo> applyInfo = applyInfoService.getApplyInfo(siteId, "02");
		modelMap.put("interviewNote", applyInfo.get(0).getInterviewNote());
		return "apply/zdstudent2/print";
	}
	
	@RequestMapping("toEditTestCard")
	public String toEditTestCard(Integer id, ModelMap map){
		map.put("student", zdStudentService.get(id));
		return "apply/zdstudent/editTestCard";
	}
	
	@RequestMapping("updateTestCard")
	public void updateTestCard(HttpServletRequest request, HttpServletResponse response, String testCard, Integer id){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", Init.FAIL);
		if(testCard != null && !testCard.trim().equals("")){
			Map<String, Object> student = new HashMap<String, Object>();
			student.put("testCard", testCard);
			student.put("id", id);
			int count = zdStudentService.update(student);
			if(count > 0){
				resultMap.put("code", Init.SUCCEED);
			}else{
				resultMap.put("msg", "保存失败");
			}
		}else{
			resultMap.put("msg", "准考证号不能为空");
		}
		JSONUtil.printDateTimeHTML(response, resultMap);
	}
	
//	/**
//	 * 保存注册信息
//	 * @author lifq
//	 * @param request
//	 * @param response
//	 */
//	@RequestMapping( value="doRegister" )
//	public void doRegister(HttpServletRequest request,HttpServletResponse response){
//		Map<String, Object> result=new HashMap<String, Object>();
//		result.put("code", Init.FAIL);
//		result.put("msg", "提交失败，请重试");
//		result.put("newAccount", "");
//
//		Integer siteId = siteService.getSiteId(request);
//		try {
//			if (siteId>0) {
//				String validCode=request.getSession().getAttribute("validCode")+"";
//				String inputValidCode=request.getParameter("validCode")+"";
//				if (StringUtil.isEmpty(validCode)||StringUtil.isEmpty(inputValidCode)||!validCode.equals(inputValidCode)) {
//					result.put("msg", "提交失败，验证输入有误");
//				}else{
//					Integer id=Integer.parseInt("0"+request.getParameter("id"));
//					Integer studentId=UserCenterAction.getSessionUserId(request);
//					if (id!=null&&id>0) {
//						if (studentId==null||studentId<1||studentId.intValue()!=id.intValue()) {
//							result.put("msg", "登录超时，请刷新界面后重新登录在进行修改");
//							throw new Exception("登录超时或非法操作，请重新登录");
//						}
//					}
//					String name=request.getParameter("name");//学生姓名
//					String account;							//学生帐号
//					String certificateType=request.getParameter("certificateType");		//证件类型
//					String certificate=request.getParameter("certificate");				//证件号码
//					String enrollmentNumbers=request.getParameter("enrollmentNumbers");	//小学学籍号
//					String gender=request.getParameter("gender");						//性别
//					String nationality=request.getParameter("nationality");				//民族
//					String nativePlace=request.getParameter("nativePlace");				//籍贯
//					String domiciProvince=request.getParameter("domiciProvince");		//户籍所在省
//					String domicilCity=request.getParameter("domicilCity");				//户籍所在地-市	
//					String domicileArea=request.getParameter("domicileArea");			//户籍所在地-区		
//					String domicile=request.getParameter("domicile");					//户籍所在地-派出所地址
//					String homeAddress=request.getParameter("homeAddress");				//家庭住址
//					String graduateProvince=request.getParameter("graduateProvince");	//毕业学校-省
//					String graduateCity=request.getParameter("graduateCity");			//毕业学校-市
//					String graduateArea=request.getParameter("graduateArea");			//毕业学校-区
//					String graduate=request.getParameter("graduate");					//毕业学校
//					String birthdayStr=request.getParameter("birthday");				//出生日期
//					String phoneNumber=request.getParameter("phoneNumber");				//联系手机
//					String rewardHobby=request.getParameter("rewardHobby");				//获奖及个人特长
//					String fullName1=request.getParameter("fullName1");						// 联系人
//					String unit1=request.getParameter("unit1");								// 所在单位
//					String position1=request.getParameter("position1");						// 职务
//					String telephone1=request.getParameter("telephone1");					// 联系电话
//					String fullName2=request.getParameter("fullName2");						//
//					String unit2=request.getParameter("unit2");								//
//					String position2=request.getParameter("position2");						//
//					String telephone2=request.getParameter("telephone2");					//
//					String password=request.getParameter("password");						// 密码，保存的时候MD5加密
//					String headPicUrl=request.getParameter("headPicUrl");					//头像
//					
//					
//					
//					//设定生日
//					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//					Date birthday=dateFormat.parse(birthdayStr);
//					
//					ZdStudent zdstudent2;
//					if (id!=null&&id>0) {
//						zdstudent2=this.zdStudentService.get(id);
//						if(zdstudent2.getStatus()==3){//如果是审核回退的，再次提交变为待审核状态
//							zdstudent2.setStatus(0);
//						}
//					}else{
//						String  maxAccount=this.zdStudentService.finMaxAccountByName(name);
//						//设定帐号
//						if (maxAccount==null) {
//							account=name;
//						}else{
//							Integer index=Integer.parseInt("0"+maxAccount.replace(name, ""));
//							account=name+(index+1);
//						}
//						zdstudent2=new ZdStudent();
//						zdstudent2.setAccount(account);
//						zdstudent2.setAdmit(-1);//是否录取，未公布
//						zdstudent2.setStatus(0);//未
//						zdstudent2.setCreateTime(new Date());
//						zdstudent2.setSiteId(siteId);
//						zdstudent2.setPassword(CryptUtil.MD5encrypt(password));
//					}
//					
//					zdstudent2.setName(name);
////					zdstudent2.setCertificateType(certificateType);
////					zdstudent2.setCertificate(certificate);
//					zdstudent2.setEnrollmentNumbers(enrollmentNumbers);
//					zdstudent2.setGender(gender);
//					zdstudent2.setNationality(nationality);
//					zdstudent2.setNativePlace(nativePlace);
//					zdstudent2.setDomiciProvince(domiciProvince);
//					zdstudent2.setDomicilCity(domicilCity);
//					zdstudent2.setDomicileArea(domicileArea);
//					zdstudent2.setDomicile(domicile);
//					zdstudent2.setHomeAddress(homeAddress);
//					zdstudent2.setGraduateProvince(graduateProvince);
//					zdstudent2.setGraduateCity(graduateCity);
//					zdstudent2.setGraduateArea(graduateArea);
//					zdstudent2.setGraduate(graduate);
//					zdstudent2.setBirthday(birthday);
//					zdstudent2.setPhoneNumber(phoneNumber);
//					zdstudent2.setRewardHobby(rewardHobby);
//					zdstudent2.setFullName1(fullName1);
//					zdstudent2.setRelationship1("父亲");
//					zdstudent2.setUnit1(unit1);
//					zdstudent2.setPosition1(position1);
//					zdstudent2.setTelephone1(telephone1);
//					zdstudent2.setFullName2(fullName2);
//					zdstudent2.setRelationship2("母亲");
//					zdstudent2.setUnit2(unit2);
//					zdstudent2.setPosition2(position2);
//					zdstudent2.setTelephone2(telephone2);
//					zdstudent2.setHeadPicUrl(headPicUrl);
//					zdstudent2.setUpdateTime(new Date());
//					
//					
//					if (id!=null&&id>0) {
//						if (zdstudent2.getStatus()==0||zdstudent2.getStatus()==3) {
//							studentService.update(zdstudent2);
//							result.put("code", Init.SUCCEED);
//							result.put("msg", "成功修改");
//						}else{//报名状态：0待审核;1审核通过；2审核不通过；3审核回退
//							result.put("msg", "修改失败，你的当前状态为【"+(zdstudent2.getStatus()==1?"审核通过":"审核不通过")+"】,不允许修改报名信息");
//						}
//					}else{
//						Integer newId=(Integer) studentService.save(zdstudent2);
//						zdstudent2.setId(newId);
//						result.put("code", Init.SUCCEED);
//						result.put("newAccount", zdstudent2.getAccount());
//						result.put("msg", "保存成功");
//					}
//					request.getSession().setAttribute(Init.APPLY_USER, this.studentService.load(zdstudent2.getId(),siteId));
//				}
//			}else{
//				result.put("code", Init.FALSE);
//				result.put("msg", "未进入任何站点");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		JSONUtil.printDateTimeHTML(response, result);
//	}
	
}
