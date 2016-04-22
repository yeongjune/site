package com.tejiao.action;

import com.base.util.CryptUtil;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;
import com.tejiao.model.Parent;
import com.tejiao.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
@Controller
@RequestMapping("/parent")
public class ParentAction {

    @Autowired
    private ParentService parentService;

    @Autowired
    private SiteService siteService;

    /**
     * 注册
     * @param account
     * @param password
     * @param email
     */
    //@RequestMapping("enroll")
    @Deprecated
    public void enroll(HttpServletResponse response, HttpServletRequest request, HttpSession session
            , String account, String password, String email, String code){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = siteService.getSiteId(request);
        if(StringUtil.isNotEmpty(account) && StringUtil.isNotEmpty(password)
                && StringUtil.isNotEmpty(email)
                && StringUtil.isNotEmpty(code) && code.trim().length() == 4
                && account.trim().length() >= 6 && password.trim().length() >= 8){
            String validCode = (String) session.getAttribute("validCode");
            if(!validCode.trim().equals(code.trim())){
                result.put("code", -4);
                result.put("msg", "验证码不正确");
                JSONUtil.printToHTML(response, result);
                return;
            }
            Parent parent = parentService.getByAccount(account.trim(), siteId);
            if(parent != null){
                result.put("code", -2);
                result.put("msg", "账号已存在");
                JSONUtil.printToHTML(response, result);
                return;
            }
            parent = new Parent();
            parent.setAccount(account);
            parent.setEmail(email);
            parent.setPassword(CryptUtil.MD5encrypt(password.trim()));
            parent.setSiteId(siteId);
            parentService.save(parent);
            result.put("code", 0);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 登录
     * @param account
     * @param password
     */
    @RequestMapping("login")
    public void login(HttpServletResponse response, HttpServletRequest request, HttpSession session
            , String account, String password, String code){
        Map<String, Object> result = new HashMap<>();
        Integer siteId = siteService.getSiteId(request);
        if(StringUtil.isNotEmpty(account) && StringUtil.isNotEmpty(password)
                && StringUtil.isNotEmpty(code) && code.trim().length() == 4
                && account.trim().length() >= 6 && password.trim().length() >= 8){
            String validCode = (String) session.getAttribute("validCode");
            if(!validCode.trim().equals(code.trim())){
                result.put("code", -4);
                result.put("msg", "验证码不正确");
                JSONUtil.printToHTML(response, result);
                return;
            }
            Parent parent = parentService.getByAccount(account.trim(), siteId);
            if(parent == null){
                result.put("code", -2);
                result.put("msg", "账号不存在");
                JSONUtil.printToHTML(response, result);
                return;
            }
            if(!parent.getPassword().equals(CryptUtil.MD5encrypt(password))){
                result.put("code", -3);
                result.put("msg", "密码错误");
                JSONUtil.printToHTML(response, result);
                return;
            }
            if(parent.getIsStop() == 1){
                result.put("code", -5);
                result.put("msg", "账号已停用");
                JSONUtil.printToHTML(response, result);
                return;
            }
            result.put("code", 0);
            parentService.setCurrent(request.getSession(), parent);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 更新电子邮件和密码
     * @param email
     * @param password
     * @param newPassword
     */
    @RequestMapping("updateEmailAndPassword")
    public void updateEmailAndPassword(HttpServletResponse response, HttpServletRequest request, HttpSession session
            , String email, String password, String newPassword){
        Map<String, Object> result = new HashMap<>();
        Parent parent = parentService.getCurrent(session);
        if(StringUtil.isNotEmpty(email) && StringUtil.isNotEmpty(password)
                && StringUtil.isNotEmpty(newPassword) && parent != null){
            String md5Password = CryptUtil.MD5encrypt(password.trim());
            if(!parent.getPassword().equals(md5Password)){
                result.put("code", -2);
                result.put("msg", "原密码不正确");
                JSONUtil.printToHTML(response, result);
                return;
            }
            parent.setEmail(email.trim());
            parent.setPassword(CryptUtil.MD5encrypt(newPassword.trim()));
            parentService.update(parent);
            result.put("code", 0);
            parentService.setCurrent(session, parent);
        }else{
            result.put("code", -1);
            result.put("msg", "参数错误");
        }
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping("logout")
    public void logout(HttpServletResponse response, HttpSession session, HttpServletRequest request) throws IOException {
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements())
            session.removeAttribute(attributeNames.nextElement());
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            Cookie cookie = new Cookie(c.getName(), "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        response.sendRedirect("/");
    }

}
