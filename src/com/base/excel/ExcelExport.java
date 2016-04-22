package com.base.excel;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.SheetUtil;

import com.base.util.ContextAware;

public class ExcelExport {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static InputStream process(HttpServletRequest request) throws Exception{
		String workbook = request.getParameter("workbook");
		String sheet = request.getParameter("sheet");
		sheet = sheet==null||sheet.trim().equals("")?"Sheet1":sheet;
		Writer excel = null;
		
		Map<String, Object> classMap = DataConvert.get(workbook, sheet, "export");
		if(classMap!=null){
			String className = (String) classMap.get("class");
			Class c = ClassUtil.getClass(className);
			Method m = c.getMethod("invoke", ClassUtil.getClass("javax.servlet.http.HttpServletRequest"));
			Object obj = m.invoke(ContextAware.getService(c), request);
			
			LinkedHashMap<String, String> formatMap = DataConvert.getFormat(workbook, sheet);
			LinkedHashMap<String, Integer> columnSort = DataConvert.getSort(workbook, sheet);
			//正则提示
			Map<String, Map<String, String>> regTipMap = DataConvert.getRegMap(workbook, sheet);
			Map<String, String> regTip = new HashMap<String, String>();
			if(regTipMap!=null && !regTipMap.isEmpty()){
				for (String key : regTipMap.keySet()) {
					Map<String, String> regMap = regTipMap.get(key);
					if(regMap!=null && !regMap.isEmpty()){
						regTip.put(key, regMap.get("regTip"));
					}
				}
			}
			if(obj!=null){
				if(obj instanceof List){
					List<LinkedHashMap<String, Object>> mapList = (List<LinkedHashMap<String, Object>>) obj;
					if(mapList!=null && mapList.size()>0){
						List<Area> areaList = new ArrayList<Area>();
						if(mapList!=null && mapList.size()>0){
							Area a = new Area();
							a.setMapList(mapList);
							areaList.add(a);
						}
						Vertical v = new Vertical(areaList);
						v.setSheetName(sheet);
						v.setColumnSort(columnSort);
						v.setFormatMap(formatMap);
						v.setWorkBook(workbook);
						v.setRegTip(regTip);
						excel = v.create();
					}else{
						Vertical v = new Vertical();
						v.setSheetName(sheet);
						v.setColumnSort(columnSort);
						v.setFormatMap(formatMap);
						v.setWorkBook(workbook);
						v.setRegTip(regTip);
						excel = v.create();
					}
				}else if(obj instanceof LinkedHashMap){
					LinkedHashMap<String, List<LinkedHashMap<String, Object>>> mapMap = (LinkedHashMap<String, List<LinkedHashMap<String, Object>>>) obj;
					if(mapMap.size()>0){
						List<Area> areaList = new ArrayList<Area>();
						for (String title : mapMap.keySet()) {
							List<LinkedHashMap<String, Object>> mapList = mapMap.get(title);
							if(mapList!=null && mapList.size()>0){
								Area a = new Area();
								a.setTitle(title);
								a.setMapList(mapList);
								areaList.add(a);
							}
						}
						Vertical v = new Vertical(areaList);
						v.setSheetName(sheet);
						v.setColumnSort(columnSort);
						v.setFormatMap(formatMap);
						v.setWorkBook(workbook);
						v.setRegTip(regTip);
						excel = v.create();
					}
				}else if(obj instanceof Horizontal){
					Horizontal h = (Horizontal) obj;
					h.setSheetName(sheet);
					h.setColumnSort(columnSort);
					h.setFormatMap(formatMap);
					h.setWorkBook(workbook);
					h.setRegTip(regTip);
					excel = h.create();
				}else if(obj instanceof Vertical){
					Vertical v = (Vertical) obj;
					v.setSheetName(sheet);
					v.setColumnSort(columnSort);
					v.setFormatMap(formatMap);
					v.setWorkBook(workbook);
					v.setRegTip(regTip);
					excel = v.create();
				}else{
					Vertical v = new Vertical();
					v.setSheetName(sheet);
					v.setColumnSort(columnSort);
					v.setFormatMap(formatMap);
					v.setWorkBook(workbook);
					v.setRegTip(regTip);
					excel = v.create();
				}
			}else{
				Vertical v = new Vertical();
				v.setSheetName(sheet);
				v.setColumnSort(columnSort);
				v.setFormatMap(formatMap);
				v.setWorkBook(workbook);
				v.setRegTip(regTip);
				excel = v.create();
			}
			Map<String, String> methodMap = DataConvert.getValueMethod(workbook, sheet);
			for (String key : methodMap.keySet()) {
				String methodName = methodMap.get(key);
				Method vm = c.getMethod(methodName, ClassUtil.getClass("javax.servlet.http.HttpServletRequest"));
				excel.validationList(sheet, key, vm.invoke(ContextAware.getService(c), request));
			}
			Map<String, String> nameMap = DataConvert.getNameSpace(workbook, sheet);
			for (String key : nameMap.keySet()) {
				String name = nameMap.get(key);
				if(name.startsWith("WORLD")){
					String worldName;
					if(name.contains("_")){
						String tmp = name.replace("WORLD_", "");
						String[] w = tmp.split("_");
						worldName = excel.hideWorld(w);
					}else{
						worldName = excel.hideWorld();
					}
					HSSFRow row = excel.row(sheet, 0);
					Integer colIndex = excel.getColumnIndexOfRowCellValue(row, key);
					excel.formula(sheet, worldName, 1, 65535, colIndex, colIndex);
				}else{
					excel.validationList(sheet, key, FormworkValues.get(name));
				}
			}
			Map<String, String> linkageMap = DataConvert.getColumnLinkage(workbook, sheet);
			for (String key : linkageMap.keySet()) {
				String linkage = linkageMap.get(key);
				Excel.linkage(excel, sheet, linkage, key);
			}
		}else{
			Vertical v = new Vertical();
			excel = v.create();
		}
		HSSFWorkbook book = excel.getBook();
		HSSFSheet hssfsheet = book.getSheet(sheet);
		int rowNum = hssfsheet.getLastRowNum();
		if(rowNum > 0){
			Row row = hssfsheet.getRow(rowNum - 1);
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String cellValue = null;
				if(cell.getCellType() == Cell.CELL_TYPE_STRING ){
					cellValue = cell.getStringCellValue();
				}
				autoColumnWidth(hssfsheet, cell.getColumnIndex(),  cellValue);
			}
		}
		return excel.getInputStream();
	}
	
	private static void autoColumnWidth(HSSFSheet sheet, int columnIndex, String value){
		int width = (int) SheetUtil.getColumnWidth(sheet, columnIndex, false);
		 if(width != -1) {
	            width *= 256;
	            char maxColumnWidth = '\uff00';
	            if(width > maxColumnWidth) {
	                width = maxColumnWidth ;
	            }
	            if( value != null && Pattern.matches("[\u2E80-\u9FFF]+",  value)){
	            	width *= 1.5;
	            }
		 }
		sheet.setColumnWidth(columnIndex, width );
	}

	private static void changeMapKey(
			LinkedHashMap<String, List<LinkedHashMap<String, Object>>> mapMap,
			String workbook, String sheetName) {
		if(mapMap!=null && !mapMap.isEmpty()){
			LinkedHashMap<String, String> columnName = DataConvert.getColumnName(workbook, sheetName);
			for (String key : mapMap.keySet()) {
				List<LinkedHashMap<String, Object>> list = mapMap.get(key);
				for (int i = 0; i < list.size(); i++) {
					LinkedHashMap<String, Object> linkedHashMap = list.get(i);
					LinkedHashMap<String, Object> newMap = new LinkedHashMap<String, Object>();
					for (String k : linkedHashMap.keySet()) {
						if(columnName.get(k)==null){
							newMap.put(k, linkedHashMap.get(k));
						}else{
							if(!columnName.get(k).startsWith("hide"))
							newMap.put(columnName.get(k), linkedHashMap.get(k));
						}
					}
					list.set(i, newMap);
				}
			}
		}
	}
	/**
	 * 转换map的key，同时去掉hide开头的key
	 * @param mapList
	 * @param workbook
	 * @param sheetName
	 */
	public static void changeMapKey(
			List<LinkedHashMap<String, Object>> mapList,
			String workbook, String sheetName) {
		if(mapList!=null && mapList.size()>0){
			LinkedHashMap<String, String> columnName = DataConvert.getColumnName(workbook, sheetName);
			for (int i = 0; i < mapList.size(); i++) {
				LinkedHashMap<String, Object> linkedHashMap = mapList.get(i);
				LinkedHashMap<String, Object> newMap = new LinkedHashMap<String, Object>();
				for (String k : linkedHashMap.keySet()) {
					if(columnName.get(k)==null){
						newMap.put(k, linkedHashMap.get(k));
					}else{
						if(!columnName.get(k).startsWith("hide"))
						newMap.put(columnName.get(k), linkedHashMap.get(k));
					}
				}
				mapList.set(i, newMap);
			}
		}
	}

	public static void main(String[] args) {
		LinkedHashMap<String, List<LinkedHashMap<String, Object>>> mapMap = new LinkedHashMap<String, List<LinkedHashMap<String,Object>>>();
		List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String,Object>>();
		mapMap.put("阿斯顿个", list);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("yuwen", 80);
		map.put("shuxue", 80);
		list.add(map);
		changeMapKey(mapMap, "workbook", "Sheet1");
		System.out.println(mapMap);
	}

	@SuppressWarnings("rawtypes")
	public static void parseRequestParameter(HttpServletRequest request){
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++) {
				System.out.println(values[i]);
			}
		}
	}
}
