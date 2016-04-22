package com.base.excel;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Import {

	/**
	 * mapList是从excel文件解析得到的数据，如果正常请返回"succeed"，否则返回错误提示
	 * @param request
	 * @param response
	 * @param mapList
	 * @return
	 */
	String invoke(HttpServletRequest request,HttpServletResponse response, List<Map<String, Object>> mapList);
	
	/**
	 * 整行数据验证
	 * @param request
	 * @param map
	 * @return
	 */
	String checkedMethod(HttpServletRequest request, Map<String, Object> map);

	/**
	 * table.xml文件中字段添加的checkMethod，Import实现类中要类似实现
	 * @param request
	 * @param key 列名；xls文件中原始列名，比如：“姓名”
	 * @param map 一整行数据
	 * @return
	 */
//	String methodName(HttpServletRequest request, String key, LinkedHashMap<String, Object> map);
}
