package com.tejiao.action;

import com.authority.model.User;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.service.SiteService;
import com.tejiao.model.School;
import com.tejiao.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-29.
 */
@Controller
@RequestMapping("/school")
public class SchoolAction {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private SiteService siteService;

    @RequestMapping("edit")
    public String edit(ModelMap modelMap, Integer id){
        if(id != null && id > 0)
            modelMap.put("school", schoolService.get(id));
        return "tejiao/school/edit";
    }

    @RequestMapping("add")
    public void add(HttpServletResponse response, HttpServletRequest request
            , String name, String town, String area, String code){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = User.getCurrentSiteId(request);
        if(StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(town)
                && StringUtil.isNotEmpty(area) && StringUtil.isNotEmpty(code)
                && code.trim().length() == 10 && siteId != null){
            School school = new School();
            school.setSiteId(siteId);
            school.setName(name.trim());
            school.setTown(town.trim());
            school.setArea(area.trim());
            school.setCode(code.trim());
            School s = schoolService.getByCode(code.trim(), siteId);
            if (s == null) {
                schoolService.save(school);
                result.put("code", 0);
            }else{
                result.put("code", -2);
                result.put("msg", "学校代号已存在");
            }
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("update")
    public void update(HttpServletResponse response, HttpServletRequest request, Integer id
            , String name, String town, String area, String code){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = User.getCurrentSiteId(request);
        if(StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(town)
                && StringUtil.isNotEmpty(area) && StringUtil.isNotEmpty(code)
                && code.trim().length() == 10
                && siteId != null && id != null){
            School school = schoolService.get(id);
            school.setName(name.trim());
            school.setTown(town.trim());
            school.setArea(area.trim());
            if (school.getCode().equals(code.trim()) || schoolService.getByCode(code.trim(), siteId) == null) {
                school.setCode(code.trim());
                if(school.getSiteId().equals(siteId))
                    schoolService.update(school);
                result.put("code", 0);
            }else{
                result.put("code", -2);
                result.put("msg", "学校代号已存在");
            }
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("delete")
    public void delete(HttpServletResponse response, HttpServletRequest request, Integer id){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = User.getCurrentSiteId(request);
        if(siteId != null && id != null){
            int count = schoolService.delete(id, siteId);
            result.put("code", 0);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("index")
    public String index(ModelMap modelMap){

        return "tejiao/school/index";
    }

    /**
     * 加载列表
     */
    @RequestMapping("list")
    public void list(HttpServletResponse response, HttpServletRequest request
            , Integer currentPage, Integer pageSize, String keyword){
        Integer siteId = User.getCurrentSiteId(request);
        if(siteId != null){
            PageList pageList = schoolService.getPageList(currentPage, pageSize, siteId, keyword);
            JSONUtil.printToHTML(response, pageList);
        }
    }

    /**
     * 加载所有学校
     */
    @RequestMapping("load")
    public void load(HttpServletResponse response, HttpServletRequest request){
        Integer siteId = siteService.getSiteId(request);
        if(siteId != null){
            List<Map<String, Object>> schoolList = schoolService.getBySiteId(siteId);
            JSONUtil.printToHTML(response, schoolList);
        }
    }

}
