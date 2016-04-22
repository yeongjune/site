package com.tejiao.action;

import com.authority.model.User;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.tejiao.model.UserType;
import com.tejiao.service.SchoolService;
import com.tejiao.service.UserTypeService;
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
@RequestMapping("/userType")
public class UserTypeAction {

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private SchoolService schoolService;

    @RequestMapping("index")
    public String index(ModelMap modelMap, HttpServletRequest request){
        modelMap.put("siteId", User.getCurrentSiteId(request));
        return "tejiao/userType/index";
    }

    @RequestMapping("edit")
    public String edit(ModelMap modelMap, HttpServletRequest request, Integer userId){
        Integer siteId = User.getCurrentSiteId(request);
        UserType userType = userTypeService.get(userId, siteId);
        modelMap.put("userType", userType);
        modelMap.put("townList", schoolService.getAllTown(siteId));
        modelMap.put("userId", userId);
        return "tejiao/userType/edit";
    }

    @RequestMapping("bindType")
    public void bindType(HttpServletResponse response, HttpServletRequest request
            , Integer userId, Integer isAdmin, String town){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = User.getCurrentSiteId(request);
        if(userId != null && siteId != null
                && isAdmin != null && (isAdmin == 0 || isAdmin == 1)){
            UserType userType = new UserType();
            userType.setTown(town);
            userType.setSiteId(siteId);
            userType.setIsAdmin(isAdmin);
            userType.setUserId(userId);
            userTypeService.save(userType);
            result.put("code", 0);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

}
