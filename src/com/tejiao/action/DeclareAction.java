package com.tejiao.action;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.service.SiteService;
import com.tejiao.model.Declare;
import com.tejiao.model.Parent;
import com.tejiao.model.School;
import com.tejiao.model.UserType;
import com.tejiao.service.DeclareService;
import com.tejiao.service.ParentService;
import com.tejiao.service.SchoolService;
import com.tejiao.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dzf on 15-12-28.
 */
@Controller
@RequestMapping("/declare")
public class DeclareAction {

    @Autowired
    private SiteService siteService;

    @Autowired
    private ParentService parentService;

    @Autowired
    private DeclareService declareService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private SchoolService schoolService;

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    @RequestMapping("index")
    public String index(ModelMap map, HttpServletRequest request){
        Integer userId = User.getCurrentUserId(request);
        Integer siteId = User.getCurrentSiteId(request);
        UserType userType = userTypeService.get(userId, siteId);
        map.put("userType", userType);
        map.put("schoolList", schoolService.getBySiteId(siteId));
        return "tejiao/declare/index";
    }

    @RequestMapping("save")
    public void save(HttpServletResponse response, HttpServletRequest request, HttpSession session, Declare declare){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = siteService.getSiteId(request);
        Parent parent = parentService.getCurrent(session);
        if(declare != null && siteId != null && parent != null){
            String title = Calendar.getInstance().get(Calendar.YEAR) + "年";
            School school = schoolService.get(declare.getSchoolId());
            title += school.getArea() + school.getName();
            String[] grades = {"", "小学一年级", "小学二年级", "小学三年级", "小学四年级", "小学五年级", "", "初中一年级", "初中二年级"};
            title += grades[declare.getGrade()] + declare.getName();

            declare.setTitle(title);
            declare.setSiteId(siteId);
            declare.setParentId(parent.getId());
            declare.setTownCheckState(0);
            declare.setDistrictCheckState(0);
            declare.setCreateTime(new Date());
            declareService.save(declare);
            result.put("code", 0);
        }
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 加载个人申报表
     * @param keyword
     */
    @RequestMapping("personal")
    public void personal(HttpServletResponse response, HttpSession session, HttpServletRequest request
            , Integer currentPage, Integer pageSize, String keyword, Integer status, String date){
        currentPage = Init.getCurrentPage(currentPage);
        pageSize    = Init.getPageSize(pageSize);
        Parent parent = parentService.getCurrent(session);
        Integer siteId = siteService.getSiteId(request);
        if(siteId != null && parent != null){
            PageList pageList = declareService.getPersonalPageList(currentPage, pageSize, parent.getId(), siteId, keyword, status, date);
            JSONUtil.printToHTML(response, pageList);
        }
    }

    @RequestMapping("toWebView")
    public String toWebView(ModelMap modelMap, HttpSession session, Integer id){
        Parent parent = parentService.getCurrent(session);
        modelMap.put("declare", declareService.getById(id, parent.getSiteId()));
        return "tejiao/declare/viewWebDeclare";
    }

    /**
     * 后台用户审核列表
     * @param type 残疾类型
     */
    @RequestMapping("loadCheck")
    public void loadCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session
            , Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer type){
        currentPage = Init.getCurrentPage(currentPage);
        pageSize    = Init.getPageSize(pageSize);
        Integer siteId = User.getCurrentSiteId(request);
        Integer userId = User.getCurrentUserId(request);
        PageList pageList = declareService.getCheckList(currentPage, pageSize, keyword
                , state, gender, schoolId, grade, siteId, userId, type);
        JSONUtil.printToHTML(response, pageList);
    }
    /**进入全部申报列表页面
     * @param map
     * @param request
     * @return
     */
    @RequestMapping("declare")
    public String declare(ModelMap map, HttpServletRequest request){
        Integer userId = User.getCurrentUserId(request);
        Integer siteId = User.getCurrentSiteId(request);
        UserType userType = userTypeService.get(userId, siteId);
        map.put("userType", userType);
        map.put("schoolList", schoolService.getBySiteId(siteId));
        return "tejiao/declare/declareIndex";
    }
    /**
     * 获取全部的申报数据
     * @param type 残疾类型
     */
    @RequestMapping("loadDeclare")
    public void loadDeclare(HttpServletRequest request, HttpServletResponse response, HttpSession session
    		, Integer currentPage, Integer pageSize, String keyword
    		, Integer state, Integer gender, Integer schoolId, Integer grade, Integer type){
    	currentPage = Init.getCurrentPage(currentPage);
    	pageSize    = Init.getPageSize(pageSize);
    	Integer siteId = User.getCurrentSiteId(request);
    	Integer userId = User.getCurrentUserId(request);
    	PageList pageList = declareService.getDeclareList(currentPage, pageSize, keyword, state, gender, schoolId, grade, siteId, type);
    	JSONUtil.printToHTML(response, pageList);
    }

