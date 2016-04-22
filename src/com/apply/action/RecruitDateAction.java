package com.apply.action;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.model.RecruitDate;
import com.apply.service.RecruitDateService;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.model.Data;

@Controller
@RequestMapping(value="recruite")
public class RecruitDateAction {
	@Autowired
	private RecruitDateService recruitDateService;
	/**
	 * 进入报名日期管理界面
	 * @author lifq
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping( value="index" )
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			RecruitDate recruitDate=this.recruitDateService.load(siteId);
			modelMap.put("recruitDate", recruitDate);
		}
		return "apply/recruitDate/index";
	}
	
	
	/**
	 * 保存或修改面试日期时间
	 * @author lifq
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @param printStartDate
	 * @param printEndDate
	 */
	@RequestMapping( value="doSave" )
	public void  doSave(HttpServletRequest request,HttpServletResponse response,
			Integer id,String startDate,String endDate,String printStartDate,String printEndDate){
		Integer siteId = User.getCurrentSiteId(request);
		try {
			if (siteId>0) {
				SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				/*SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM-dd");*/
				
				RecruitDate recruitDate=this.recruitDateService.load(User.getCurrentSiteId(request));
				if (recruitDate==null) {
					recruitDate=new RecruitDate();
					recruitDate.setSiteId(siteId);
				}
				recruitDate.setStartDate(dateFormat1.parse(startDate));
				recruitDate.setEndDate(dateFormat1.parse(endDate));
			/*	recruitDate.setPrintStartDate(dateFormat2.parse(printStartDate));
				recruitDate.setPrintEndDate(dateFormat2.parse(printEndDate));*/
				
				if (recruitDate.getId()==null||recruitDate.getId()==0) {//新增
					 this.recruitDateService.save(recruitDate);
				}else{//修改
					this.recruitDateService.update(recruitDate);
				}
				JSONUtil.print(response,Init.SUCCEED);
			}else{
				JSONUtil.print(response, Init.FALSE);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JSONUtil.print(response, Init.FAIL);
		}
	}

	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Data.class, new CustomDateEditor(dateFormat, true));
	}
}
