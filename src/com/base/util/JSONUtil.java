package com.base.util;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtil{
	
	public final static String SUCCESS = "success";
	
	public final static String ERROR = "error";
	
	/**
	 * 批量更改json 
	 * @param jsonString	需要更改的json格式字符串
	 * @param fieldMap		需要更改的json key 和value
	 * @return
	 */
	public static String updateJSONField(String jsonString, Map<String, Object> fieldMap){
		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		for(String keySet : fieldMap.keySet()){
			Object objValue = fieldMap.get(keySet);
			jsonObj.put(keySet, objValue);
		}
		if(jsonObj == null){
			return "";
		}else{
			return jsonObj.toString();
		}
			
	}
	
	/**
	 * 查询json字符串里的某一个 值
	 * @param jsonString	json格式字符串
	 * @param key			需要查询json的key
	 * @return
	 */
	public static String getJSONField(String jsonString, String key){
		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		if(jsonObj.get(key) != null){
			return jsonObj.get(key).toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 将json字符转换回Map
	 * @param jsonString	json格式字符串
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getJSONField(String jsonString){
		JSONObject jsonObj = JSONObject.fromObject(jsonString);
		return jsonObj;
	}
	


	/**
	 * 通过HttpServletResponse发送出具体一个集合,并且转换成JSONArray形式内容;
	 * 
	 * @param response
	 * @param coll
	 */
	public static void printToHTML(HttpServletResponse response, Object obj) {
		PrintWriter pw = null;
		response.setContentType("text/html;charset=UTF-8");
		try {
			pw = response.getWriter();
			pw.print(JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm", SerializerFeature.DisableCircularReferenceDetect));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (pw != null)
				pw.close();
		}
	}
	/**
	 * 通过HttpServletResponse发送出具体一个集合,并且转换成JSONArray形式内容， 时间格式：yyyy-MM-dd HH:mm:ss;
	 * 
	 * @param response
	 * @param coll
	 */
	public static void printDateTimeHTML(HttpServletResponse response, Object obj) {
		PrintWriter pw = null;
		response.setContentType("text/html;charset=UTF-8");
		try {
			pw = response.getWriter();
			pw.print(JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (pw != null)
				pw.close();
		}
	}
	
	/**
	 * jsonp 方式输出
	 * 
	 * @param response
	 * @param obj
	 * @param callBack
	 */
	public static void printToJSONP(HttpServletResponse response, Object obj, String callBack) {
		PrintWriter pw = null;
		response.setContentType("application/json;charset=UTF-8");
		try {
			pw = response.getWriter();
			pw.print(callBack + "("+ JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect)+ ");");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (pw != null)
				pw.close();
		}
	}
	
	/**
	 * json 输出 字符串
	 * @param response
	 * @param str
	 */
	public static void print(HttpServletResponse response, Object str){
		PrintWriter pw = null;
		response.setContentType("text/html;charset=UTF-8");
		try {
			pw = response.getWriter();
			pw.print(str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (pw != null)
				pw.close();
		}
	}
	


	/**
	 * 加载手机端传过来的json数据
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadPhaoneJSON(HttpServletResponse response, HttpServletRequest request){
		StringBuilder sb=new StringBuilder();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			InputStreamReader reader = new InputStreamReader(request.getInputStream(), "UTF-8");
			char[] buff = new char[1024];
			int length = 0;
			while ((length = reader.read(buff)) != -1) {
				String str = new String(buff, 0, length);
				sb.append(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(sb.toString().length() > 0){
			map = JSONObject.fromObject(sb.toString());
		}
		return map;
	}
	
	/**
	 * 通过HttpServletResponse发送出具体一个集合,并且转换成JSONArray形式内容， 时间格式自定义
	 * @author lfq
	 * @param response
	 * @param obj
	 * @param datePattern 时间格式
	 */
	public static void printDatePatternHTML(HttpServletResponse response, Object obj,String datePattern) {
		PrintWriter pw = null;
		response.setContentType("text/html;charset=UTF-8");
		try {
			pw = response.getWriter();
			pw.print(JSON.toJSONStringWithDateFormat(obj, datePattern, SerializerFeature.DisableCircularReferenceDetect));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (pw != null)
				pw.close();
		}
	}
}