    @RequestMapping("toView")
    public String toView(ModelMap modelMap, HttpServletRequest request, Integer id){
        Integer siteId = User.getCurrentSiteId(request);
        modelMap.put("declare", declareService.getById(id, siteId));
        return "tejiao/declare/view";
    }

    /**
     * 第一步审核
     */
    @RequestMapping("checkOne")
    public void checkOne(HttpServletResponse response, HttpServletRequest request, Integer id, Integer state){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = User.getCurrentSiteId(request);
        Integer userId = User.getCurrentUserId(request);
        if(state != null && (state == 1 || state == 2)
                && userId != null && siteId != null && id != null){
            declareService.updateOneCheck(id, state, siteId);
            result.put("code", 0);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 第二步审核
     */
    @RequestMapping("checkTwo")
    public void checkTwo(HttpServletResponse response, HttpServletRequest request
            , Integer id, Integer state){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = User.getCurrentSiteId(request);
        Integer userId = User.getCurrentUserId(request);
        if(state != null && (state == 1 || state == 2)
                && userId != null && siteId != null && id != null){
            declareService.updateTwoCheck(id, state, siteId);
            result.put("code", 0);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }
    /**
     * 升级年级
     */
    @RequestMapping("upgrade")
    public void upgrade(HttpServletResponse response, HttpServletRequest request){
    	System.out.println("定时触发了");
    	Map<String, Object> result = new HashMap<>();
    	try {
    		Integer siteId = User.getCurrentSiteId(request);
    		declareService.updateGrade(Long.valueOf(siteId));
    		result.put("code", 0);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
			result.put("msg", "升级失败");
		}
    	JSONUtil.printToHTML(response, result);
    }

    /**
     * 提交申请
     * @param id
     * @param type 1 职中面试申请 2 免计算分数申请
     */
    @RequestMapping("apply")
    public void apply(HttpServletResponse response, HttpServletRequest request, HttpSession session
            , Integer id, Integer type){
        Map<String, Object> result = new HashMap<>();
        Parent parent = parentService.getCurrent(session);
        if(id != null){
            Integer siteId = siteService.getSiteId(request);
            declareService.updateApplyState(id, parent.getId(), type, siteId);
            result.put("code", 0);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("toCheckApply")
    public String toCheckApply(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        Integer siteId = User.getCurrentSiteId(request);
        modelMap.put("schoolList", schoolService.getBySiteId(siteId));
        return "tejiao/apply/index";
    }

    @RequestMapping("loadApplyData")
    public void loadApplyData(HttpServletResponse response, HttpServletRequest request, HttpSession session
            , Integer currentPage, Integer pageSize, String keyword, Integer schoolId, Integer gender, Integer type){
        currentPage = Init.getCurrentPage(currentPage);
        pageSize    = Init.getPageSize(pageSize);
        Integer siteId = User.getCurrentSiteId(request);
        PageList pageList = declareService.getCheckApplyList(currentPage, pageSize, keyword, gender, schoolId, siteId, type);
        JSONUtil.printToHTML(response, pageList);
    }

    /**
     * 审核申请
     * @param id 申报id
     * @param state 审核状态
     * @param type 审核类型 1 职中面试申请 2 免计算分数申请
     */
    @RequestMapping("checkApply")
    public void checkApply(HttpServletResponse response, HttpServletRequest request, HttpSession session
            , Integer id, Integer state, Integer type){
        Integer siteId = User.getCurrentSiteId(request);
        Integer userId = User.getCurrentUserId(request);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        if(id != null && state != null && type != null){
            declareService.processCheckApply(id, state, type, userId, siteId);
            result.put("code", 0);
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("delete")
    public void delete(HttpServletResponse response, HttpServletRequest request, HttpSession session, Integer id){
        Integer siteId = User.getCurrentSiteId(request);
        if(siteId == null || siteId == 0) siteId = siteService.getSiteId(request);
        Parent parent = parentService.getCurrent(session);
        Integer userId = User.getCurrentUserId(request);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        Map<String, Object> declare = declareService.getById(id, siteId);
        if(declare != null && (declare.get("grade").equals(6) || declare.get("grade").equals(9))
                && ((userId != null && userId > 0) || parent.getId().equals(declare.get("parentId"))) ){
            declareService.deleteById(id, siteId);
            result.put("code", 0);
        }else{
            result.put("msg", "不符合条件");
        }
        JSONUtil.printToHTML(response, result);
    }
}
