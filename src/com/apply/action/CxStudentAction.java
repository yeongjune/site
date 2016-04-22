package com.apply.action;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.model.CxStudent;
import com.apply.service.CxStudentService;
import com.apply.vo.CxStudentSearchVo;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;

/**
 *  长兴中学报名请求处理控制器
 * @author lfq
 * @time 2015-3-16
 *
 */
@Controller
@RequestMapping(value="cxStudent")
public class CxStudentAction {
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private CxStudentService cxStudentService;

	/**
	 * 保存 长兴中学报名报名信息
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param cxStudent
	 */
	@RequestMapping( value="doRegister" )
	public void doRegister(HttpServletRequest request,HttpServletResponse response,CxStudent cxStudent){
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
				}else if(cxStudent.getId()!=null&&cxStudent.getId()>0){
					result.put("msg", "抱歉，您已提交过报名");
				}else if(StringUtil.isEmpty(cxStudent.getGender())
						||StringUtil.isEmpty(cxStudent.getName())
						||StringUtil.isEmpty(cxStudent.getGraduation())
						||StringUtil.isEmpty(cxStudent.getPosition())
						||StringUtil.isEmpty(cxStudent.getDomicile())
						||StringUtil.isEmpty(cxStudent.getHomeAddress())
						||StringUtil.isEmpty(cxStudent.getHeadPicUrl())
						||StringUtil.isEmpty(cxStudent.getFullName1())
						||StringUtil.isEmpty(cxStudent.getTelephone1())
						||StringUtil.isEmpty(cxStudent.getUnit1())
						||StringUtil.isEmpty(cxStudent.getFullName2())
						||StringUtil.isEmpty(cxStudent.getTelephone2())
						||StringUtil.isEmpty(cxStudent.getUnit2())
						||cxStudent.getBirthday()==null
						||cxStudent.getYuwen1()==null || cxStudent.getYuwen1()<0
						||cxStudent.getShuxue1()==null || cxStudent.getShuxue1()<0
						||cxStudent.getYingyu1()==null || cxStudent.getYingyu1()<0
						||cxStudent.getPaiming1()==null || cxStudent.getPaiming1()<0
						||cxStudent.getYuwen2()==null || cxStudent.getYuwen2()<0
						||cxStudent.getShuxue2()==null || cxStudent.getShuxue2()<0
						||cxStudent.getYingyu2()==null || cxStudent.getYingyu2()<0
						||cxStudent.getPaiming2()==null || cxStudent.getPaiming2()<0
						||cxStudent.getYuwen3()==null || cxStudent.getYuwen3()<0
						||cxStudent.getShuxue3()==null || cxStudent.getShuxue3()<0
						||cxStudent.getYingyu3()==null || cxStudent.getYingyu3()<0
						||cxStudent.getPaiming3()==null || cxStudent.getPaiming3()<0
						||cxStudent.getYuwen4()==null || cxStudent.getYuwen4()<0
						||cxStudent.getShuxue4()==null || cxStudent.getShuxue4()<0
						||cxStudent.getYingyu4()==null || cxStudent.getYingyu4()<0
						||cxStudent.getPaiming4()==null || cxStudent.getPaiming4()<0
						){
					result.put("msg", "报名信息不完整，请填写完整报名信息后再提交报名");
				}else{	
					cxStudent.setSiteId(siteId);
					cxStudent.setCreateTime(new Date());
					cxStudentService.saveOrUpdate(cxStudent);
					result.put("id", cxStudent.getId());
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
	 * 进入 长兴中学报名学生报名管理界面
	 * @author lfq
	 * @time 2015-3-17
	 * @return
	 */
	@RequestMapping( value="index")
	public String index(ModelMap modelMap){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		modelMap.put("year", dateFormat.format(new Date()));
		return "apply/changxin/index";
	}
	
	/**
	 * 分页查询 长兴中学报名学生报名信息
	 * @author lfq
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request,HttpServletResponse response,CxStudentSearchVo cxStudentSearchVo){
		cxStudentSearchVo.setCurrentPage(Init.getCurrentPage(cxStudentSearchVo.getCurrentPage()));
		cxStudentSearchVo.setPageSize(Init.getPageSize(cxStudentSearchVo.getPageSize()));
		cxStudentSearchVo.setSiteId(User.getCurrentSiteId(request));
		JSONUtil.printToHTML(response, this.cxStudentService.getCxStudentPageList(cxStudentSearchVo));
	}
	
	/**
	 * 删除 长兴中学报名学生报名信息
	 * @author lifq
	 * @time 2015-3-17
	 * @param ids  长兴中学报名学生id,多个用英文逗号隔开
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
					int count= this.cxStudentService.delete(siteId,StringUtil.splitToArray(ids, ","));
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
	 * 进入 长兴中学报名学生报名详情界面
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param id 	 长兴中学报名学生id
	 * @return
	 */
	@RequestMapping( value="view")
	public String view(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id){
		modelMap.put("id", id);
		Integer siteId = User.getCurrentSiteId(request);
		try {
			if (siteId>0 && id!=null) {
				CxStudent cxStudent=cxStudentService.getCxStudent(id);
				if (cxStudent!=null && cxStudent.getSiteId().intValue()==siteId) {
					modelMap.put("student", cxStudent);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/changxin/view";
	}
	

	/**
	 * 进入打印 长兴中学报名学生报名详情界面
	 * @author lfq
	 * @time 2015-3-18
	 * @param			cxStudentSearchVo
	 * @param ids		 长兴中学报名学生id,多个用逗号隔开
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( value="toPrint")
	public String toPrint(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,CxStudentSearchVo cxStudentSearchVo,String ids) throws Exception{
		cxStudentSearchVo.setSiteId(User.getCurrentSiteId(request));//后台登录的的siteId
		if (!StringUtil.isEmpty(ids)) {
			cxStudentSearchVo.setIds(StringUtil.splitToArray(ids, ","));
		}
		modelMap.put("cxStudentList", cxStudentService.getCxStudentList(cxStudentSearchVo));
		return "apply/changxin/print";
	}

    @RequestMapping("/toPrintExamRoom")
    public String toPrintExamRoom(HttpServletRequest request, ModelMap map, String year, Integer groupNum){
        if(StringUtil.isEmpty(year)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            year = sdf.format(new Date());
        }
        if(groupNum == null || groupNum < 1){
            groupNum = 36;
        }
        Integer siteId = User.getCurrentSiteId(request);
        CxStudentSearchVo searchVo = new CxStudentSearchVo();
        searchVo.setSiteId(siteId);
        searchVo.setYear(year);
        List<Map<String, Object>> cxStudentList = cxStudentService.getCxStudentList(searchVo);
        List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>(cxStudentList.size() / groupNum + 1);
        int count = 1;
        List<Map<String, Object>> stuList = new ArrayList<Map<String, Object>>();
        list.add(stuList);
        for(Map<String, Object> stu : cxStudentList){
            if(count > groupNum){
                count = 1;
                stuList = new ArrayList<Map<String, Object>>();
                list.add(stuList);
            }
            stuList.add(stu);
            count++;
        }
        map.put("studentListGroup", list);
        return "apply/changxin/printExamRoom";
    }
}
