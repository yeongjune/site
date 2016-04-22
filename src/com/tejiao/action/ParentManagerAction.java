package com.tejiao.action;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.tejiao.model.Parent;
import com.tejiao.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理学校用户 parent
 * Created by dzf on 16-1-22.
 */
@Controller
@RequestMapping("/parentManager")
public class ParentManagerAction {

    @Autowired
    private ParentService parentService;

    @RequestMapping("index")
    public String index(){

        return "tejiao/parent/index";
    }

    @RequestMapping("save")
    public void save(HttpServletResponse response, HttpServletRequest request
            , String account, String name, String email){
        Integer siteId = User.getCurrentSiteId(request);
        Map<String, Object> result = new HashMap<>();
        result.put("code", -1);
        if(StringUtil.isNotEmpty(account) && account.trim().length() >= 6
                && StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(email)){
            Parent p = parentService.getByAccount(account.trim(), siteId);
            if(p != null){
                result.put("msg", "账号已存在");
                JSONUtil.printToHTML(response, result);
                return;
            }
            Parent parent = new Parent();
            parent.setAccount(account.trim());
            parent.setName(name.trim());
            parent.setEmail(email.trim());
            parent.setPassword(CryptUtil.MD5encrypt("abcd1234"));
            parent.setSiteId(siteId);
            parentService.save(parent);
            result.put("code", 0);
        }else{
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("update")
    public void update(HttpServletResponse response, HttpServletRequest request
            , Integer id, String name, String email){
        Integer siteId = User.getCurrentSiteId(request);
        Map<String, Object> result = new HashMap<>();
        result.put("code", -1);
        if( StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(email)){
            Parent parent = parentService.get(id);
            if(parent == null || !parent.getSiteId().equals(siteId)){
                result.put("msg", "参数错误");
                JSONUtil.printToHTML(response, result);
                return;
            }
            parent.setName(name.trim());
            parent.setEmail(email.trim());
            parentService.update(parent);
            result.put("code", 0);
        }else{
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 加载列表
     */
    @RequestMapping("list")
    public void list(HttpServletResponse response, HttpServletRequest request
            , Integer currentPage, Integer pageSize, String keyword){
        pageSize = Init.getPageSize(pageSize);
        currentPage = Init.getCurrentPage(currentPage);
        Integer siteId = User.getCurrentSiteId(request);
        PageList pageList = parentService.getPageList(currentPage, pageSize, keyword, siteId);
        JSONUtil.printToHTML(response, pageList);
    }

    /**
     * 重置密码
     */
    @RequestMapping("reset")
    public void reset(HttpServletResponse response, HttpServletRequest request, Integer id){
        Integer siteId = User.getCurrentSiteId(request);
        parentService.updatePassword(id, siteId, CryptUtil.MD5encrypt("abcd1234"));
        JSONUtil.print(response, "ok");
    }

    /**
     * 停用
     */
    @RequestMapping("stop")
    public void stop(HttpServletResponse response, HttpServletRequest request, Integer id, Integer state){
        Integer siteId = User.getCurrentSiteId(request);
        Parent parent = parentService.get(id);
        if(parent != null && parent.getSiteId().equals(siteId)){
            parent.setIsStop((state == null || state == 0) ? 0 : 1);
            parentService.update(parent);
            JSONUtil.print(response, "ok");
        }
    }

    @RequestMapping("edit")
    public String edit(ModelMap modelMap,@RequestParam(required = false) Integer id){
        if(id != null && id > 0)
            modelMap.put("parent", parentService.get(id));
        return "tejiao/parent/edit";
    }

}
