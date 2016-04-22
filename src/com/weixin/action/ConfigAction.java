package com.weixin.action;

import com.authority.model.User;
import com.weixin.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by dzf on 2015/11/13.
 */
@Controller
@RequestMapping("/config")
public class ConfigAction {

    @Autowired
    private ConfigService configService;

    @RequestMapping("/toSetConfig")
    public String toSetSiteWeiXInConfig(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
        Integer siteId= User.getCurrentSiteId(request);
        if (siteId!=null&&siteId>0) {
            Map<String, Object> config = configService.getBySiteId(siteId);
            modelMap.put("config", config);
        }
        return "weixin/config/set";
    }

    @RequestMapping("/update")
    public void update(HttpServletRequest request, HttpServletResponse response, String appID, String appsecret){
        Integer siteId= User.getCurrentSiteId(request);
        configService.update(siteId, appID, appsecret);
    }

}
