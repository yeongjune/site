package com.lottery.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.authority.service.LoginService;
import com.authority.service.UserService;
import com.base.config.Init;
import com.base.util.JSONUtil;

@Controller
@RequestMapping("lottery/login")
public class LotteryLoginAction {

	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("index")
	public String index(){
		return "lottery/index";
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
//			Integer siteId = siteService.getSiteId(domain);
			Integer siteId = 0;
			Object login_fail_count = request.getSession().getAttribute("login_fail_count");
			Integer count = (Integer) (login_fail_count==null?0:login_fail_count);
			if(count>=5){
				Object validCode = request.getSession().getAttribute("validCode");
				String captcha = request.getParameter("captcha");
				if(captcha!=null && captcha.equals(validCode)){
					
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
}
