package com.base.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFAnchor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

public class Writer {

	private static String DEFAULT_SHEET_NAME = "default";
	private HSSFWorkbook book = new HSSFWorkbook();
	
	private HSSFDataFormat format = book.createDataFormat();
	private Font font = book.createFont();
	
	private CellStyle titleStyle = book.createCellStyle();
	private CellStyle dateStyle = book.createCellStyle();
	private CellStyle stringStyle = book.createCellStyle();

	public Writer() {
		initStyle();
		mergeRegion(DEFAULT_SHEET_NAME, 0, 0, 0, 10, "没有任何数据");
	}
	/**
	 * 初始化样式
	 */
	private void initStyle(){
		updateStyle(titleStyle);
		dateStyle.setDataFormat(format.getFormat("yyy-MM-dd HH:mm:ss"));
		stringStyle.setDataFormat(format.getFormat("@"));
	}

	/**
	 * 创建以"horizontal_"开头的隐藏sheet
	 * @param sheetName
	 * @return
	 */
	private HSSFSheet horizontalHideSheet(String sheetName){
		sheetName = "horizontal_"+sheetName;
		return hideSheet(sheetName);
	}
	/**
	 * 创建以"vertical_"开头的隐藏sheet
	 * @param sheetName
	 * @return
	 */
	private HSSFSheet verticalHideSheet(String sheetName){
		sheetName = "vertical_"+sheetName;
		return hideSheet(sheetName);
	}
	/**
	 * 创建一个名称name,把list的数据写入名为vertical_hide的sheet作为名称name的有效值序列
	 * @param name
	 * @param list
	 * @return
	 */
	private String hideVerticalCol(String name, List<Object> list){
		if(list==null)return null;
		int firstCol = 0;
		HSSFSheet sheet = verticalHideSheet("hide");
		HSSFRow row = sheet.getRow(0);
		row = row==null?sheet.createRow(0):row;
		row.getLastCellNum();
		for (int i = 0; i < list.size(); i++) {
			int tmpIndex = row(sheet, i).getLastCellNum();
			if(tmpIndex>firstCol)firstCol=tmpIndex;
		}
		
		for (int i = 0; i < list.size(); i++) {
			setValue(cell(row(sheet, i), firstCol), list.get(i));
		}
		createName(name, sheet.getSheetName(), 0, list.size()-1, firstCol, firstCol);
		return name;
	}
	/**
	 * 创建一个名称name,把list的数据写入名为horizontal_hide的sheet作为名称name的有效值序列
	 * @param name
	 * @param list
	 * @return
	 */
	private String hideHorizontalRow(String name, List<Object> list){
		if(list==null)return null;
		if(list.size()>255)return hideVerticalCol(name, list);
		HSSFSheet sheet = horizontalHideSheet("hide");
		int lastNullRowIndex = sheet.getLastRowNum();
		HSSFRow lastRow = sheet.getRow(lastNullRowIndex);
		if(lastRow!=null)lastNullRowIndex++;
		HSSFRow row = sheet.getRow(lastNullRowIndex);
		if(row==null)row=sheet.createRow(lastNullRowIndex);
		for (int i = 0; i < list.size(); i++) {
			setValue(cell(row, i), list.get(i));
		}
		createName(name, sheet.getSheetName(), lastNullRowIndex, lastNullRowIndex, 0, list.size()-1);
		return name;
	}
	/**
	 * 读取地区xml文件，创建一系列联动名称，如果名称与name重复，name加前缀"_"，以name创建名称
	 * @param name 参考顶级名称
	 * @param key 读取地区xml文件的范围限制；比如：hideWorld("address", "中国", "广东");创建顶级名称address，有效值序列是xml中中国广东下所有元素的值
	 * @return 返回实际创建的顶级名称
	 */
	public String hideWorld(String...key){
		String name = key==null||key.length<=0?"WORLD":key[0];
		LinkedHashMap<String, Object> map = World.get(key);
		Map<String,List<Object>> listMap = World.changeToList(name, map);
		for (String k : listMap.keySet()) {
			listMap.get(k);
			createName(k, listMap.get(k));
		}
		return key==null||key.length<=0?"WORLD":key[key.length-1];
	}
	public String createNameByMap(LinkedHashMap<String, Object> map){
		if(map==null || map.isEmpty())return null;
		String name = World.getSimpleUUID();
		Map<String,List<Object>> listMap = World.changeToList(name, map);
		for (String k : listMap.keySet()) {
			listMap.get(k);
			createName(k, listMap.get(k));
		}
		return name;
	}
	/**
	 * 如果list不为空，创建一个名称name，list的数据作为其有效值序列
	 * @param name
	 * @param list
	 * @return
	 */
	public String createName(String name, List<Object> list){
		if(list!=null && list.size()>0){
			if(list.size()>255){
				return hideVerticalCol(name, list);
			}else{
				return hideHorizontalRow(name, list);
			}
		}
		return null;
	}
	/**
	 * 创建一个名称
	 * @param nameCode 名称
	 * @param sheetName 名称值引用的sheet名
	 * @param firstRow 名称值引用区域的第一行
	 * @param lastRow 名称值引用区域的最后一行
	 * @param firstCol 名称值引用区域的第一列
	 * @param lastCol 名称值引用区域的最后一行
	 */
	private void createName(String nameCode, String sheetName, int firstRow, int lastRow, int firstCol, int lastCol) {
		Name name;
		name = hideSheet(sheetName).getWorkbook().createName();
		try {
			name.setNameName(nameCode);
		} catch (Exception e) {
			hideSheet(sheetName).getWorkbook().removeName(name.getNameName());
			return ;
		}
		String formula = sheetName+"!$"+getColName(firstCol)+"$"+(firstRow+1)+":$"+getColName(lastCol)+"$"+(lastRow+1);
		name.setRefersToFormula(formula);
	}

