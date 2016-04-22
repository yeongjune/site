package com.apply.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.model.RecruitDate;
import com.apply.model.Student;
import com.apply.service.CertificateService;
import com.apply.service.RecruitDateService;
import com.apply.service.StudentService;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.model.Data;
import com.site.service.SiteService;

@Controller
@RequestMapping( value="register")
public class RegisterAction {
	@Autowired
	private RecruitDateService recruitDateService;
	@Autowired
	private CertificateService certificateService;

	@Autowired 
	private StudentService studentService;

	@Autowired
	private SiteService siteService;

	
	/**
	 * 加载注册也没数据
	 * @author lifq
	 */
	@RequestMapping( value="loadRegisterData" )
	public void loadRegisterDate(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result=new HashMap<String, Object>();
		Integer siteId = siteService.getSiteId(request);
		result.put("code", Init.FAIL);
		if (siteId>0) {
			RecruitDate recruitDate=recruitDateService.load(siteId);
			List<Map<String, Object>> certificateList= certificateService.findCertificateList(null, siteId);
			result.put("recruitDate", recruitDate);//报名日期
			result.put("certificateList", certificateList);//证件类型列表
			Date currentDate=new Date();
			
			if (recruitDate!=null && recruitDate.getStartDate().compareTo(currentDate)<=0 &&recruitDate.getEndDate().compareTo(currentDate) >=0 ) {
				result.put("hasApply", 1);//
			}else{
				result.put("hasApply", 0);//
			}
			
			Integer studentId=UserCenterAction.getSessionUserId(request);
			if (studentId!=null) {//如果是修改则加载出用户信息
				result.put("student", this.studentService.load(studentId,siteId));
			}
			result.put("code", Init.SUCCEED);
			
		}else{
			result.put("code", Init.FALSE);
		}
		JSONUtil.printDateTimeHTML(response, result);
	}
	
