package com.site.action;

import java.util.Arrays;
import java.util.Date;
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

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.base.util.JSONUtil;
import com.base.util.MD5Util;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.model.SiteUser;
import com.site.service.SiteUserService;
import com.site.vo.SiteUserSearchVo;

/**
 * 站点用户管理控制器
 * @author lfq
 * @2014-5-7
 */
@Controller
@RequestMapping(value="siteUser")
public class SiteUserAction {
	
	
	
	@Autowired
	private SiteUserService siteUserService;
	
	/**
	 * 进入后台站点用户管理界面
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request,HttpServletResponse response, ModelMap map){
		Integer siteId = User.getCurrentSiteId(request);
		//查询部门类型
		List<Map<String, Object>> typeList = siteUserService.findDepartmentTypes(siteId);
		map.put("typeList", typeList);
		return "site/siteUser/index";
	}
	
	/**
	 * 查询站点用户列表
	 * @author lfq
	 */
	@RequestMapping(value="list")
	public void list(HttpServletRequest request,HttpServletResponse response,Integer pageSize,Integer currentPage,SiteUserSearchVo searchVo){
		currentPage = Init.getCurrentPage(currentPage);
		pageSize = Init.getPageSize(pageSize);
		if (searchVo==null) {
			searchVo=new SiteUserSearchVo();
		}
		searchVo.setSiteId(User.getCurrentSiteId(request));
		PageList pageList=this.siteUserService.findSiteUserPageList(searchVo, pageSize, currentPage);
		JSONUtil.printToHTML(response, pageList);
	}
	