	/**
	 * sheetName中firstRow,lastRow,firstCol,lastCol区域指定有效值序列引用fromSheet中fromRow指定行所有数据
	 * @param sheetName 引用sheet
	 * @param firstRow 首行
	 * @param lastRow 尾行
	 * @param firstCol 首列
	 * @param lastCol 尾列
	 * @param fromSheet 被引用sheet
	 * @param fromRow 被引用行
	 * @param fromCol 被引用首列
	 */
	public void valueFromRow(String sheetName, int firstRow, int lastRow, int firstCol, int lastCol, String fromSheet, int fromRow, int fromCol){
		String formula = createFormula(fromSheet, fromRow, fromRow, fromCol, notNullInRow(fromSheet, fromRow, fromCol));
		formula(sheetName, formula, firstRow, lastRow, firstCol, lastCol);
	}
	/**
	 * sheetName中firstRow,lastRow,firstCol,lastCol区域指定有效值序列引用fromSheet中fromCol指定列所有数据
	 * @param sheetName 引用sheet
	 * @param firstRow 首行
	 * @param lastRow 尾行
	 * @param firstCol 首列
	 * @param lastCol 尾列
	 * @param fromSheet 被引用sheet
	 * @param fromCol 被引用列
	 * @param fromRow 被引用首行
	 */
	public void valueFromCol(String sheetName, int firstRow, int lastRow, int firstCol, int lastCol, String fromSheet, int fromCol, int fromRow){
		String formula = createFormula(fromSheet, fromRow, notNullInCol(fromSheet, fromCol, fromRow), fromCol, fromCol);
		formula(sheetName, formula, firstRow, lastRow, firstCol, lastCol);
	}
	/**
	 * 拼写sheetName中firstRow,lastRow,firstCol,lastCol区域数据的引用公式
	 * @param sheetName sheet名
	 * @param firstRow 首行
	 * @param lastRow 尾行
	 * @param firstCol 首列
	 * @param lastCol 尾列
	 * @return 返回公式字符串表达式
	 */
	private static String createFormula(String sheetName, int firstRow, int lastRow, int firstCol, int lastCol){
		StringBuffer buffer = new StringBuffer();
		buffer.append(sheetName);
		buffer.append("!$");
		buffer.append(getColName(firstCol));
		buffer.append("$");
		buffer.append(firstRow+1);
		buffer.append(":$");
		buffer.append(getColName(lastCol));
		buffer.append("$");
		buffer.append(lastRow+1);
		return buffer.toString();
	}