	/**
	 * 保存注册信息
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping( value="doRegister" )
	public void doRegister(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "提交失败，请重试");
		result.put("newAccount", "");

		Integer siteId = siteService.getSiteId(request);
		try {
			if (siteId>0) {
				String validCode=request.getSession().getAttribute("validCode")+"";
				String inputValidCode=request.getParameter("validCode")+"";
				if (StringUtil.isEmpty(validCode)||StringUtil.isEmpty(inputValidCode)||!validCode.equals(inputValidCode)) {
					result.put("msg", "提交失败，验证输入有误");
				}else{
					Integer id=Integer.parseInt("0"+request.getParameter("id"));
					Integer studentId=UserCenterAction.getSessionUserId(request);
					if (id!=null&&id>0) {
						if (studentId==null||studentId<1||studentId.intValue()!=id.intValue()) {
							result.put("msg", "登录超时，请刷新界面后重新登录在进行修改");
							throw new Exception("登录超时或非法操作，请重新登录");
						}
					}
					String name=request.getParameter("name");//学生姓名
					String account;							//学生帐号
					String certificateType=request.getParameter("certificateType");		//证件类型
					String certificate=request.getParameter("certificate");				//证件号码
					String enrollmentNumbers=request.getParameter("enrollmentNumbers");	//小学学籍号
					String gender=request.getParameter("gender");						//性别
					String nationality=request.getParameter("nationality");				//民族
					String nativePlace=request.getParameter("nativePlace");				//籍贯
					String domiciProvince=request.getParameter("domiciProvince");		//户籍所在省
					String domicilCity=request.getParameter("domicilCity");				//户籍所在地-市	
					String domicileArea=request.getParameter("domicileArea");			//户籍所在地-区		
					String domicile=request.getParameter("domicile");					//户籍所在地-派出所地址
					String homeAddress=request.getParameter("homeAddress");				//家庭住址
					String graduateProvince=request.getParameter("graduateProvince");	//毕业学校-省
					String graduateCity=request.getParameter("graduateCity");			//毕业学校-市
					String graduateArea=request.getParameter("graduateArea");			//毕业学校-区
					String graduate=request.getParameter("graduate");					//毕业学校
					String birthdayStr=request.getParameter("birthday");				//出生日期
					String phoneNumber=request.getParameter("phoneNumber");				//联系手机
					String rewardHobby=request.getParameter("rewardHobby");				//获奖及个人特长
					String fullName1=request.getParameter("fullName1");						// 联系人
					String unit1=request.getParameter("unit1");								// 所在单位
					String position1=request.getParameter("position1");						// 职务
					String telephone1=request.getParameter("telephone1");					// 联系电话
					String fullName2=request.getParameter("fullName2");						//
					String unit2=request.getParameter("unit2");								//
					String position2=request.getParameter("position2");						//
					String telephone2=request.getParameter("telephone2");					//
					String password=request.getParameter("password");						// 密码，保存的时候MD5加密
					String headPicUrl=request.getParameter("headPicUrl");					//头像
					
					
					
					//设定生日
					SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
					Date birthday=dateFormat.parse(birthdayStr);
					
					Student student2;
					if (id!=null&&id>0) {
						student2=this.studentService.get(id);
						if(student2.getStatus()==3){//如果是审核回退的，再次提交变为待审核状态
							student2.setStatus(0);
						}
					}else{
						String  maxAccount=this.studentService.finMaxAccountByName(name);
						//设定帐号
						if (maxAccount==null) {
							account=name;
						}else{
							Integer index=Integer.parseInt("0"+maxAccount.replace(name, ""));
							account=name+(index+1);
						}
						student2=new Student();
						student2.setAccount(account);
						student2.setAdmit(-1);//是否录取，未公布
						student2.setStatus(0);//未
						student2.setCreateTime(new Date());
						student2.setSiteId(siteId);
						student2.setPassword(CryptUtil.MD5encrypt(password));
					}
					
					student2.setName(name);
					student2.setCertificateType(certificateType);
					student2.setCertificate(certificate);
					student2.setEnrollmentNumbers(enrollmentNumbers);
					student2.setGender(gender);
					student2.setNationality(nationality);
					student2.setNativePlace(nativePlace);
					student2.setDomiciProvince(domiciProvince);
					student2.setDomicilCity(domicilCity);
					student2.setDomicileArea(domicileArea);
					student2.setDomicile(domicile);
					student2.setHomeAddress(homeAddress);
					student2.setGraduateProvince(graduateProvince);
					student2.setGraduateCity(graduateCity);
					student2.setGraduateArea(graduateArea);
					student2.setGraduate(graduate);
					student2.setBirthday(birthday);
					student2.setPhoneNumber(phoneNumber);
					student2.setRewardHobby(rewardHobby);
					student2.setFullName1(fullName1);
					student2.setRelationship1("父亲");
					student2.setUnit1(unit1);
					student2.setPosition1(position1);
					student2.setTelephone1(telephone1);
					student2.setFullName2(fullName2);
					student2.setRelationship2("母亲");
					student2.setUnit2(unit2);
					student2.setPosition2(position2);
					student2.setTelephone2(telephone2);
					student2.setHeadPicUrl(headPicUrl);
					student2.setUpdateTime(new Date());
					
					
					if (id!=null&&id>0) {
						if (student2.getStatus()==0||student2.getStatus()==3) {
							studentService.update(student2);
							result.put("code", Init.SUCCEED);
							result.put("msg", "成功修改");
						}else{//报名状态：0待审核;1审核通过；2审核不通过；3审核回退
							result.put("msg", "修改失败，你的当前状态为【"+(student2.getStatus()==1?"审核通过":"审核不通过")+"】,不允许修改报名信息");
						}
					}else{
						Integer newId=(Integer) studentService.save(student2);
						student2.setId(newId);
						result.put("code", Init.SUCCEED);
						result.put("newAccount", student2.getAccount());
						result.put("msg", "保存成功");
					}
					request.getSession().setAttribute(Init.APPLY_USER, this.studentService.load(student2.getId(),siteId));
				}
			}else{
				result.put("code", Init.FALSE);
				result.put("msg", "未进入任何站点");
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
		binder.registerCustomEditor(Data.class, new CustomDateEditor(dateFormat, true));
	}
}
