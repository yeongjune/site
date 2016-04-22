package com.authority.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.util.CryptUtil;
import com.base.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.authority.service.UserService;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.service.SiteService;

@Controller
@RequestMapping("login")
public class LoginAction {

	@Autowired
	private UserService userService;
	@Autowired
	private SiteService siteService;
	

	@RequestMapping("index")
	public String index(ModelMap map, HttpServletRequest request, HttpServletResponse response){
		Integer sId = siteService.getSiteId(request);
		@SuppressWarnings("unchecked")
		Map<String, Object> user = (Map<String, Object>) request.getSession().getAttribute(Init.AUTHORITY_USER);
		if(user==null || user.isEmpty()){
			int i = 0;
			Cookie cookies[] = request.getCookies();
			// 当cookies 为空 或者只有 jsessionid 的时候 则判定 为不存在
			if(cookies != null && cookies.length > 1){
				Integer siteId = 0;
				String account = "";
				String password = "";
				String loginStatus = "";
				for(Cookie cookie : cookies){
					if(Init.AUTHORITY_USER_LOGIN_STATUS.equals(cookie.getName())){
						loginStatus = cookie.getValue();
						if("0".equals(loginStatus))break;
					}
					if(Init.AUTHORITY_USER_SITE.equals(cookie.getName()))siteId = Integer.parseInt(cookie.getValue());
					if(Init.AUTHORITY_USER_ACCOUNT.equals(cookie.getName())){
						account = cookie.getValue();
						try {
							account = URLDecoder.decode(account, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					if(Init.AUTHORITY_USER_PASSWORD.equals(cookie.getName()))password = cookie.getValue();
				}
				user = userService.getUserByIdAndPassword(siteId, account, password);
				i = User.login(request, user);
			}
			if(i>0){
				return "redirect:/"+Init.SYSTEM_INDEX_URL+".action";
			}else{
				Object login_fail_count = request.getSession().getAttribute("login_fail_count");
				Integer count = (Integer) (login_fail_count==null?0:login_fail_count);
				map.put("fail_count", count);
                String account = request.getParameter("account");
                if(!StringUtil.isEmpty(account)){
                    try {
                        int interval = 1 * 1000;
                        long currentTime = Long.parseLong(request.getParameter("currentTime"));
                        long nowTime = System.currentTimeMillis();
                        if( nowTime - interval < currentTime && currentTime < nowTime + interval){
                            String key = request.getParameter("key");
                            CryptUtil cryptUtil = new CryptUtil();
                            long decrypt = Long.parseLong(cryptUtil.get3DESDecrypt(key, CryptUtil.SPKEY));
                            if(currentTime == decrypt){
                                Map<String, Object> userMap = userService.getUserById(sId, account);
                                if(userMap != null){
                                    User.login(request, userMap);
                                    return "redirect:/"+Init.SYSTEM_INDEX_URL+".action";
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
				return "login";
			}
		}else{
			return "redirect:/"+Init.SYSTEM_INDEX_URL+".action";
		}
	}
	@RequestMapping("login")
	public void login(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		String account = request.getParameter("account");
		int i = 0;
		if(account==null || account.trim().equals("")){
			result.put("status", Init.FAIL);
			result.put("message", "帐号不能为空");
		}else{
			String domain = request.getServerName();
			Integer siteId = siteService.getSiteId(domain);
			siteId = siteId==null?0:siteId;
			Object login_fail_count = request.getSession().getAttribute("login_fail_count");
			Integer count = (Integer) (login_fail_count==null?0:login_fail_count);
			if(count>=5){
				Object validCode = request.getSession().getAttribute("validCode");
				String captcha = request.getParameter("captcha");
				if(captcha!=null && captcha.equals(validCode)){
					
				}else if(validCode == null && captcha == null){
					
				}else{
					result.put("status", Init.FAIL);
					result.put("message", "验证码不正确");
					JSONUtil.printToHTML(response, result);
					return ;
				}
			}
			String password = request.getParameter("password");
			Map<String, Object> user = userService.getUserByIdAndPassword(siteId, account, password);
			String writeCookie = request.getParameter("writeCookie");
			if(writeCookie!=null && writeCookie.equals("1")){
				i = User.login(request, response, user);
			}else{
				i = User.login(request, user);
			}
		}
		responseResult(i, request, result);
		JSONUtil.printToHTML(response, result);
	}
	private void responseResult(int i, HttpServletRequest request, Map<String, Object> result) {
		Object login_fail_count = request.getSession().getAttribute("login_fail_count");
		Integer count = (Integer) (login_fail_count==null?0:login_fail_count);
		if(i>0){
			request.getSession().setAttribute("login_fail_count", 0);
			result.put("status", Init.SUCCEED);
			result.put("message", "");
			result.put("failCount", 0);
		}else{
			request.getSession().setAttribute("login_fail_count", count+1);
			result.put("failCount", count+1);
			result.put("status", Init.FAIL);
			result.put("message", "帐号或密码错误");
		}
	}
	@RequestMapping("logout")
	public void logout(HttpServletRequest request, HttpServletResponse response){
		User.logout(request, response);
		JSONUtil.print(response, "succeed");
	}

}
