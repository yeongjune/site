package com.base.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class World {

	/**
	 * 根路径
	 */
	public static final String rootPath = FormworkValues.class.getResource("").getPath();
	/**
	 * 存储所有xml中解析得到的数据
	 */
	private static LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();

	static{
		init();
	}

	/**
	 * 获取指定范围地区表数据；例如：get()将得到全世界所有国家和地区信息，get("中国", "广东")将得到中国广东的所有市区县地区信息
	 * @return 递归结果集；如Object!=null都可以强转为LinkedHashMap<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Object> get(String...key){
		LinkedHashMap<String, Object> map = null;
		if(key==null || key.length<=0){
			map = linkedHashMap;
		}else{
			map = get(linkedHashMap, key, 0);
		}
		return (LinkedHashMap<String, Object>) map.clone();
	}
	@SuppressWarnings("unchecked")
	private static LinkedHashMap<String, Object> get(LinkedHashMap<String, Object> map, String[] key, int cursor) {
		if(key==null || key.length<=0){
			return map;
		}else{
			if(cursor>=key.length)return map;
			LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) map.get(key[cursor]);
			if(tmp==null)return map;
			return get(tmp, key, cursor+1);
		}
	}

	/**
	 * 控制台查看指定范围地区表数据；例如：show()将得到全世界所有国家和地区信息，show("中国", "广东")将得到中国广东的所有市区县地区信息
	 * @param key
	 */
	public static void show(String...key){
		show(get(key));
	}
	public static void show(LinkedHashMap<String, Object> map){
		show(map, 0);
	}
	@SuppressWarnings("unchecked")
	private static void show(Object obj, int index){
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)obj;
		if(map!=null && !map.isEmpty()){
			String space = "";
			for (int i = 0; i < index; i++) {
				space+="\t";
			}
			for (String key : map.keySet()) {
				System.out.println(space+key);
				show((LinkedHashMap<String, Object>) map.get(key), index+1);
			}
		}
	}

	/**
	 * 将LinkedHashMap转换为Map<List>，为了写入xls文件
	 * @param name
	 * @param lhm
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<Object>> changeToList(String name, LinkedHashMap<String, Object> lhm){
		Map<String, List<Object>> listMap = new HashMap<String, List<Object>>();
		if(lhm!=null && !lhm.isEmpty()){
			if(lhm.containsKey(name)){
				LinkedHashMap<String, Object> newMap = new LinkedHashMap<String, Object>();
				for (String k : lhm.keySet()) {
					if(k.equals(name)){
						newMap.put(k+"_", lhm.get(k));
					}else{
						newMap.put(k, lhm.get(k));
					}
				}
				lhm = newMap;
			}
			List<Object> list = new ArrayList<Object>(lhm.keySet());
			listMap.put(name, list);
			for (String key : lhm.keySet()) {
				Object obj = lhm.get(key);
				if(obj!=null && obj instanceof LinkedHashMap){
					LinkedHashMap<String, Object> tmp = (LinkedHashMap<String, Object>) obj;
					listMap.putAll(changeToList(key, tmp));
				}
			}
		}
		return listMap;
	}

	/**
	 * 读取xml文件，初始化数据到linkedHashMap
	 * @param elementName
	 * @return
	 */
	public static void init(){
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
			is = new FileInputStream(rootPath+"world.xml");
			Document doc = reader.read(is);
			Element root = doc.getRootElement();
			parseElement(linkedHashMap, root);
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
	/**
	 * 递归解析xml元素
	 * @param parentMap
	 * @param e
	 */
	@SuppressWarnings("unchecked")
	private static void parseElement(LinkedHashMap<String, Object> parentMap, Element e){
		if(e!=null){
			Attribute attribute = e.attribute("Name");
			List<Element> children = e.elements();
			if(attribute==null){
				for (Element element : children) {
					parseElement(parentMap, element);
				}
			}else{
				LinkedHashMap<String, Object> tmpMap = new LinkedHashMap<String, Object>();
				for (Element element : children) {
					parseElement(tmpMap, element);
				}
				String name = attribute.getValue();
				parentMap.put(name, tmpMap);
			}
		}
	}
	/**
	 * linkedMap及其子集是否包含name
	 * @param linkedMap
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean contains(
			LinkedHashMap<String, Object> linkedMap, String name) {
		if(linkedMap==null || linkedMap.isEmpty()){
			return false;
		}else{
			if(linkedMap.containsKey(name))return true;
			for (String k : linkedMap.keySet()) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) linkedMap.get(k);
				if(contains(map, name))return true;
			}
			return false;
		}
	}

	public static String getSimpleUUID(){
		String uuidStr = UUID.randomUUID().toString();
		uuidStr = uuidStr.replaceAll("-", "");
		return "name_"+uuidStr.substring(10);
	}
}
