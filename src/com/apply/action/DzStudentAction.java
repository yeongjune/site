package com.apply.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import com.apply.model.DzStudent;
import com.apply.service.DzStudentService;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;

/**
 * 民航子弟初中报名请求处理控制器
 * @author lfq
 * @time 2015-3-16
 *
 */
@Controller
@RequestMapping(value="dzStudent")
public class DzStudentAction {
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private DzStudentService dzStudentService;

	/**
	 * 保存民航子弟初中报名信息
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param dzStudent
	 */
	@RequestMapping( value="doRegister" )
	public void doRegister(HttpServletRequest request,HttpServletResponse response,DzStudent dzStudent){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "提交失败，请重试");

		Integer siteId = siteService.getSiteId(request);
		//System.out.println("fdgskldjk----------======================");
		try {
			if (siteId>0) {
				String validCode=request.getSession().getAttribute("validCode")+"";
				String inputValidCode=request.getParameter("validCode")+"";
				if (StringUtil.isEmpty(validCode)||StringUtil.isEmpty(inputValidCode)||!validCode.equals(inputValidCode)) {
					result.put("msg", "提交失败，验证输入有误");
				}else if(dzStudent.getId()!=null&&dzStudent.getId()>0){
					result.put("msg", "抱歉，您已提交过报名");
				}else if(StringUtil.isEmpty(dzStudent.getCompanyName())
						||StringUtil.isEmpty(dzStudent.getDomicilCity())
						||StringUtil.isEmpty(dzStudent.getDomiciProvince())
						||StringUtil.isEmpty(dzStudent.getGender())
						||StringUtil.isEmpty(dzStudent.getGraduate())
						||StringUtil.isEmpty(dzStudent.getHeadPicUrl())
						||StringUtil.isEmpty(dzStudent.getHomeAddress())
						||StringUtil.isEmpty(dzStudent.getIDCard())
						||StringUtil.isEmpty(dzStudent.getName())
						||StringUtil.isEmpty(dzStudent.getNativePlaceCity())
						||StringUtil.isEmpty(dzStudent.getNativePlaceProvince())
						||StringUtil.isEmpty(dzStudent.getFullName1())
						||StringUtil.isEmpty(dzStudent.getRelationship1())
						||StringUtil.isEmpty(dzStudent.getTelephone1())
						||StringUtil.isEmpty(dzStudent.getUnit1())
						||StringUtil.isEmpty(dzStudent.getInSchoolNo())
						||dzStudent.getBirthday()==null){
					result.put("msg", "报名信息不完整，请填写完整报名信息后再提交报名");
				}else{	
					dzStudent.setSiteId(siteId);
					dzStudent.setCreateTime(new Date());
					dzStudentService.saveOrUpdate(dzStudent);
					result.put("id", dzStudent.getId());
					result.put("code", Init.SUCCEED);
					result.put("msg", "恭喜您，成功提交报名");
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
   public void initBinder(WebDataBinder binder) {  
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
       dateFormat.setLenient(false);  
       binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));  
   }  
   

	/**
	 * 进入民航子弟初中学生报名管理界面
	 * @author lfq
	 * @time 2015-3-17
	 * @return
	 */
	@RequestMapping( value="index")
	public String index(ModelMap modelMap){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("year", dateFormat.format(new Date()));
		return "apply/minghangdizi/index";
	}
	
	/**
	 * 分页查询民航子弟初中学生报名信息
	 * @author lfq
	 * @param keyword	关键字模糊查询(学院名)
	 * @param pageSize  每页记录数
	 * @param currentPage 当前页
	 * @param year		创建年份
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request,HttpServletResponse response,
			Integer pageSize,Integer currentPage,String keyword,String year){
		currentPage = Init.getCurrentPage(currentPage);
		pageSize = Init.getPageSize(pageSize);
		Integer siteId = User.getCurrentSiteId(request);
		JSONUtil.printToHTML(response, this.dzStudentService.getDzStudentPageList(siteId, pageSize, currentPage, keyword,year));
	}
	
	/**
	 * 删除民航子弟初中学生报名信息
	 * @author lifq
	 * @time 2015-3-17
	 * @param ids 民航子弟学生id,多个用英文逗号隔开
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
					int count= this.dzStudentService.delete(siteId,StringUtil.splitToArray(ids, ","));
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
	 * 进入民航子弟初中学生报名详情界面
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param id 	民航子弟学生id
	 * @return
	 */
	@RequestMapping( value="view")
	public String view(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id){
		modelMap.put("id", id);
		Integer siteId = User.getCurrentSiteId(request);
		try {
			if (siteId>0 && id!=null) {
				DzStudent dzStudent=dzStudentService.getDzStudent(id);
				if (dzStudent!=null && dzStudent.getSiteId().intValue()==siteId) {
					modelMap.put("student", dzStudent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/minghangdizi/view";
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
		Integer siteId = User.getCurrentSiteId(request);//后台登录的的siteId
		modelMap.put("dzStudentList", dzStudentService.getDzStudentList(siteId, year, keyword, StringUtil.splitToArray(ids, ",")));
		return "apply/minghangdizi/print";
	}
}
