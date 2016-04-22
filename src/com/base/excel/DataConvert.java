package com.base.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 加载并缓存table.xml文件中的数据
 * @author Administrator
 *
 */
public class DataConvert {

	private static LinkedHashMap<String, Object> MAP = new LinkedHashMap<String, Object>();
	private static final String rootPath = DataConvert.class.getResource("").getPath();
	private static String xmlFileName = "table.xml";

	static{
		loadKeyValue();
	}

	/**
	 * 读取xml文件，初始化数据
	 * @param elementName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void loadKeyValue(){
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
			is = new FileInputStream(rootPath+xmlFileName);
			Document doc = reader.read(is);
			Element root = doc.getRootElement();
			MAP = (LinkedHashMap<String, Object>) parseElement(root);
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
	private static Object parseElement(Element element){
		if(element==null)return null;
		List<Element> children = element.elements();
		if(children==null || children.size()<=0){
			List list = element.attributes();
			if(list==null || list.size()<=0)return null;
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
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
				if(attr==null){
					continue;
				}else{
					String name = attr.getValue();
					if(name==null || name.trim().equals(""))continue;
				}
				map.put(attr.getValue(), parseElement(e));
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
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, String> getColumnName(String workbook, String sheet){
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> columnName = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				if(obj instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						columnName.put(""+tmp.get("column"), ""+tmp.get("name"));
					}
				}
			}
			return columnName;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, String> getNameColumn(String workbook, String sheet){
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> columnName = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
				if(tmp!=null && !tmp.isEmpty()){
					Object name = tmp.get("name");
					Object column = tmp.get("column");
					if(name!=null && !name.toString().trim().equals("") && column!=null && !column.toString().trim().equals("") && !column.toString().startsWith("hide")){
						columnName.put(""+tmp.get("name"), ""+tmp.get("column"));
					}
				}
			}
			return columnName;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, String> getColumnType(String workbook, String sheet){
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> columnName = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
				if(tmp!=null && !tmp.isEmpty()){
					columnName.put(""+tmp.get("name"), ""+tmp.get("type"));
				}
			}
			return columnName;
		}
		return null;
	}

	@SuppressWarnings({ })
	public static void main(String[] args) throws Exception {
//		show(get("workbook","Sheet1", "export"), 0);
//		System.out.println(getColumnName("workbook", "Sheet1"));
		System.out.println(getTitle("statisticSubject","Sheet1"));
	}
	@SuppressWarnings("unchecked")
	public static void show(LinkedHashMap<String, Object> map, int index){
		if(map!=null && !map.isEmpty()){
			StringBuffer b = new StringBuffer();
			for (int i = 0; i < index; i++) {
				b.append("\t");
			}
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				if(obj==null)continue;
				if(obj instanceof LinkedHashMap){
					System.out.println(b.toString()+key);
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) obj;
					show(tmp, index+1);
				}else{
					System.out.println(b.toString()+key+"="+obj);
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	public static Map<String, String> getValueMethod(String workbook,
			String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> columnName = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				Object o = map.get(key);
				if(o instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						Object obj = tmp.get("valueMethod");
						if(obj!=null)
							columnName.put(key, ""+obj);
					}
				}
			}
			return columnName;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, String> getCheckMethod(String workbook,
			String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> columnName = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				Object o = map.get(key);
				if(o instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						Object obj = tmp.get("checkMethod");
						if(obj!=null)
							columnName.put(key, ""+obj);
					}
				}
			}
			return columnName;
		}
		return null;
	}
	/**
	 * 获得列关联关系
	 * @param workbook
	 * @param sheet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getColumnLinkage(String workbook,
			String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> columnName = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				Object o = map.get(key);
				if(o instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						Object obj = tmp.get("linkage");
						if(obj!=null)
							columnName.put(key, ""+obj);
					}
				}
			}
			return columnName;
		}
		return null;
	}

	/**
	 * 解析班级编码到班级名称
	 * @param classCode
	 * @return
	 */
	public static String parseClassName(String classCode){
		if(classCode==null || classCode.trim().equals("") || classCode.length()!=5)return null;
		StringBuffer buffer = new StringBuffer();
		String part = classCode.substring(0, 2);
		buffer.append(part.equals("01")?"初":"高");
		char grade = classCode.charAt(2);
		if(grade=='1'){
			buffer.append("一(");
		}else if(grade=='2'){
			buffer.append("二(");
		}else{
			buffer.append("三(");
		}
		buffer.append(classCode.substring(3));
		buffer.append(")班");
		return buffer.toString();
	}
	/**
	 * 解析班级编码到班级名称
	 * @param grade
	 * @param classNo
	 * @return
	 */
	public static String parseClassName(String grade, String classNo){
		return parseClassName(grade+classNo);
	}
	/**
	 * 获得列排序
	 * @param workbook
	 * @param sheet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Integer> getSort(String workbook,
			String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, Integer> columnName = new LinkedHashMap<String, Integer>();
			for (String key : map.keySet()) {
				Object o = map.get(key);
				if(o instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						Object obj = tmp.get("sort");
						if(obj!=null)
							columnName.put(key, Integer.parseInt(""+obj));
					}
				}
			}
			return columnName;
		}
		return null;
	}
	/**
	 * 获得唯一属性列名称集合
	 * @param workbook
	 * @param sheet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getUnique(String workbook,
			String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			List<String> columnName = new ArrayList<String>();
			for (String key : map.keySet()) {
				Object o = map.get(key);
				if(o instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						Object obj = tmp.get("unique");
						if(obj!=null && obj.toString().equals("true"))columnName.add(key);
					}
				}
			}
			return columnName;
		}
		return null;
	}
	/**
	 * 获取为excel文件的单元格格式化表达式
	 * @param workbook
	 * @param sheet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, String> getFormat(String workbook,
			String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> formatMap = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				if(obj instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						if(!key.startsWith("hide")){
							Object fmt = tmp.get("format");
							formatMap.put(key, fmt==null?null:fmt.toString());
						}
					}
				}
			}
			return formatMap;
		}
		return null;
	}
	/**
	 * 为java数字格式化表达式（去掉了@符号的相关表达式）
	 * @param workbook
	 * @param sheet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, String> getFormat2(String workbook,
			String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			LinkedHashMap<String, String> formatMap = new LinkedHashMap<String, String>();
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				if(obj instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						if(!key.startsWith("hide")){
							Object fmt = tmp.get("format");
							if(fmt==null || fmt.toString().trim().equals("@") || fmt.toString().trim().equals("null") || fmt.toString().trim().equals(""))continue;
							formatMap.put(key, ""+fmt);
						}
					}
				}
			}
			return formatMap;
		}
		return null;
	}
	public static List<String> getTitle(String workbook, String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			List<String> list = new ArrayList<String>();
			for (String key : map.keySet()) {
				if(key.startsWith("hide"))continue;
				list.add(key);
			}
			return list;
		}
		return null;
	}

	public static LinkedHashMap<String, List<LinkedHashMap<String, Object>>> changeMapToList(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> map){
		LinkedHashMap<String, List<LinkedHashMap<String, Object>>> list = new LinkedHashMap<String, List<LinkedHashMap<String,Object>>>();
		if(map==null || map.isEmpty())return list;
		for (String key : map.keySet()) {
			LinkedHashMap<String, LinkedHashMap<String, Object>> mapMap = map.get(key);
			List<LinkedHashMap<String, Object>> mapList = new ArrayList<LinkedHashMap<String,Object>>();
			for (String k : mapMap.keySet()) {
				mapList.add(mapMap.get(k));
			}
			list.put(key, mapList);
		}
		return list;
	}

	public static String createTypeString(Object obj){
		if(obj==null || obj.toString().trim().equals("null"))return "NULL";
		if(obj instanceof String){
			return "'"+obj+"'";
		}
		if(obj instanceof java.util.Date || obj instanceof java.sql.Date  || obj instanceof Timestamp){
			java.util.Date d = (Date) obj;
			return "'"+DateTime.toLocaleString(d.getTime())+"'";
		}
		if(obj instanceof Calendar){
			Calendar c = (Calendar) obj;
			return "'"+DateTime.toLocaleString(c.getTimeInMillis())+"'";
		}
		return ""+obj;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getNameSpace(String workbook, String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			Map<String, String> nameMap = new HashMap<String, String>();
			for (String key : map.keySet()) {
				Object o = map.get(key);
				if(o instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						Object obj = tmp.get("nameSpace");
						if(obj!=null)
							nameMap.put(key, ""+obj);
					}
				}
			}
			return nameMap;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, String>> getRegMap(String workbook, String sheet) {
		LinkedHashMap<String, Object> map = get(workbook, sheet, "columns");
		if(map!=null && !map.isEmpty()){
			Map<String, Map<String, String>> mapMap = new HashMap<String, Map<String,String>>();
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				if(obj instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key);
					if(tmp!=null && !tmp.isEmpty()){
						Object reg = tmp.get("reg");
						Object regTip = tmp.get("regTip");
						if(reg!=null && !reg.toString().trim().equals("")){
							Map<String, String> regMap = new HashMap<String, String>();
							regMap.put("reg", reg.toString());
							regMap.put("regTip", regTip==null?"未知":regTip.toString());
							mapMap.put(key, regMap);
						}
					}
				}
			}
			return mapMap;
		}
		return null;
	}
	public static String formatPecent(int i, int size) {
		DecimalFormat df = new DecimalFormat("##.0%");
		return df.format(i*1.0/size);
	}
	public static Double parseDouble(Object obj) {
		if(obj==null || obj.toString().trim().equals("") || obj.toString().equals("null"))return 0.0;
		return Double.parseDouble(obj.toString());
	}
}
