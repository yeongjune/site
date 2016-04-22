package com.base.excel;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.base.util.ContextAware;

public class ExcelImport {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String process(HttpServletRequest request, HttpServletResponse response) throws Exception{
		updateImportProgress(request, "导入开始");
		String workbook = request.getParameter("workbook");
		String sheet = request.getParameter("sheet");
		sheet = sheet==null||sheet.trim().equals("")?"Sheet1":sheet;
		// spring 上传获取文件流
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		
		//这里是表单的名字，在swfupload.js中this.ensureDefault("file_post_name", "excelImportForm");
		CommonsMultipartFile uploadFile = (CommonsMultipartFile) multipartRequest.getFile("file");
		
		Map<String, Object> classMap = DataConvert.get(workbook, sheet, "import");
		if(classMap!=null && !classMap.isEmpty()){
			InputStream is =uploadFile.getInputStream();
			if(is==null){
				updateImportProgress(request, "导入结束");
				return "选择文件再导入";
			}else{
				ExcelRead read;
				try {
					read = new ExcelRead(is);
				} catch (Exception e) {
					updateImportProgress(request, "导入结束");
					return e.getMessage();
				}
				updateImportProgress(request, "开始读取表格");
				List<LinkedHashMap<String, Object>> mapList = read.firstSheetData(0, 1);
				updateImportProgress(request, "表格读取完成");
				
				String className = (String) classMap.get("class");
				Class c = ClassUtil.getClass(className);
				
				Method cm = c.getMethod("checkedMethod", ClassUtil.getClass("javax.servlet.http.HttpServletRequest"), Map.class);
				if(mapList!=null){
					updateImportProgress(request, "开始数据验证");
					List<String> uniqueColumns = DataConvert.getUnique(workbook, sheet);
					Map<String, Map<Object, Integer>> valueCounts = null;
					if(uniqueColumns!=null && uniqueColumns.size()>0){
						valueCounts = new HashMap<String, Map<Object,Integer>>();
						for (String columnName : uniqueColumns) {
							if(columnName==null)continue;
							valueCounts.put(columnName, new HashMap<Object, Integer>());
						}
					}
					for (int i = 0; i < mapList.size(); i++) {
						LinkedHashMap<String, Object> linkedHashMap = mapList.get(i);
						if(valueCounts!=null)
						for (String string : linkedHashMap.keySet()) {
							if(valueCounts.containsKey(string)){
								Object obj = linkedHashMap.get(string);
								Map<Object, Integer> tmp = valueCounts.get(string);
								if(tmp.containsKey(obj)){
									return (i+2)+" 行与 "+tmp.get(obj)+" 行的"+string+"：“"+obj+"”重复，请改正再导入";
								}else{
									tmp.put(obj, i+2);
								}
							}
						}
						Object checkResult = cm.invoke(ContextAware.getService(c), request, linkedHashMap);
						updateImportProgress(request, "数据验证："+DataConvert.formatPecent(i,mapList.size()));
						if(checkResult==null || !checkResult.toString().equals("succeed"))return "第（"+(i+2)+"）行 "+checkResult;
					}
					updateImportProgress(request, "数据验证完成");
				}
				
				updateImportProgress(request, "开始数据格式验证及类型转换");
				String checkInfo = changeData(request, mapList, workbook, sheet, c);
				updateImportProgress(request, "数据格式验证及类型转换完成");
				if(checkInfo.equals("succeed")){
					updateImportProgress(request, "开始保存数据");
					Method m = c.getMethod("invoke", ClassUtil.getClass("javax.servlet.http.HttpServletRequest"), ClassUtil.getClass("javax.servlet.http.HttpServletResponse"), List.class);
					Object obj = m.invoke(ContextAware.getService(c), request, response, mapList);
					updateImportProgress(request, "数据完成");
					updateImportProgress(request, "导入结束");
					return ""+obj;
				}else{
					updateImportProgress(request, "导入结束");
					return checkInfo;
				}
			}
		}
		updateImportProgress(request, "导入结束");
		return "没有数据可以导入";
	}

