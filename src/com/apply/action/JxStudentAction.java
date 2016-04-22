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

import com.apply.model.JxStudent;
import com.apply.service.JxStudentService;
import com.apply.vo.JxStudentSearchVo;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;

/**
 *  集贤报名请求处理控制器
 * @author lfq
 * @time 2015-3-16
 *
 */
@Controller
@RequestMapping(value="jxStudent")
public class JxStudentAction {
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private JxStudentService jxStudentService;

	/**
	 * 保存 集贤报名报名信息
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param jxStudent
	 */
	@RequestMapping( value="doRegister" )
	public void doRegister(HttpServletRequest request,HttpServletResponse response,JxStudent jxStudent){
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
				}else if(jxStudent.getId()!=null&&jxStudent.getId()>0){
					result.put("msg", "抱歉，您已提交过报名");
				}else if(StringUtil.isEmpty(jxStudent.getGender())
						||StringUtil.isEmpty(jxStudent.getHeadPicUrl())
						||StringUtil.isEmpty(jxStudent.getHomeAddress())
						||StringUtil.isEmpty(jxStudent.getIDCard())
						||StringUtil.isEmpty(jxStudent.getName())
						||StringUtil.isEmpty(jxStudent.getNativePlaceCity())
						||StringUtil.isEmpty(jxStudent.getNativePlaceProvince())
						||StringUtil.isEmpty(jxStudent.getFullName1())
						||StringUtil.isEmpty(jxStudent.getRelationship1())
						||StringUtil.isEmpty(jxStudent.getTelephone1())
						||StringUtil.isEmpty(jxStudent.getUnit1())
						||StringUtil.isEmpty(jxStudent.getHealthyCondition())
						||StringUtil.isEmpty(jxStudent.getDomicile())
						||StringUtil.isEmpty(jxStudent.getRelationship1())
						||StringUtil.isEmpty(jxStudent.getFullName1())
						||StringUtil.isEmpty(jxStudent.getUnit1())
						||StringUtil.isEmpty(jxStudent.getTelephone1())
						||StringUtil.isEmpty(jxStudent.getDds())
						||StringUtil.isEmpty(jxStudent.getGzhjfdds())
						||StringUtil.isEmpty(jxStudent.getYfjzzh())
						||StringUtil.isEmpty(jxStudent.getFgzhjxs())
						||StringUtil.isEmpty(jxStudent.getTjb())
						||StringUtil.isEmpty(jxStudent.getYfjzzh())
						||StringUtil.isEmpty(jxStudent.getJszh())
						||StringUtil.isEmpty(jxStudent.getHkb())
						||StringUtil.isEmpty(jxStudent.getJyzm())
						||StringUtil.isEmpty(jxStudent.getFczhhgfxy())
						||jxStudent.getBirthday()==null){
					result.put("msg", "报名信息不完整，请填写完整报名信息后再提交报名");
				}else{	
					jxStudent.setSiteId(siteId);
					jxStudent.setCreateTime(new Date());
					jxStudentService.saveOrUpdate(jxStudent);
					result.put("id", jxStudent.getId());
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
	 * 进入 集贤报名学生报名管理界面
	 * @author lfq
	 * @time 2015-3-17
	 * @return
	 */
	@RequestMapping( value="index")
	public String index(ModelMap modelMap){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("year", dateFormat.format(new Date()));
		return "apply/jixian/index";
	}
	
	/**
	 * 分页查询 集贤报名学生报名信息
	 * @author lfq
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request,HttpServletResponse response,JxStudentSearchVo jxStudentSearchVo){
		jxStudentSearchVo.setCurrentPage(Init.getCurrentPage(jxStudentSearchVo.getCurrentPage()));
		jxStudentSearchVo.setPageSize(Init.getPageSize(jxStudentSearchVo.getPageSize()));
		jxStudentSearchVo.setSiteId(User.getCurrentSiteId(request));
		JSONUtil.printToHTML(response, this.jxStudentService.getJxStudentPageList(jxStudentSearchVo));
	}
	
	/**
	 * 删除 集贤报名学生报名信息
	 * @author lifq
	 * @time 2015-3-17
	 * @param ids  集贤报名学生id,多个用英文逗号隔开
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
					int count= this.jxStudentService.delete(siteId,StringUtil.splitToArray(ids, ","));
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
	 * 进入 集贤报名学生报名详情界面
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param id 	 集贤报名学生id
	 * @return
	 */
	@RequestMapping( value="view")
	public String view(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id){
		modelMap.put("id", id);
		Integer siteId = User.getCurrentSiteId(request);
		try {
			if (siteId>0 && id!=null) {
				JxStudent jxStudent=jxStudentService.getJxStudent(id);
				if (jxStudent!=null && jxStudent.getSiteId().intValue()==siteId) {
					modelMap.put("student", jxStudent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/jixian/view";
	}
	

	/**
	 * 进入打印 集贤报名学生报名详情界面
	 * @author lfq
	 * @time 2015-3-18
	 * @param			jxStudentSearchVo
	 * @param ids		 集贤报名学生id,多个用逗号隔开
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value="toPrint")
	public String toPrint(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,JxStudentSearchVo jxStudentSearchVo,String ids) throws Exception{
		jxStudentSearchVo.setSiteId(User.getCurrentSiteId(request));//后台登录的的siteId
		if (!StringUtil.isEmpty(ids)) {
			jxStudentSearchVo.setIds(StringUtil.splitToArray(ids, ","));
		}
		modelMap.put("jxStudentList", jxStudentService.getJxStudentList(jxStudentSearchVo));
		return "apply/jixian/print";
	}
}
