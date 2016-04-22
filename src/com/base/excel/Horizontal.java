package com.base.excel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Horizontal {

	/**
	 * sheet名
	 */
	private String sheetName = "Sheet1";
	private String title;
	private List<Area> areaList = new ArrayList<Area>();
	public Horizontal() {
		
	}
	public Horizontal(List<Area> areaList) {
		super();
		this.areaList = areaList;
	}
	public void setFormatMap(LinkedHashMap<String, String> formatMap){
		if(areaList==null || areaList.size()<=0)areaList = new ArrayList<Area>();
		if(areaList.size()<=0){
			Area area = new Area();
			area.setFormatMap(formatMap);
			areaList.add(area);
		}
		for (Area area : areaList) {
			area.setFormatMap(formatMap);
		}
	}
	public void setColumnSort(LinkedHashMap<String, Integer> columnSort){
		if(areaList==null || areaList.size()<=0)areaList = new ArrayList<Area>();
		if(areaList.size()<=0){
			Area area = new Area();
			area.setColumnSort(columnSort);
			areaList.add(area);
		}
		for (Area area : areaList) {
			area.setColumnSort(columnSort);
		}
	}
	public void setRegTip(Map<String, String> regTipMap){
		if(areaList==null || areaList.size()<=0)areaList = new ArrayList<Area>();
		if(areaList.size()<=0){
			Area area = new Area();
			area.setRegTipMap(regTipMap);
			areaList.add(area);
		}
		for (Area area : areaList) {
			area.setRegTipMap(regTipMap);
		}
	}
	public void setWorkBook(String workbook){
		for (Area area : areaList) {
			List<LinkedHashMap<String, Object>> dataList = area.getMapList();
			ExcelExport.changeMapKey(dataList, workbook, sheetName);
		}
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void addArea(Area area){
		areaList.add(area);
	}
	public void empty(Area area){
		areaList.clear();
	}
	public boolean isEmpty(){
		if(areaList.size()>0)return false;
		return true;
	}
	public int getColumnCount(){
		int count = 0;
		for (Area area : areaList) {
			count += area.getColumnCount();
		}
		return count;
	}
	public Writer create(){
		Writer excel = new Writer();
		if(isEmpty()){
			
		}else{
			int firstRow = 0;
			if(title!=null){
				excel.mergeRegion(sheetName, 0, 0, 0, getColumnCount()-1, title);
				firstRow = 1;
			}
			for (Area area : areaList) {
				excel.horizontalAppendArea(sheetName, area, firstRow, 0);
			}
		}
		return excel;
	}
	public List<Area> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}
}