	/**
	 * 更新上传进度信息
	 * @param request
	 * @param string
	 */
	public static void updateImportProgress(HttpServletRequest request,
			String message) {
		HttpSession session = request.getSession();
		session.setAttribute("importProgress", message);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String changeData(HttpServletRequest request,
			List<LinkedHashMap<String, Object>> mapList, String workbook,
			String sheet, Class c) {
		if(mapList==null || mapList.isEmpty())return "读取文件数据为空，导入失败";
		LinkedHashMap<String, Object> map = DataConvert.get(workbook, sheet, "columns");
		if(map==null || map.isEmpty())return "table.xml中没有"+workbook+"的相关配置";
		LinkedHashMap<String, String> columnType = DataConvert.getColumnType(workbook, sheet);
		StringBuffer buffer = new StringBuffer();
		//StringBuffer err = new StringBuffer("");
		buffer.append("导入数据异常：");
		String excelRowName = "";
		String excelColumnName = "";
		String message = "";
		try {
			Map<String, String> methods = DataConvert.getCheckMethod(workbook, sheet);
			//正则
			Map<String, Map<String, String>> mapMap = DataConvert.getRegMap(workbook, sheet);
			Map<String, Map<String, Object>> patternMap = new HashMap<String, Map<String,Object>>();
			if(mapMap!=null && !mapMap.isEmpty()){
				for (String key : mapMap.keySet()) {
					Map<String, String> regMap = mapMap.get(key);
					if(regMap!=null && !regMap.isEmpty()){
						Map<String, Object> pMap = new HashMap<String, Object>();
						pMap.put("pattern", Pattern.compile(regMap.get("reg")));
						pMap.put("regTip", regMap.get("regTip"));
						patternMap.put(key, pMap);
					}
				}
			}
			for (int i = 0; i < mapList.size(); i++) {
				excelRowName = "第"+(i+2)+"行";
				LinkedHashMap<String, Object> linkedHashMap = mapList.get(i);
				LinkedHashMap<String, Object> newMap = new LinkedHashMap<String, Object>();
				
				updateImportProgress(request, "格式验证及数据转换："+DataConvert.formatPecent(i, mapList.size()));
				for (String k : linkedHashMap.keySet()) {
					excelColumnName = k;
					if("".equals(excelColumnName)){
						buffer.append("表头某列为空，请检查！");
						return buffer.toString();
					}
						
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(k);
					Object obj = linkedHashMap.get(k);
					
					String type = columnType.get(k);
					if(type==null || type.equals(""))continue;
					if(type.equals("Integer")){
						if(obj==null || obj.toString().trim().equals(""))obj=0;
						else obj = (int)Double.parseDouble(""+obj);
					}else if(type.equals("Long")){
						if(obj==null || obj.toString().trim().equals(""))obj=0L;
						else 
							obj = (long)Double.parseDouble(""+obj);
					}else if(type.equals("Double")){
						if(obj==null || obj.toString().trim().equals(""))obj=0;
						else 
						obj = Double.parseDouble(""+obj);
					}else if(type.equals("Float")){
						if(obj==null || obj.toString().trim().equals(""))obj=0f;
						else 
						obj = Float.parseFloat(""+obj);
					}else if(type.equals("Date")){
						if(obj==null || obj.toString().trim().equals(""))obj=null;
						else
							try {
								obj = (Date) obj;
							} catch (Exception e) {
								obj = DateTime.parse(obj.toString());
							}
					}else if(type.equals("String")){
						if(obj==null || obj.toString().trim().equals(""))obj="";
						else 
						obj = obj.toString();
					}
					
					//正则验证
					if(!patternMap.isEmpty()){
						Map<String, Object> pMap = patternMap.get(k);
						if(pMap!=null && !pMap.isEmpty()){
							Pattern p = (Pattern) pMap.get("pattern");
							Matcher m = p.matcher(obj==null?"":obj.toString());
							if(m.matches()){
								
							}else{
								message =  ""+pMap.get("regTip");
								buffer.append(excelRowName);
								buffer.append(excelColumnName+"，");
								buffer.append(message);
								return buffer.toString();
							}
						}
					}
					
					String checkMethodName = methods.get(k);
					if(checkMethodName==null || checkMethodName.equals("") || checkMethodName.startsWith(" ") || checkMethodName.endsWith(" ")){
						
					}else{
						Method m = c.getMethod(checkMethodName, ClassUtil.getClass("javax.servlet.http.HttpServletRequest"), String.class, LinkedHashMap.class);
						message = ""+m.invoke(ContextAware.getService(c), request, k, linkedHashMap);
						if(!message.equals("succeed")){
							buffer.append(excelRowName);
							buffer.append(excelColumnName+"，");
							buffer.append(message);
							return buffer.toString();
						}
					}
					String columnName = tmp.get("column").toString();
					newMap.put(columnName, obj);
				}
				mapList.set(i, newMap);
			}
				return "succeed";
		} catch (Exception e) {
			e.printStackTrace();
			//message = e.getMessage();
			message = "数据有误，请检查!";
		}
		buffer.append(excelRowName);
		buffer.append("“"+excelColumnName+"”，");
		buffer.append(message);
		return buffer.toString();
	}
	
}