	/**
	 * 删除站点用户
	 * @author lfq
	 */
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,String ids){
		String result=Init.FAIL;
		if (!StringUtil.isEmpty(ids)) {
			String [] idArray=ids.split(",");
			Integer [] idIntegerArray=new Integer[idArray.length];
			for (int i = 0; i < idArray.length; i++) {
				idIntegerArray[i]=Integer.parseInt(idArray[i]);
			}
			int temp= this.siteUserService.delete(idIntegerArray);
			result=temp>0?Init.SUCCEED:Init.FAIL;
		}
		JSONUtil.print(response, result);
	}
	
	/**
	 * 修改站点用户状态：
	 * @author lifq
	 * @param id
	 */
	@RequestMapping(value="changeStatus")
	public void changeStatus(HttpServletRequest request,HttpServletResponse response,Integer id,Integer status){
		String result=Init.FAIL;
		if (id!=null&&status!=null&&Arrays.<Integer>asList(new Integer[]{-2,-1,0,1}).contains(status)) {
			Map<String, Object> siteUser=new HashMap<String, Object>();
			siteUser.put("id", id);
			siteUser.put("status", status);
			int temp= this.siteUserService.update(siteUser);
			result=temp>0?Init.SUCCEED:Init.FAIL;
		}
		JSONUtil.print(response, result);
	}
	
	/**
	 * 进入站点帐号注册界面
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="toRegister")
	public String toRegister(HttpServletRequest request,String local){
		//自定义注册界面，传递文件夹ID,格式custom_17_en_US
		if(!StringUtil.isEmpty(local)&&local.indexOf("custom") != -1){
			if(local.indexOf("en_US") != -1){
				return "template/" + local.substring(8, local.indexOf("en_US")) + "/register_en";
			}else{
				return "template/" + local.substring(8, local.length()) + "/register";
			}
		}
		//学校通用注册界面
		else if (!StringUtil.isEmpty(local)&&"school".equals(local)) { 
			return "template/school_general_page/register";
		} else if(!StringUtil.isEmpty(local)&&"school_en_US".equals(local)){
			return "template/school_general_page/register_en";
		} 
		//一般登录注册界面
		else if(!StringUtil.isEmpty(local)&&"en_US".equals(local) ){ 
			return "template/register_en";
		} else{
			return "template/register";
		}
	}
	
	/**
	 * 进入站点帐号登录界面
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="toLogin")
	public String toLogin(HttpServletRequest request,String local){
		if (!StringUtil.isEmpty(local)&&"en_US".equals(local)) {
			return "template/login_en";
		}else{
			return "template/login";
		}
	}
	
	/**
	 * 用户退出站点的登录
	 * @author lfq
	 * @return
	 */
	@RequestMapping(value="logout")
	public String logout(HttpServletRequest request,HttpSession session){
		if (session.getAttribute(Init.SITE_USER)!=null) {
			session.removeAttribute(Init.SITE_USER);
		}
		return "redirect:/load/index.action";
	}
	
	/**
	 * 注册
	 * @author lfq
	 * 返回错误数据包含错误代号errorCode:0注册失败，1帐号非空，2密码非空，3注册成功，4用户名已存在
	 */
	@RequestMapping(value="doRegister")
	public void doRegister(HttpServletRequest request,HttpServletResponse response,String local,SiteUser siteUser){
		if (StringUtil.isEmpty(local)) {
			local="zh_CN";
		}
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("errorCode", 0);//错误代号
		result.put("msg", local.equals("en_US") ? "REGIST FAIL" : "注册失败");
		if (siteUser!=null) {
			if (StringUtil.isEmpty(siteUser.getAcount())) {
				result.put("msg", local.equals("en_US") ? "INPUT ACCOUNT PLEASE" : "帐号不能为空");
				result.put("errorCode", 1);
			}else if (StringUtil.isEmpty(siteUser.getPassword())) {
				result.put("msg", local.equals("en_US") ? "INPUT PASSWORD PLEASE" : "密码不能为空");
				result.put("errorCode", 2);
			}else if (this.checkedAcount(request, siteUser.getAcount())) {
				result.put("msg", local.equals("en_US") ? "ACOUNT EXISTS" : "用户名已存在");
				result.put("errorCode", 4);
			}else{
				siteUser.setCreateTime(new Date());
				siteUser.setStatus(1);//默认为启用状态
				Integer siteId = null;
				@SuppressWarnings("unchecked")
				Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
				if(site != null){
					siteId = Integer.parseInt(site.get("id")+"");
				}else{
					siteId = User.getCurrentSiteId(request);
				}
				if (siteId!=null) {
					siteUser.setSiteId(siteId);
					int newId=(Integer) siteUserService.save(siteUser);
					if (newId>0) {
						result.put("code", Init.SUCCEED);
						result.put("msg", local.equals("en_US") ? "REGIST SUCCEED" : "注册成功");
						result.put("errorCode", 3);
					}
				}
			}
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 登录
	 * @author lfq
	 * @param response
	 * @param acount
	 * @param password md5加密
	 * @param validCode 验证码，错误次数超过3次要要输入验证码
	 * @param times    错误次数
	 * 返回数据包括错误代号errorCode:0登录失败，1帐号非空，2密码非空，3登录成功，4用户名不存在，5，密码错误,6验证码错误,7用户未启用
	 */
	@RequestMapping(value="doLogin")
	public void doLogin(HttpServletRequest request,HttpServletResponse response,String local,String acount,String password,String validCode,Integer times){
		if (StringUtil.isEmpty(local)) {
			local="zh_CN";
		}
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("errorCode", 0);//错误代号
		result.put("msg",  local.equals("en_US") ? "LOGIN FAIL" : "登录失败");
		@SuppressWarnings("unchecked")
		Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
		if (StringUtil.isEmpty(acount)) {
			result.put("msg",  local.equals("en_US") ? "INPUT ACCOUNT PLEASE" : "帐号不能为空");
			result.put("errorCode", 1);
		}else if (StringUtil.isEmpty(password)) {
			result.put("msg",  local.equals("en_US") ? "INPUT PASSWORD PLEASE" : "密码不能为空");
			result.put("errorCode", 2);
		}else if (times!=null && times>3 && !validCode.equals(request.getSession().getAttribute("validCode")+"")) {
			result.put("msg",  local.equals("en_US") ? "VALIDATE CODE ERROR" : "验证码错误");
			result.put("errorCode", 6);
		}
		else if(site!=null){
			SiteUserSearchVo searchVo=new SiteUserSearchVo();
			searchVo.setAllAcount(acount);
			searchVo.setSiteId(Integer.parseInt(site.get("id")+""));
			List<Map<String, Object>> list=this.siteUserService.findSiteUserList(searchVo, null, null);
			if (list==null||list.size()<1) {
				result.put("msg",  local.equals("en_US") ? "ACCOUNT ERROR" : "用户名不存在");
				result.put("errorCode", 4);
			}else{
				Map<String, Object> siteUser=list.get(0);
				if (!siteUser.get("password").equals(password)) {
					result.put("msg",  local.equals("en_US") ? "PASSWORD ERROR" : "密码错误");
					result.put("errorCode", 5);
				}else if(!"1".equals(siteUser.get("status")+"")){
					result.put("msg",  local.equals("en_US") ? "ACCOUNT NOT USED" : "用户未启用或被禁用");
					result.put("errorCode", 7);
				}else{
					siteUser.remove("password");
					request.getSession().setAttribute("siteUser", siteUser);//登录后站点用户保存session里
					result.put("errorCode", 3);
					result.put("code", Init.SUCCEED);
				}
			}
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 判断站点用户是否存在
	 * @author lifq
	 * @param request
	 * @param acount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkedAcount(HttpServletRequest request,String acount){
		boolean exists=true;
		if (!StringUtil.isEmpty(acount)) {
			Integer siteId = null;
			Map<String, Object> site=(Map<String, Object>) request.getSession().getAttribute("site");
			if(site != null){
				siteId = Integer.parseInt(site.get("id")+"");
			}else{
				siteId = User.getCurrentSiteId(request);
			}
			if (siteId!=null) {
				SiteUserSearchVo searchVo=new SiteUserSearchVo();
				searchVo.setAllAcount(acount);
				if(site!=null){
					searchVo.setSiteId(Integer.parseInt(site.get("id")+""));
				}else{
					searchVo.setSiteId(siteId);
				}
				List<Map<String, Object>> list= this.siteUserService.findSiteUserList(searchVo, null, null);
				if (list==null||list.size()==0) {
					exists=false;
				}
			}
		}
		return exists;
	}
	
	/**
	 * 打开后台新增客户账号页面
	 * @return
	 */
	@RequestMapping("createInSys")
	public String createInSys(HttpServletRequest request,HttpServletResponse response){
		return "site/siteUser/add";
	}
	

	/**
	 * 进入站点密码修改界面
	 * @param request
	 * @param local
	 * @return
	 */
	@RequestMapping(value="toEditPwd")
	public String toEditPwd(HttpServletRequest request, String local){
		if(!StringUtil.isEmpty(local) && "en_US".equals(local)){
			return "template/editPwd_en";
		} else {
			return "template/editPwd";
		}
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @param id
	 * @param oPassword 旧密码
	 * @param password 新密码
	 */
	@RequestMapping(value="editPwd")
	public void editPwd(HttpServletRequest request,HttpServletResponse response, 
			Integer id, String oPassword, String password){
		if(id != null && oPassword != null && password != null){
			//检查当前用户的原始密码是否正确
			int checkResult = siteUserService.checkPassword(id, oPassword);
			if(checkResult == 1){
				Map<String, Object> siteUser = new HashMap<String, Object>();
				siteUser.put("id", id);
				siteUser.put("password", password);
				int updateResult = siteUserService.update(siteUser);
				JSONUtil.printToHTML(response, updateResult);
			}else{
				JSONUtil.printToHTML(response, checkResult);
			}
		}
	}
	
	/**
	 * 站外用户密码重置
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value="resetPwd")
	public void resetPwd(HttpServletRequest request,HttpServletResponse response, 
			Integer id){
		if(id != null){
			Map<String, Object> siteUser = new HashMap<String, Object>();
			siteUser.put("id", id);
			siteUser.put("password", CryptUtil.MD5encrypt("123456"));
			int result = siteUserService.update(siteUser);
			JSONUtil.printToHTML(response, result);
		}
	}
}
