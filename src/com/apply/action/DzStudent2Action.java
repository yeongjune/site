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

import com.apply.model.DzStudent2;
import com.apply.service.DzStudent2Service;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;

/**
 * 民航子弟小学报名请求处理控制器
 * @author lfq
 * @time 2015-3-16
 *
 */
@Controller
@RequestMapping(value="dzStudent2")
public class DzStudent2Action {
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private DzStudent2Service dzStudent2Service;

	/**
	 * 保存民航子弟小学报名信息
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param dzStudent2
	 */
	@RequestMapping( value="doRegister" )
	public void doRegister(HttpServletRequest request,HttpServletResponse response,DzStudent2 dzStudent2){
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
				}else if(dzStudent2.getId()!=null&&dzStudent2.getId()>0){
					result.put("msg", "抱歉，您已提交过报名");
				}else if(StringUtil.isEmpty(dzStudent2.getGender())
						||StringUtil.isEmpty(dzStudent2.getName())
						||StringUtil.isEmpty(dzStudent2.getIDCard())
						||StringUtil.isEmpty(dzStudent2.getGraduate())
						||StringUtil.isEmpty(dzStudent2.getNationality())
						||StringUtil.isEmpty(dzStudent2.getHealthyCondition())
						||StringUtil.isEmpty(dzStudent2.getHeadPicUrl())
						||StringUtil.isEmpty(dzStudent2.getHomeAddress())
						||StringUtil.isEmpty(dzStudent2.getFullName1())
						||StringUtil.isEmpty(dzStudent2.getTelephone1())
						||StringUtil.isEmpty(dzStudent2.getUnit1())
						||StringUtil.isEmpty(dzStudent2.getFullName2())
						||StringUtil.isEmpty(dzStudent2.getTelephone2())
						||StringUtil.isEmpty(dzStudent2.getUnit2())
						||StringUtil.isEmpty(dzStudent2.getDomiciProvince())
						||StringUtil.isEmpty(dzStudent2.getDomicilCity())
						||dzStudent2.getIsPeasant()==null
						||dzStudent2.getBirthday()==null){
					result.put("msg", "报名信息不完整，请填写完整报名信息后再提交报名");
				}else{	
					dzStudent2.setSiteId(siteId);
					dzStudent2.setCreateTime(new Date());
					dzStudent2Service.saveOrUpdate(dzStudent2);
					result.put("id", dzStudent2.getId());
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
	 * 进入民航子弟小学学生报名管理界面
	 * @author lfq
	 * @time 2015-3-17
	 * @return
	 */
	@RequestMapping( value="index")
	public String index(ModelMap modelMap){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("year", dateFormat.format(new Date()));
		return "apply/minghangdizi/index2";
	}
	
	/**
	 * 分页查询民航子弟小学学生报名信息
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
		JSONUtil.printToHTML(response, this.dzStudent2Service.getDzStudent2PageList(siteId, pageSize, currentPage, keyword,year));
	}
	
	/**
	 * 删除民航子弟小学学生报名信息
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
					int count= this.dzStudent2Service.delete(siteId,StringUtil.splitToArray(ids, ","));
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
	 * 进入民航子弟小学学生报名详情界面
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
				DzStudent2 dzStudent2=dzStudent2Service.getDzStudent2(id);
				if (dzStudent2!=null && dzStudent2.getSiteId().intValue()==siteId) {
					modelMap.put("student", dzStudent2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/minghangdizi/view2";
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
	@RequestMapping( value="toPrint")
	public String toPrint(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String year,String keyword,String ids) throws Exception{
		Integer siteId = User.getCurrentSiteId(request);//后台登录的的siteId
		modelMap.put("dzStudent2List", dzStudent2Service.getDzStudent2List(siteId, year, keyword, StringUtil.splitToArray(ids, ",")));
		return "apply/minghangdizi/print2";
	}
}
