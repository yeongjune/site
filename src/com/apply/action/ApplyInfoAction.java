package com.apply.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.apply.service.ApplyInfoService;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;

/**
 * 报名信息管理控制器
 * @author lfq
 * @time 2015-3-23
 *
 */
@Controller
@RequestMapping("applyInfo")
public class ApplyInfoAction {
	@Autowired
	private SiteService siteService;

	@Autowired
	private ApplyInfoService applyInfoService;
	
   @InitBinder  
   public void initBinder(WebDataBinder binder) {  
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
       dateFormat.setLenient(false);  
       binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));  
   }  
   
   /**
	 * 获取站点报名信息
	 * @author lfq
	 */
	@RequestMapping("getApplyInfo")
	public void getApplyInfoListBySite(HttpServletRequest request,HttpServletResponse response,String applyNo){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		List<ApplyInfo> applyInfoList=  applyInfoService.getApplyInfo(siteService.getSiteId(request), applyNo);
		if (applyInfoList!=null && applyInfoList.size()>0) {
			result.put("code", Init.SUCCEED);
			ApplyInfo applyInfo=applyInfoList.get(0);
			
			Calendar now=Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			
			Calendar start=Calendar.getInstance();
			start.setTime(applyInfo.getStartTime());
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			
			Calendar end=Calendar.getInstance();
			end.setTime(applyInfo.getEndTime());
			end.set(Calendar.HOUR_OF_DAY, 24);
			end.set(Calendar.MINUTE, 0);
			end.set(Calendar.SECOND, -1);
			
			if (start.getTime().compareTo(now.getTime())<=0 &&end.getTime().compareTo(now.getTime()) >=0 ) {
				result.put("hasApply", 1);//在报名日期内
			}else{
				result.put("hasApply", 0);//不在报名日期内
			}
			result.put("applyInfo", applyInfo);
		}
		JSONUtil.printDatePatternHTML(response, result, "yyyy年MM月dd日");
	}
   
   /**
	 * 进入报名管理首页
	 * @author lfq
	 * @time 2015-3-23
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("toIndex")
	public  String toIndex(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		return "apply/applyInfo/index";
	}
	
	/**
	 * 设置报名
	 * 有两个报名设置时请使用： com.apply.action.ApplyInfoAction.setApplyInfo2
	 * @author lfq
	 * @time 2015-3-23
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("setApplyInfo")
	public  String setApplyInfo(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId=User.getCurrentSiteId(request);
		String applyNo="01";
		if (siteId!=null && siteId>0) {
			List<ApplyInfo> list=applyInfoService.getApplyInfo(siteId, applyNo);
			if(list!=null && list.size()>0){
				modelMap.put("applyInfo", list.get(0));
			}
		}
		modelMap.put("applyNo", applyNo);
		String menu_name = request.getParameter("menu_name"); 
		if (!StringUtil.isEmpty(menu_name)) {
			modelMap.put("menu_name", menu_name);
		}
		return "apply/applyInfo/setApplyInfo";
	}
	
	/**
	 * 设置报名2,有两个报名设置时第二个使用这个
	 * @author lfq
	 * @time 2015-3-23
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("setApplyInfo2")
	public  String setApplyInfo2(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId=User.getCurrentSiteId(request);
		String applyNo="02";
		if (siteId!=null && siteId>0) {
			List<ApplyInfo> list=applyInfoService.getApplyInfo(siteId, applyNo);
			if(list!=null && list.size()>0){
				modelMap.put("applyInfo", list.get(0));
			}
		}
		modelMap.put("applyNo", applyNo);
		String menu_name = request.getParameter("menu_name"); 
		if (!StringUtil.isEmpty(menu_name)) {
			modelMap.put("menu_name", menu_name);
		}
		return "apply/applyInfo/setApplyInfo";
	}
	
	
	/**
	 * 进入编辑报名信息界面
	 * @author lfq
	 * @time 2015-3-23
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param id	报名信息id,为null时表示为新增
	 * @return
	 */
	@RequestMapping("toEdit")
	public  String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null && siteId>0 && id!=null && id >0 ) {
			ApplyInfo applyInfo=applyInfoService.get(id);
			if(applyInfo.getSiteId()==siteId.intValue()){
				modelMap.put("applyInfo", applyInfo);
			}
		}
		return "apply/applyInfo/edit";
	}
	
	/**
	 * 新增或修改报名信息
	 * @author lfq
	 * @time 2015-3-23
	 * @param request
	 * @param response
	 * @param applyInfo
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request,HttpServletResponse response,ApplyInfo applyInfo){
		Integer siteId=User.getCurrentSiteId(request);
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		try {
			if (siteId<1) {
				result.put("msg", "操作失败，您未进入任何站点");
			}else if (StringUtil.isEmpty(applyInfo.getApplyNo())
				||StringUtil.isEmpty(applyInfo.getTitle())
				||applyInfo.getStartTime()==null
				||applyInfo.getEndTime()==null
				||applyInfo.getState()==null
				||(applyInfo.getState()!=0 && applyInfo.getState()!=1)
				){
				result.put("msg", "操作失败，提交信息不完整");
			}else{
				List<ApplyInfo> list=applyInfoService.getApplyInfo(siteId, applyInfo.getApplyNo());
				if(applyInfo.getId()!=null){
					if (siteId.intValue()!=applyInfo.getSiteId()) {
						result.put("msg", "操作信息有误，请刷新界面后重试");
					}else if (list!=null && list.size()>0 && list.get(0).getId().intValue()!=applyInfo.getId()) {
						result.put("msg", "操作失败，已存在该编号的报名信息，请使用其他报名编号");
					}else{
						applyInfoService.saveOrUpdate(applyInfo);
						result.put("code", Init.SUCCEED);
						result.put("msg", "成功修改报名信息设置");
					}
				}else{
					applyInfo.setSiteId(siteId);
					if (list!=null && list.size()>0) {
						result.put("msg", "操作失败，已存在该编号的报名信息，请使用其他报名编号");
					}else{
						applyInfoService.saveOrUpdate(applyInfo);
						result.put("code", Init.SUCCEED);
						result.put("msg", "成功新增报名信息设置");
						result.put("id", applyInfo.getId());
						result.put("siteId", applyInfo.getSiteId());
					}
				}
			}
		} catch (Exception e) {
			result.put("msg", "操作失败，系统异常,请刷新界面后重试");
		}
		JSONUtil.printToHTML(response, result);
		
	}
	
	/**
	 * 删除报名
	 * @author lifq
	 * @time 2015-3-17
	 * @param ids报名id,多个用英文逗号隔开
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
					int count= this.applyInfoService.delete(siteId,StringUtil.splitToArray(ids, ","));
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
	 * 获取站点报名列表
	 * @author lfq
	 * @time 2015-3-23
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("list")
	public  void list(HttpServletRequest request,HttpServletResponse response){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId>0) {
			List<ApplyInfo> applyInfoList=applyInfoService.getApplyInfo(siteId, null);
			JSONUtil.printDatePatternHTML(response, applyInfoList, "yyyy-MM-dd");
		}
	}
	
	/**
	 * 更新报名状态
	 * @author lifq
	 * @time 2015-3-17
	 * @param ids报名id
	 * @param state 更新后的状态：0未开启，1开启
	 */
	@RequestMapping( value="updateState" )
	public void updateState(HttpServletRequest request,HttpServletResponse response,Integer id,Integer state){
		Integer siteId = User.getCurrentSiteId(request);
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "操作失败");
		try {
			if (siteId>0) {
				ApplyInfo applyInfo=applyInfoService.get(id);
				if (applyInfo!=null && applyInfo.getIsDelete()!=1 && applyInfo.getSiteId()==siteId.intValue()) {
					applyInfo.setState(state);
					applyInfoService.saveOrUpdate(applyInfo);
					result.put("code", Init.SUCCEED);
					result.put("msg", "成功"+(state==0?"停止":"开启")+"["+applyInfo.getTitle()+"]报名");
				}else{
					result.put("msg", "操作有误，请刷新界面后重试");
				}
			}else{
				result.put("msg", "未进入站点管理");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "操作异常，请刷新界面后重试");
		}
		JSONUtil.printToHTML(response, result);
	}
}