	/**
	 * 获取名称为sheetName的HSSFSheet，不存在则创建
	 * @param sheetName
	 * @return
	 */
	public HSSFSheet sheet(String sheetName){
		sheetName = sheetName==null||sheetName.equals("")?DEFAULT_SHEET_NAME:sheetName;
		HSSFSheet defaultSheet = book.getSheet(DEFAULT_SHEET_NAME);
		if(defaultSheet!=null && !DEFAULT_SHEET_NAME.equals(sheetName)){
			book.setSheetName(book.getSheetIndex(defaultSheet), sheetName);
			HSSFSheet sheet = book.getSheet(sheetName);
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				sheet.removeRow(sheet.getRow(i));
			}
			sheet.removeMergedRegion(0);
		}
		HSSFSheet sheet = book.getSheet(sheetName);
		if(sheet==null)sheet=book.createSheet(sheetName);
		return sheet;
	}
	/**
	 * 创建隐藏sheet
	 * @param sheetName sheet名
	 * @return
	 */
	private HSSFSheet hideSheet(String sheetName){
		HSSFSheet sheet = book.getSheet(sheetName);
		if(sheet==null)sheet=book.createSheet(sheetName);
		book.setSheetHidden(book.getSheetIndex(sheetName), true);
		return sheet;
	}
	/**
	 * 创建行
	 * @param sheet
	 * @param index 行索引
	 * @return
	 */
	private HSSFRow row(HSSFSheet sheet, int index){
		HSSFRow row = sheet.getRow(index);
		if(row==null)row=sheet.createRow(index);
		return row;
	}
	/**
	 * 获取名称为sheetName的HSSFSheet下索引为index的行，不存在则创建
	 * @param sheetName sheet名
	 * @param index 行索引
	 * @return
	 */
	public HSSFRow row(String sheetName, int index){
		HSSFSheet sheet = sheet(sheetName);
		return row(sheet, index);
	}
	/**
	 * 第index行以上固定
	 * @param sheetName sheet名
	 * @param index 行索引
	 */
	public void freeRow(String sheetName, int index){
		sheet(sheetName).createFreezePane(0, index);
	}
	/**
	 * 创建单元格
	 * @param row
	 * @param index 列索引
	 * @return
	 */
	private HSSFCell cell(HSSFRow row, int index){
		HSSFCell cell = row.getCell(index);
		if(cell==null){
			cell=row.createCell(index);
		}
		return cell;
	}
	/**
	 * 获取名称为sheetName的HSSFSheet下索引为rowIndex的行中的索引为index的列，不存在则创建
	 * @param sheetName sheet名
	 * @param rowIndex 行索引
	 * @param index 列索引
	 * @return
	 */
	public HSSFCell cell(String sheetName, int rowIndex, int index){
		HSSFRow row = row(sheetName, rowIndex);
		return cell(row, index);
	}
	/**
	 * 合并指定区域单元格，并设置其值，会覆盖原来单元格数据
	 * @param sheetName HSSFSheet名称
	 * @param firstRow 起始行（包括）
	 * @param lastRow 结束行（包括）
	 * @param firstCol 起始列（包括）
	 * @param lastCol 结束列（包括）
	 * @param value
	 * @return
	 */
	public int mergeRegion(String sheetName, int firstRow, int lastRow, int firstCol, int lastCol, Object value){
		HSSFSheet sheet = sheet(sheetName);
		int index = sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
		setValue(sheetName, firstRow, firstCol, value);
		return index;
	}

	/**
	 * 设置指定单元格的值，会覆盖原来单元格数据
	 * @param sheetName
	 * @param rowIndex 行索引
	 * @param colIndex 列索引
	 * @param value
	 */
	public void setValue(String sheetName, int rowIndex, int colIndex, Object value){
		HSSFCell cell = cell(sheetName, rowIndex, colIndex);
		setValue(cell, value);
	}
	public void setValue(HSSFCell cell, Object value){
		if(value==null)return;
		if(value instanceof Integer){
			if(!value.toString().trim().equals("0"))
			cell.setCellValue(Integer.valueOf(value.toString()));
		}else if(value instanceof Long){
			if(!value.toString().trim().equals("0"))
			cell.setCellValue(Long.valueOf(value.toString()));
		}else if(value instanceof Float){
			cell.setCellValue(Float.valueOf(value.toString()));
		}else if(value instanceof Double){
			cell.setCellValue(Double.valueOf(value.toString()));
		}else if(value instanceof Short){
			if(!value.toString().trim().equals("0"))
			cell.setCellValue(Short.valueOf(value.toString()));
		}else if(value instanceof Byte){
			cell.setCellValue(Byte.valueOf(value.toString()));
		}else if(value instanceof Boolean){
			cell.setCellValue(Boolean.valueOf(value.toString()));
		}else if(value instanceof Date){
			cell.setCellValue((Date)value);
			cell.setCellStyle(dateStyle);
		}else if(value instanceof Calendar){
			cell.setCellValue((Calendar)value);
			cell.setCellStyle(dateStyle);
		}else{
			cell.setCellValue(value.toString());
			cell.setCellStyle(stringStyle);
		}
//		HSSFSheet sheet = cell.getSheet();
//		int width = (int) SheetUtil.getColumnWidth(sheet, cell.getColumnIndex(), false);
//		 if(width != -1) {
//	            width *= 256;
//	            char maxColumnWidth = '\uff00';
//	            if(width > maxColumnWidth) {
//	                width = maxColumnWidth ;
//	            }
//	            boolean flag = Pattern.matches("[\u2E80-\u9FFF]+", value.toString());
//	            if(flag)width *= 1.5;
//		 }
//		sheet.setColumnWidth(cell.getColumnIndex(), width );
	}
	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
	public Object getValue(HSSFCell cell){
		int type = cell.getCellType();
		switch (type) {
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case HSSFCell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		default:
			return null;
		}
	}
	/**
	 * 获取指定行列单元格的值
	 * @param sheetName sheet名
	 * @param rowIndex 行索引
	 * @param colIndex 列索引
	 * @return
	 */
	public Object getValue(String sheetName, int rowIndex, int colIndex){
		HSSFCell cell = cell(sheetName, rowIndex, colIndex);
		return getValue(cell);
	}

	public void createArea(String sheetName, Area area, int firstRow, int firstCol){
		String title = area.getTitle();
		LinkedHashMap<String, String> keyMap = area.getKeyMap();
		List<LinkedHashMap<String, Object>> mapList = area.getMapList();
		Map<String, String> regTipMap = area.getRegTipMap();
		createArea(sheetName, title, keyMap, mapList, regTipMap, firstRow, firstCol);
	}
	public void createArea(String sheetName, String title, LinkedHashMap<String, String> keyMap, List<LinkedHashMap<String, Object>> mapList, Map<String, String> regTipMap, int firstRow, int firstCol){
		if(keyMap==null || keyMap.isEmpty())return;
		List<String> keys = new ArrayList<String>();
		for (String key : keyMap.keySet()) {
			if(key==null)continue;
			keys.add(key);
		}
		int colCount = keys.size();
		int nextRow = firstRow;
		if(title!=null && !title.equals("")){
			mergeRegion(sheetName, firstRow, firstRow, firstCol, firstCol+colCount-1, title);
			nameRow(sheetName, nextRow, nextRow, firstCol, firstCol+colCount-1);
			nextRow++;
		}
		if(keys.size()>0){
			int rowCount = mapList==null?0:mapList.size();
			for (int i=0; i<keys.size(); i++) {
				int columnIndex = firstCol+i;
				setValue(sheetName, nextRow, columnIndex, keys.get(i));
				String fm = keyMap.get(keys.get(i));
				if(fm!=null && !fm.trim().equals("") && !fm.equals("@")){
					if(fm.equals("Date")){
						validationDate(sheetName, nextRow+1, nextRow+1+rowCount, columnIndex, columnIndex);
					}else{
						format(sheetName, nextRow+1, nextRow+1+rowCount, columnIndex, fm);
					}
				}else{
					formatString(sheetName, nextRow+1, nextRow+1+rowCount, columnIndex);
				}
				
				HSSFPatriarch _drawing = (HSSFPatriarch) sheet(sheetName).createDrawingPatriarch();
				HSSFCell cell = cell(sheetName, nextRow, columnIndex);
				String regTip = regTipMap.get(keys.get(i));
				if(cell!=null && regTip!=null && !regTip.trim().equals("")){
					HSSFAnchor anchor = new HSSFClientAnchor(0, 0, 20, 20, (short)3, 3, (short)5, 8);
					Comment commentglqf = _drawing.createComment(anchor);
					commentglqf.setString(new HSSFRichTextString(regTip));
					cell.setCellComment(commentglqf);
				}
			}
			nameRow(sheetName, nextRow, nextRow, firstCol, firstCol+colCount-1);
			nextRow++;
		}
		if(mapList!=null && mapList.size()>0){
			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> map = mapList.get(i);
				for (String k : map.keySet()) {
					setValue(sheetName, nextRow, firstCol+keys.indexOf(k), map.get(k));
				}
				nextRow++;
			}
		}
	}
	public Integer getColumnIndexOfRowCellValue(HSSFRow row, Object value){
		int cellCount = row.getLastCellNum();
		for (int i = 0; i < cellCount; i++) {
			Object obj = getValue(row.getSheet().getSheetName(), row.getRowNum(), i);
			if(obj!=null && obj.equals(value))return i;
		}
		return null;
	}
	/**
	 * 区域所跨所有行的最后一列后边水平添加数据区域
	 * @param sheetName
	 * @param title
	 * @param mapList
	 * @param firstRow
	 */
	public void horizontalAppendArea(String sheetName, Area area, int firstRow, int marginLeft) {
		int firstCol = firstNullInRow(sheetName, firstRow, 0);
		List<LinkedHashMap<String, Object>> mapList = area.getMapList();
		int lastRowIndex = mapList==null?sheet(sheetName).getLastRowNum():mapList.size();
		for (int i = firstRow; i <= lastRowIndex; i++) {
			int tmpIndex = firstNullInRow(sheetName, i, 0);
			if(tmpIndex>firstCol)firstCol=tmpIndex;
		}
		firstCol += marginLeft;
		createArea(sheetName, area, firstRow, firstCol);
	}
	/**
	 * 获取指定行某列开始的第一个为null的单元格的列索引
	 * @param sheetName
	 * @param rowIndex 指定行索引
	 * @param firstCol 开始列索引
	 * @return
	 */
	public int firstNullInRow(String sheetName, int rowIndex, int firstCol){
		int colIndex = firstCol;
		HSSFCell cell = null;
		HSSFRow row = sheet(sheetName).getRow(rowIndex);
		do {
			if(row!=null){
				cell = row.getCell(colIndex);
				if(cell!=null){
					colIndex++;
				}
			}else{
				return 0;
			}
		} while (cell!=null);
		return colIndex;
	}

	/**
	 * 最后一行底部垂直添加数据区域
	 * @param sheetName
	 * @param title
	 * @param mapList
	 * @param firstCol
	 */
	public void verticalAppendArea(String sheetName, Area area, int marginTop) {
		int lastNullRowIndex = sheet(sheetName).getLastRowNum();
		HSSFRow lastRow = sheet(sheetName).getRow(lastNullRowIndex);
		if(lastRow!=null)lastNullRowIndex++;
		lastNullRowIndex = marginTop>0?lastNullRowIndex+marginTop:lastNullRowIndex;
		int nextRow = lastNullRowIndex;
		createArea(sheetName, area, nextRow, 0);
	}

	/**
	 * 获取指定行某列开始的最后不为null的单元格的列索引
	 * @param sheetName
	 * @param rowIndex 指定行索引
	 * @param firstCol 开始列索引
	 * @return
	 */
	public int notNullInRow(String sheetName, int rowIndex, int firstCol){
		int colIndex = firstCol;
		HSSFCell cell = null;
		HSSFRow row = sheet(sheetName).getRow(rowIndex);
		do {
			if(row!=null){
				cell = row.getCell(colIndex);
				if(cell!=null){
					colIndex++;
				}
			}
		} while (cell!=null);
		return colIndex-1;
	}
	/**
	 * 获取指定列某行开始的最后不为null的单元格的行索引
	 * @param sheetName
	 * @param colIndex 指定列索引
	 * @param firstRow 开始行索引
	 * @return
	 */
	public int notNullInCol(String sheetName, int colIndex, int firstRow){
		int rowIndex = firstRow;
		HSSFCell cell = null;
		do {
			HSSFRow row = sheet(sheetName).getRow(rowIndex);
			if(row!=null){
				cell = row.getCell(colIndex);
				if(cell!=null){
					rowIndex++;
				}
			}else{
				cell = null;
			}
		} while (cell!=null);
		return rowIndex-1;
	}

	/**
	 * 添加数据下列选择验证
	 * @param sheetName HSSFSheet名称
	 * @param firstRow 起始行（包括）
	 * @param lastRow 结束行（包括）
	 * @param firstCol 起始列（包括）
	 * @param lastCol 结束列（包括）
	 * @param array 仅该参数中的包含的值有效
	 */
	public void validationString(String sheetName, int firstRow, int lastRow, int firstCol, int lastCol, String...array){
		if(array!=null && array.length>0){
			if(array.length<=255){
				//加载下拉列表内容   
				DVConstraint constraint = DVConstraint.createExplicitListConstraint(array);         
				//设置数据有效性加载在哪个单元格上。   
				//四个参数分别是：起始行、终止行、起始列、终止列   
				CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);   
				//数据有效性对象   
				HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
				dataValidation.createPromptBox("", "请下拉选择有效数据");
				sheet(sheetName).addValidationData(dataValidation);
			}else{
				List<Object> list = new ArrayList<Object>();
				for (int i = 0; i < array.length; i++) {
					String tmp = array[i];
					if(tmp!=null && !tmp.trim().equals(""))list.add(tmp);
				}
				String name = createName(World.getSimpleUUID(), list);
				formula(sheetName, name, firstRow, lastRow, firstCol, lastCol);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void validationList(String sheetName, String title, Object obj){
		if(obj!=null){
			Integer colIndex = getColumnIndexOfRowCellValue(row(sheetName, 0), title);
			if(colIndex==null)return ;
			if(obj instanceof List){
				List<Object> list = (List<Object>) obj;
				if(list.size()>0){
					if(list.size()<50 && list.get(0) instanceof String){
						String[] array = new String[list.size()];
						for (int i = 0; i < array.length; i++) {
							array[i] = (String) list.get(i);
						}
						//加载下拉列表内容   
						DVConstraint constraint = DVConstraint.createExplicitListConstraint(array);         
						//设置数据有效性加载在哪个单元格上。   
						//四个参数分别是：起始行、终止行、起始列、终止列   
						CellRangeAddressList regions = new CellRangeAddressList(1, 65535, colIndex, colIndex);   
						//数据有效性对象   
						HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
						dataValidation.createPromptBox("", "请下拉选择有效数据");
						sheet(sheetName).addValidationData(dataValidation);
					}else{
						String name = createName(World.getSimpleUUID(), list);
						formula(sheetName, name, 1, 65535, colIndex, colIndex);
					}
				}
			}
			if(obj instanceof LinkedHashMap){
				String topName = createNameByMap((LinkedHashMap<String, Object>) obj);
				formula(sheetName, topName, 1, 65535, colIndex, colIndex);
			}
		}
	}
	/**
	 * 添加日期验证，正确格式为：yyyy-MM-dd；比如：2011-10-01
	 * @param sheetName HSSFSheet名称
	 * @param firstRow 起始行（包括）
	 * @param lastRow 结束行（包括）
	 * @param firstCol 起始列（包括）
	 * @param lastCol 结束列（包括）
	 */
	public void validationDate(String sheetName, int firstRow, int lastRow, int firstCol, int lastCol){
		HSSFSheet sheet = sheet(sheetName);
		HSSFDataValidationHelper helper = new HSSFDataValidationHelper(sheet);
		DataValidationConstraint constraint = null;
		try {
			constraint = helper.createDateConstraint(OperatorType.GREATER_OR_EQUAL, "1970-01-01", "2025-01-01", "yyyy-MM-dd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//设置数据有效性加载在哪个单元格上。   
		//四个参数分别是：起始行、终止行、起始列、终止列   
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);   
		//数据有效性对象   
		HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);   
		dataValidation.createErrorBox("日期格式错误", "正确格式为：yyyy-MM-dd；比如：2011-10-01");
		dataValidation.createPromptBox("日期格式", "yyyy-MM-dd");
		sheet(sheetName).addValidationData(dataValidation);
		format(sheetName, firstCol, lastCol, "yyyy/m/d");
	}
	/**
	 * 指定行列的单元格添加提示内容
	 * @param sheetName
	 * @param rowIndex
	 * @param colIndex
	 * @param content
	 */
	public void tip(String sheetName, int rowIndex, int colIndex, String content){
		HSSFPatriarch drawing = (HSSFPatriarch) sheet(sheetName).createDrawingPatriarch();
		HSSFAnchor anchor = new HSSFClientAnchor(0, 0, 20, 20, (short)3, 3, (short)5, 8);
		Comment commentglqf = drawing.createComment(anchor);
		commentglqf.setString(new HSSFRichTextString(content));
		cell(sheetName, rowIndex, colIndex).setCellComment(commentglqf);
	}
	/**
	 * 名称为sheetName的HSSFSheet指定区域内单元格格式设置
	 * @param sheetName HSSFSheet名称
	 * @param firstCol 起始列（包括）
	 * @param lastCol 结束列（包括）
	 * @param expression xls单元格格式表达式；如：文本格式--"@"，百分比格式=="0.00%"；更多请参考xls设置单元格格式
	 */
	public void format(String sheetName, int firstCol, int lastCol, String expression){
		expression = expression==null||expression.trim().equals("")?"@":expression;
		CellStyle style = book.createCellStyle();
		HSSFDataFormat format = book.createDataFormat();
		style.setDataFormat(format.getFormat(expression));
		for (int j = firstCol; j <= lastCol; j++) {
			sheet(sheetName).setDefaultColumnStyle(j, style);
		}
	}
	public void format(String sheetName, String expression, int colIndex){
		expression = expression==null||expression.trim().equals("")?"@":expression;
		CellStyle style = book.createCellStyle();
		HSSFDataFormat.getBuiltinFormat(expression);
		style.setDataFormat(format.getFormat(expression));
		sheet(sheetName).setDefaultColumnStyle(colIndex, style);
	}
	public void format(String sheetName, int firstRow, int lastRow, int colIndex, String expression){
		expression = expression==null||expression.trim().equals("")?"@":expression;
		CellStyle style = book.createCellStyle();
		HSSFDataFormat.getBuiltinFormat(expression);
		style.setDataFormat(format.getFormat(expression));
		for (int j = firstRow; j <= lastRow; j++) {
			cell(sheetName, j, colIndex).setCellStyle(style);
		}
	}
	public void formatString(String sheetName, int firstRow, int lastRow, int colIndex){
		for (int j = firstRow; j <= lastRow; j++) {
			cell(sheetName, j, colIndex).setCellStyle(stringStyle);
		}
	}
	/**
	 * 指定区域所有单元格加上公式
	 * @param sheetName
	 * @param formulaString 公式
	 * @param firstRow 起始行
	 * @param lastRow 终止行
	 * @param firstCol 起始列
	 * @param lastCol 终止列
	 */
	public void formula(String sheetName, String formulaString, int firstRow, int lastRow, int firstCol, int lastCol){
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint
				.createFormulaListConstraint(formulaString);
		// 设置数据有效性加载在哪个单元格上。
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,
				lastRow, firstCol, lastCol);
		// 数据有效性对象
		DataValidation data_validation_list = new HSSFDataValidation(regions,
				constraint);
		// 设置输入信息提示信息
		data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
		// 设置输入错误提示信息
		data_validation_list
				.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
		sheet(sheetName).addValidationData(data_validation_list);
	}
	/**
	 * 添加居中样式
	 * @param style
	 */
	public void updateStyle(CellStyle style){
		// 水平对齐方式设置
		style.setAlignment(CellStyle.ALIGN_CENTER);
		// 垂直对齐方式设置
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 边框颜色和宽度设置
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		//自动换行
		style.setWrapText(true);
		// 设置背景颜色
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 粗体字设置
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
	}
	/**
	 * 列名称行样式
	 * @param sheetName HSSFSheet名称
	 * @param firstRow 起始行（包括）
	 * @param lastRow 结束行（包括）
	 * @param firstCol 起始列（包括）
	 * @param lastCol 结束列（包括）
	 */
	public void nameRow(String sheetName, int firstRow, int lastRow, int firstCol, int lastCol){
		for (int i = firstRow; i <= lastRow; i++) {
			for (int j = firstCol; j <= lastCol; j++) {
				cell(sheetName, i, j).setCellStyle(titleStyle);
			}
		}
	}

	/**
	 * 获得文件输入流
	 * @return
	 */
	public InputStream getInputStream(){
		InputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			book.write(os);
			byte[] content = os.toByteArray();
			is = new ByteArrayInputStream(content);
			return is;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(os!=null)os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * @return the book
	 */
	public HSSFWorkbook getBook() {
		return book;
	}
	/**
	 * @param book the book to set
	 */
	public void setBook(HSSFWorkbook book) {
		this.book = book;
	}

	/**
	 * 获取mapList中所有map的key的并集，并加上序号keyIndex
	 * @param mapList
	 * @return
	 */
	public static LinkedHashMap<String, Integer> getAllKeys(List<LinkedHashMap<String, Object>> mapList){
		LinkedHashMap<String, Integer> keyMap = new LinkedHashMap<String, Integer>();
		int keyIndex = 0;
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> map = mapList.get(i);
			for (String key : map.keySet()) {
				if(!keyMap.containsKey(key)){
					keyMap.put(key, keyIndex++);
				}
			}
		}
		return keyMap;
	}
	/**
	 * 获取列名：例如：0=A,26=AA,27=AB,255=IV
	 * @param index
	 * @return
	 */
	public static String getColName(int index){
		int sum = index/26-1;
		int mod = index%26;
		return ""+(sum<0?"":(char)(sum+'A'))+(char)(mod+'A');
	}
}
