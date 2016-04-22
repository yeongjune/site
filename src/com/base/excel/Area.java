package com.base.excel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * excel文件中的一个矩形区域数据
 * @author Administrator
 *
 */
public class Area {

	/**
	 * 标题
	 */
	private String title;
	private List<LinkedHashMap<String, Object>> mapList;
	private LinkedHashMap<String, String> formatMap;
	private LinkedHashMap<String, Integer> columnSort;
	private Map<String, String> regTipMap;
	public Area() {
		
	}
	public Area(String title, List<Map<String, Object>> mapList) {
		super();
		this.title = title;
		this.mapList = new ArrayList<LinkedHashMap<String,Object>>();
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> tmp = mapList.get(i);
			if(tmp==null || tmp.isEmpty())continue;
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.putAll(tmp);
			this.mapList.add(map);
		}
	}
	public int getColumnCount(){
		int count = 0;
		for (LinkedHashMap<String, Object> map : mapList) {
			if(map==null || map.isEmpty())continue;
			int tmpCount = 0;
			for (String key : map.keySet()) {
				if(key.startsWith("hide"))continue;
				tmpCount++;
			}
			if(tmpCount>count)count=tmpCount;
		}
		return count;
	}
	public int getRowCount(){
		if(title!=null && !title.equals(""))return mapList.size()+1;
		return mapList.size();
	}

	public LinkedHashMap<String, String> getKeyMap(){
		List<String> k1 = new ArrayList<String>();
		if(mapList!=null){
			for (LinkedHashMap<String, Object> map : mapList) {
				if(map==null || map.isEmpty())continue;
				for (String string : map.keySet()) {
					if(!k1.contains(string))k1.add(string);
				}
			}
		}
		List<String> list = new ArrayList<String>();
		if(formatMap!=null){
			for (String string : formatMap.keySet()) {
				list.add(string);
			}
		}
		Set<String> k3 = columnSort.keySet();
		k1.removeAll(list);
		list.addAll(k1);
		list.removeAll(k3);
		for (String string : columnSort.keySet()) {
			Integer i = columnSort.get(string);
			if(i<list.size()){
				list.add(i, string);
			}else{
				list.add(string);
			}
		}
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		for (String string : list) {
			String fm = formatMap.get(string);
			map.put(string, fm);
		}
		return map;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<LinkedHashMap<String, Object>> getMapList() {
		return mapList;
	}
	public void setMapList(List<LinkedHashMap<String, Object>> mapList) {
		this.mapList = mapList;
	}
	public LinkedHashMap<String, String> getFormatMap() {
		return formatMap;
	}
	public void setFormatMap(LinkedHashMap<String, String> formatMap) {
		this.formatMap = formatMap;
	}
	public LinkedHashMap<String, Integer> getColumnSort() {
		return columnSort;
	}
	public void setColumnSort(LinkedHashMap<String, Integer> columnSort) {
		this.columnSort = columnSort;
	}
	public Map<String, String> getRegTipMap() {
		return regTipMap;
	}
	public void setRegTipMap(Map<String, String> regTipMap) {
		this.regTipMap = regTipMap;
	}
}
