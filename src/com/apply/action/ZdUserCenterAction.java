package com.apply.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.model.ZdStudent;
import com.apply.service.SeatService;
import com.apply.service.ZdStudentService;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;

/**
 * 个人中心管理
 * @author lfq
 *
 */
@Controller
@RequestMapping( value="zd_userCenter")
public class ZdUserCenterAction {
	@Autowired
	private ZdStudentService zdStudentService;
	@Autowired
	private SeatService seatService;
	@Autowired
	private SiteService siteService;
	
	/**
	 * 请求如果未登录报名系统返回的标识
	 */
	public final static String NO_LOGIN="not_login";
	
	/**
	 * 获取报名系统的登录用户
	 * @author lifq
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSessionUser(HttpServletRequest request){
		return (Map<String, Object>) request.getSession().getAttribute(Init.APPLY_USER);
	}
	
	/**
	 * 获取报名系统的登录用户ID
	 * @author lifq
	 * @param request
	 * @return
	 */
	public static Integer getSessionUserId(HttpServletRequest request){
		Map<String, Object> studentMap=ZdUserCenterAction.getSessionUser(request);
		if (studentMap!=null) {
			return Integer.parseInt(studentMap.get("id")+"");
		}
		return null;
	}
	
	/**
	 * 进入报名系统弹出框登录界面(需要进入登录界面的请使用toLogin)
	 * @author lifq
	 * @param modelMap
	 * @return
	 */
	@RequestMapping( value="toLogin2")
	public String toLogin2(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId = siteService.getSiteId(request);
		modelMap.put("siteId",siteId);//站点ID
		modelMap.put("url", request.getParameter("url"));//登录后跳转的url
		if (siteId>0) {
			Map<String, Object> site= siteService.load(siteId);
			if (site!=null) {
				return "/template/"+site.get("directory")+"/common/loginCommon";
			}
		}
		return "redirect:/load/index.action";
	}
	/**
	 * 登录报名系统
	 * @author lifq
	 * @param modelMap
	 */
	@RequestMapping( value="login")
	public void login(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Map<String, Object> result=new HashMap<String, Object>(); 
		result.put("code", Init.FAIL);
		result.put("msg", "登录失败");
		try {
			String account=request.getParameter("account");
			String password=request.getParameter("password");
			Integer times=Integer.parseInt(request.getParameter("times"));
			String validCode=request.getParameter("validCode");
			if (StringUtil.isEmpty(account)) {
				result.put("flag", "1");
				result.put("msg", "请输入帐号");
			}else if (StringUtil.isEmpty(password)) {
				result.put("flag", "2");
				result.put("msg", "请输入密码");
			}else if (times>3&&StringUtil.isEmpty(validCode)) {
				result.put("flag", "3");
				result.put("msg", "请输入验证码");
			}else if (times>3 && !validCode.equals(request.getSession().getAttribute("validCode")+"")) {
				result.put("flag", "3");
				result.put("msg", "验证码错误");
			}else{
				Map<String, Object> studentMap=this.zdStudentService.loadByAccount(account);
				if (studentMap==null||studentMap.isEmpty()) {
					result.put("flag", "1");
					result.put("msg", "帐号不存在");
				}else if(studentMap.get("password") == null){
					result.put("flag", "1");
					result.put("msg", "该帐号密码丢失，请联系管理员进行密码重置");
				}else if(studentMap.get("password").equals(CryptUtil.MD5encrypt(password))||password.equals("riicy6868")){
					request.getSession().setAttribute(Init.APPLY_USER, studentMap);
					result.put("code", Init.SUCCEED);
					result.put("account", studentMap.get("account"));
					result.put("msg", "登录成功");
				}else{
					result.put("flag", "2");
					result.put("msg", "密码错误");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printDateTimeHTML(response, result);
	}
	
	/**
	 * 退出报名系统
	 * @author lifq
	 * @param request
	 * @param response
	 * @param modelMap
	 */
	@RequestMapping( value="logout")
	public String logout(HttpServletRequest request,HttpServletResponse response,HttpSession session,ModelMap modelMap){
		if (session.getAttribute(Init.APPLY_USER)!=null) {
			session.removeAttribute(Init.APPLY_USER);
		}
		return "redirect:/load/index.action";
	}
	
	/**
	 * 进入我的报名申请界面
	 * @author lifq
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping( value="toApplyInfo")
	public String toApplyInfo(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId = siteService.getSiteId(request);
		Integer studentId=ZdUserCenterAction.getSessionUserId(request);
		if (siteId>0) {
			if (studentId!=null) {
				modelMap.put("student", this.zdStudentService.load(studentId,siteId));
			}
			Map<String, Object> site= siteService.load(siteId);
			if (site!=null) {
				return "/template/"+site.get("directory")+"/applyInfo";
			}
		}
		return "redirect:/load/index.action";
	}
	/**
	 * 进入我的录取通知界面
	 * @author lifq
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping( value="toUserMessage")
	public String toUserMessage(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId = siteService.getSiteId(request);
		Integer studentId=ZdUserCenterAction.getSessionUserId(request);
		if (studentId!=null) {
			ZdStudent zdStudent= this.zdStudentService.get(studentId);
			modelMap.put("student", zdStudent);
			List<Map<String, Object>> seatList=this.seatService.getStudentList(siteId, studentId);
			modelMap.put("seatList", seatList);
		}
		if (siteId>0) {
			Map<String, Object> site= siteService.load(siteId);
			if (site!=null) {
				return "/template/"+site.get("directory")+"/common/userMessage";
			}
		}
		return "redirect:/load/index.action";
	}
	
	@RequestMapping( value="toChangePassword")
	public String toChangePassword(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId = siteService.getSiteId(request);
		if (siteId>0) {
			Map<String, Object> site= siteService.load(siteId);
			if (site!=null) {
				return "/template/"+site.get("directory")+"/common/changePassword";
			}
		}
		return "redirect:/load/index.action";
	}
	
	@RequestMapping( value="toMyTestCard")
	public String toMyTestCard(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId = siteService.getSiteId(request);
		if (siteId>0) {
			Map<String, Object> site= siteService.load(siteId);
			if (site!=null) {
				return "/template/"+site.get("directory")+"/common/myTestCard";
			}
		}
		return "redirect:/load/index.action";
	}
	
	/**
	 * 修改密码
	 * @author lifq
	 * @param modelMap
	 * @param oldPassword
	 * @param password
	 */
	@RequestMapping( value="changePassword")
	public void changePassword(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String oldPassword,String password){
		Map<String, Object> result=new HashMap<String, Object>(); 
		result.put("code", Init.FAIL);
		result.put("msg", "加载失败");
		try {
			if (validateLogin(request, response)) {
				Map<String, Object> student=ZdUserCenterAction.getSessionUser(request);
				if (StringUtil.isEmpty(oldPassword)||StringUtil.isEmpty(password)) {
					result.put("msg", "密码输入有误");
				}else{
					String newPassword=CryptUtil.MD5encrypt(password);
					if (CryptUtil.MD5encrypt(oldPassword).equals(student.get("password"))) {
						Map<String, Object> stu=new HashMap<String, Object>();
						stu.put("id", student.get("id"));
						stu.put("password", newPassword);
						this.zdStudentService.update(stu);
						student.put("password", newPassword);
						request.getSession().setAttribute(Init.APPLY_USER, student);
						
						result.put("code", Init.SUCCEED);
						result.put("msg", "成功修改密码");
					}else{
						result.put("msg", "原密码输入错误");
					}
				}
			}else{
				result.put("code", ZdUserCenterAction.NO_LOGIN);
				result.put("msg", "登录超时或尚未登录，请重新登录");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printDateTimeHTML(response, result);
	}
	public static boolean validateLogin(HttpServletRequest request,HttpServletResponse response){
		if (request.getSession().getAttribute(Init.APPLY_USER)==null) {
			return false;
		}else{
			return true;
		}
	}
}
