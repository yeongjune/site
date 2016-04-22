package com.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
	

	/**
	 * 针对id pid 结构做一个树结构排序
	 * @param listMap			需要排序的listMap
	 * @param id				默认是0	
	 * @param resultListMap		存放结果集
	 * @return
	 */
	public static List<Map<String, Object>> getSortBypid(List<Map<String, Object>> listMap, Integer id, List<Map<String, Object>> resultListMap){
		for(Map<String, Object> map : listMap){
			Integer pid = (Integer) map.get("pid");
			if(id.intValue() == pid.intValue()){
				Integer currentId = (Integer) map.get("id");
				resultListMap.add(map);
				getSortBypid(listMap, currentId, resultListMap);
			}else{
				continue;
			}
		}
		return resultListMap;
	}
	
	/**删除List里面Map的key值
	 * @param list
	 * @param key
	 */
	public static List<Map<String, Object>> deleteListMapByKey(List<Map<String, Object>> list,String...keys){
		if (list!=null&&!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				for (int i = 0; i < keys.length; i++) {
					if (map.containsKey(keys[i])) {
						map.remove(keys[i]);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据近key和value删除list的元素
	 * @author lifqiang
	 * @param list
	 * @param key
	 *  @param value
	 * @param equals  为true是表示删除符合条件的，为false时表示删除不符合条件的
	 * @return
	 */
	public static List<Map<String,Object>> deleteListMapByKey(List<Map<String,Object>> list,String key,String value,boolean equals){
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map=list.get(i);
			if (equals) {
				if (map.containsKey(key)&&value.equals(map.get(key).toString())) {
					list.remove(map);
					i=-1;
				}
			}else{
				if (map.containsKey(key)&&!value.equals(map.get(key).toString())) {
					list.remove(map);
					i=-1;
				}
			}
		}
		return list;
	}
	
	/**
	 * 返回要保留的key的Map
	 * @author lifqiang
	 * @param map
	 * @param keys
	 * @return
	 */
	public static Map<String, Object> getMapByKeys(Map<String, Object> map,String ...keys){
		List<String> needKeys=Arrays.asList(keys);	 //需要保留的key
		List<String> allKeys=new ArrayList<String>();//所有key
		for (String key : map.keySet()) {
			allKeys.add(key);
		}
		for (String key : allKeys) {
			if (map.containsKey(key) && !needKeys.contains(key)) {
				map.remove(key);
			}
		}
		return map;
	}
	
	/**
	 * 返回要保留的key的List
	 * @author lifqiang
	 * @param list
	 * @param keys
	 * @return
	 */
	public static List<Map<String, Object>> getListMapByKeys(List<Map<String, Object>> list,String ...keys){
		for (int i = 0; i < list.size(); i++) {
			list.set(i, getMapByKeys(list.get(i), keys));
		}
		return list;
	}
	
	/**
	 * 截取List里元素map对应key的值的长度
	 * @author lifq
	 * @param list 
	 * @param key 要截取的key
	 * @param length 截取的长度
	 * @param replaceHtml 是否替换html
	 * @return
	 */
	public static void trimListMapValue(List<Map<String, Object>> list,String key,Integer length,boolean replaceHtml){
		if (list==null||list.size()<1) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map=list.get(i);
			if (map.containsKey(key)&&!StringUtil.isEmpty(map.get(key)+"")) {
				String value=map.get(key)+"";
				if (replaceHtml) {
					value=value.replaceAll("\n", "").
							replaceAll("\r", "").
							replaceAll("\t", "").
							//replaceAll(" ", "").
							replaceAll("&.+?;", "").
							replaceAll("<.+?>", "");
				}
				if (length!=null&&length>0) {
					value=StringUtil.subStr(value, length);
				}
				list.get(i).put(key, value);
			}
		}
	}
	public static List<String> findArticleFileList(String content){
		List<String> list=new ArrayList<String>();
		String regex="src=\"(.+?\\.((swf)|(flv)|(mp3)|(ogg)|(webm)|(mp4)|(wav)|(wma)|(wmv)|(mid)|(avi)|(mpg)|(asf)|(rm)|(rmvb)))\"?";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(content);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		return list;
	}
}
