package com.base.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Init {

	private static LinkedHashMap<String, Object> MAP = new LinkedHashMap<String, Object>();
	private static final String rootPath = Init.class.getResource("").getPath();
	private static String xmlFileName = "config.xml";

	static{
		loadKeyValue();
	}

	public static void reload(ServletContext context){
		loadKeyValue();
		if(!MAP.isEmpty()){
			for (String key : MAP.keySet()) {
				context.setAttribute(key, get(key).get("value"));
			}
		}
	}
	/**
	 * 读取xml文件，初始化数据
	 * @param elementName
	 * @return
	 */
	public static void loadKeyValue(){
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
			is = new FileInputStream(rootPath+xmlFileName);
			Document doc = reader.read(is);
			Element root = doc.getRootElement();
			MAP = parseElement(root);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static LinkedHashMap<String, Object> parseElement(Element element){
		if(element==null)return null;
		List<Element> children = element.elements();
		if(children==null || children.size()<=0){
			List list = element.attributes();
			if(list==null || list.size()<=0)return null;
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			for (int i = 0; i < list.size(); i++) {
				Attribute attr = (Attribute) list.get(i);
				if(attr!=null)map.put(attr.getName(), attr.getValue());
			}
			return map;
		}else{
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			for (int i = 0; i < children.size(); i++) {
				Element e = children.get(i);
				if(e==null)continue;
				Attribute attr = e.attribute("name");
				if(attr==null || attr.getValue()==null || attr.getValue().trim().equals("")){
					map.putAll(parseElement(e));
				}else{
					map.put(attr.getValue(), parseElement(e));
				}
			}
			return map;
		}
	}

	public static LinkedHashMap<String, Object> get(String...key){
		return get(MAP, key, 0);
	}
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> get(LinkedHashMap<String, Object> map, String[] key, int cursor){
		if(map==null || map.isEmpty() || key==null || key.length<=0 || cursor>=key.length)return map;
		Object obj = map.get(key[cursor]);
		if(obj instanceof LinkedHashMap){
			LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) obj;
			return get(tmp, key, cursor+1);
		}else{
			return (LinkedHashMap<String, Object>) map.clone();
		}
	}

	/**
	 * 操作失败的标志
	 */
	public static final String FAIL="fail";
	/**
	 * 操作成功的标志
	 */
	public static final String SUCCEED="succeed";

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 后台用户登录保存的session key
	 * 通过该session可以拿到后台管理的登录用户信息id,name,account等
	 */
	public static final String AUTHORITY_USER = "authority_user";
	public static final String AUTHORITY_USER_SITE = "authority_user_site";
	public static final String AUTHORITY_USER_ACCOUNT = "authority_user_accout";
	public static final String AUTHORITY_USER_PASSWORD = "authority_user_password";
	public static final String AUTHORITY_USER_LOGIN_STATUS = "authority_user_login_status";
	public static final String SYSTEM_INDEX_URL = "sys/index";
	public static final String APPLICATION_URL_KEY = "application_url_key";
	/**
	 * 报名系统的登录session key
	 * 通过该session可以拿到报名系统的登录用户信息id,name,acount等
	 */
	public final static String APPLY_USER="APPLY_USER";
	
	/**
	 *  民航小学报名系统使用
	 */
	public final static String APPLY_USER2="APPLY_USER2";
	/**
	 * 站点前台的登录用户保存 的session key
	 * 通过该session可以拿到网站前台的登录用户信息id,name,acount等
	 */
	public static final String SITE_USER="siteUser";
	/**
	 * 访问各个网站时保存的网站site信息session key
	 * 通过该session可以拿到站点的id,name,directory等
	 */
	public static final String SITE="site";
	
	public static Integer getCurrentPage(Integer currentPage){
		return currentPage==null||currentPage<1?1:currentPage;
	}
	public static Integer getPageSize(Integer pageSize){
		Integer defaultPageSize = 10;
		Object obj = Init.get("pageSize").get("value");
		if(obj!=null && !obj.toString().trim().equals("")){
			defaultPageSize = Integer.parseInt(obj.toString().trim());
		}
		return (pageSize==null||pageSize<1?defaultPageSize:pageSize);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getByMapList(String key){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> pageSizesMap = Init.get(key);
		Set<String> tempMapKey = pageSizesMap.keySet();
		Iterator<String> keyIterator = tempMapKey.iterator();
		while(keyIterator.hasNext()){
			result.add((Map<String, Object>) pageSizesMap.get(keyIterator.next()));
		}
		return result;
	}
}
