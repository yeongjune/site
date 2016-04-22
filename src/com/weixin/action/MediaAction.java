package com.weixin.action;

import com.authority.model.User;
import com.base.util.JSONUtil;
import com.base.vo.PageList;
import com.weixin.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/13.
 */
@Controller
@RequestMapping("/media")
public class MediaAction {

    @Autowired
    private MediaService mediaService;

    @RequestMapping("/index")
    public String index(ModelMap modelMap, HttpServletRequest request){
        Integer siteId = User.getCurrentSiteId(request);
        List<Map<String, Object>> typeList = mediaService.getAllType(siteId);
        modelMap.put("typeList", typeList);
        return "weixin/media/index";
    }

    @RequestMapping("/list")
    public void list(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize
            , Integer type, String keyword){
        Integer siteId = User.getCurrentSiteId(request);
        PageList pageList = mediaService.getPageList(siteId, type, keyword);
        JSONUtil.printToHTML(response, pageList);
    }

}
