package com.authority.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.config.Init;

@Entity
@Table(name="authority_user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	static{
		Table t = User.class.getAnnotation(Table.class);
		tableName = t.name();
	}

	@Id
	@GeneratedValue
	private Integer id;
	private String account;
	private String password;
	private String name;
	private Integer siteId;
	private Integer enable;
	private Date createTime;
	private Date updateTime;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCurrentUser(HttpServletRequest request){
		return (Map<String, Object>) request.getSession().getAttribute(Init.AUTHORITY_USER);
	}
	public static Integer getCurrentUserId(HttpServletRequest request){
		Map<String, Object> user = getCurrentUser(request);
		if(user==null || user.isEmpty())return null;
		return (Integer) user.get("id");
	}
	public static Integer getCurrentSiteId(HttpServletRequest request){
		Map<String, Object> user = getCurrentUser(request);
		if(user==null || user.isEmpty())return null;
		return (Integer) user.get("siteId");
	}
	public static String getCurrentWaterMark(HttpServletRequest request) {
		Map<String, Object> user = getCurrentUser(request);
		if(user==null || user.isEmpty())return null;
		return (String) user.get("smallPicOriginalUrl");
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 */
	public static void logout(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().removeAttribute(Init.AUTHORITY_USER);
		Cookie site = new Cookie(Init.AUTHORITY_USER_SITE, "");
		Cookie account = new Cookie(Init.AUTHORITY_USER_ACCOUNT, "");
		Cookie password= new Cookie(Init.AUTHORITY_USER_PASSWORD, "");
		Cookie loginStatus= new Cookie(Init.AUTHORITY_USER_LOGIN_STATUS, "0");
		List<Cookie> cookieList = new ArrayList<Cookie>();
		
		cookieList.add(loginStatus);
		cookieList.add(site);
		cookieList.add(account);
		cookieList.add(password);
		
		for(Cookie cookie : cookieList){
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}
	/**
	 * 记住密码登录
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	public static int login(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> user) {
		if(user==null || user.isEmpty())return 0;
		request.getSession().setAttribute(Init.AUTHORITY_USER, user);
		Cookie site = new Cookie(Init.AUTHORITY_USER_SITE, ""+user.get("siteId"));
		String accountString = (String) user.get("account");
		try {
			accountString = URLEncoder.encode(accountString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie account = new Cookie(Init.AUTHORITY_USER_ACCOUNT, accountString);
		Cookie password= new Cookie(Init.AUTHORITY_USER_PASSWORD, (String) user.get("password"));
		Cookie loginStatus= new Cookie(Init.AUTHORITY_USER_LOGIN_STATUS, "1");
		List<Cookie> cookieList = new ArrayList<Cookie>();
		
		cookieList.add(loginStatus);
		cookieList.add(site);
		cookieList.add(account);
		cookieList.add(password);
		
		for(Cookie cookie : cookieList){
			cookie.setMaxAge(60 * 60 * 24 * 15 );
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return 1;
	}
	/**
	 * 普通登录
	 * @param request
	 * @param user
	 * @return
	 */
	public static int login(HttpServletRequest request,
			Map<String, Object> user) {
		if(user==null || user.isEmpty())return 0;
		request.getSession().setAttribute(Init.AUTHORITY_USER, user);
		return 1;
	}
	public static Map<String, Object> cookie(HttpServletRequest request) {
		Map<String, Object> user = new HashMap<String, Object>();
		Cookie cookies[] = request.getCookies();
		// 当cookies 为空 或者只有 jsessionid 的时候 则判定 为不存在
		if(cookies != null && cookies.length > 1){
			Integer organizationId = 0;
			String account = "";
			String password = "";
			String loginStatus = "";
			for(Cookie cookie : cookies){
				if(Init.AUTHORITY_USER_LOGIN_STATUS.equals(cookie.getName())){
					loginStatus = cookie.getValue();
					if("0".equals(loginStatus))break;
				}
				if(Init.AUTHORITY_USER_SITE.equals(cookie.getName()))organizationId = Integer.parseInt(cookie.getValue());
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
			if(loginStatus!=null && !loginStatus.equals("0")){
				user.put("organizationId", organizationId);
				user.put("account", account);
				user.put("password", password);
			}
		}
		return user;
	}
}
