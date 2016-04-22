package com.base.excel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Vertical {

	/**
	 * sheet名
	 */
	private String sheetName = "Sheet1";
	private List<Area> areaList = new ArrayList<Area>();
	public Vertical() {
		
	}
	public Vertical(List<Area> areaList) {
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
	public Writer create(){
		Writer excel = new Writer();
		if(isEmpty()){
			
		}else{
			for (Area area : areaList) {
				excel.verticalAppendArea(sheetName, area, 0);
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
