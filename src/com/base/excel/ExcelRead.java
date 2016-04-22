package com.base.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExcelRead {

	private Workbook book;

	/**
	 * 
	 * @param path xls文件路径
	 */
	public ExcelRead(String path) {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			book = WorkbookFactory.create(is);;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
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
	 * 
	 * @param path xls文件路径
	 */
	public ExcelRead(InputStream is) {
		try {
			book = WorkbookFactory.create(is);;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Sheet sheet(String sheetName){
		if(book!=null)return book.getSheet(sheetName);
		return null;
	}
	public Row row(Sheet sheet, int rowIndex){
		if(sheet!=null)return sheet.getRow(rowIndex);
		return null;
	}
	public Row row(String sheetName, int rowIndex){
		Sheet sheet = sheet(sheetName);
		if(sheet!=null)return row(sheet, rowIndex);
		return null;
	}
	public Cell cell(Row row, int colIndex){
		if(row!=null)return row.getCell(colIndex);
		return null;
	}
	public Cell cell(Sheet sheet, int rowIndex, int colIndex){
		if(sheet!=null){
			Row row = row(sheet, rowIndex);
			if(row!=null){
				return cell(row, colIndex);
			}
		}
		return null;
	}
	public Cell cell(String sheetName, int rowIndex, int colIndex){
		Sheet sheet = sheet(sheetName);
		if(sheet!=null){
			Row row = row(sheet, rowIndex);
			if(row!=null){
				return cell(row, colIndex);
			}
		}
		return null;
	}
	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
	public Object getValue(Cell cell){
		if(cell==null)return null;
		Object cellValue = null;
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			cellValue = "";
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			double d = cell.getNumericCellValue();
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				cellValue = HSSFDateUtil.getJavaDate(d);
			} else {
				cellValue = d;
			}
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue().replaceAll("^\\s+|\\s+$", "");
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cellValue = cell.getBooleanCellValue();
		} else {
			cellValue = cell.getErrorCellValue();
		}
		if(cellValue==null || cellValue.toString().trim().equals(""))return null;
		return cellValue;
	}
	/**
	 * 获取指定行列单元格的值
	 * @param sheetName sheet名
	 * @param rowIndex 行索引
	 * @param colIndex 列索引
	 * @return
	 */
	public Object getValue(String sheetName, int rowIndex, int colIndex){
		Cell cell = cell(sheetName, rowIndex, colIndex);
		if(cell!=null)return getValue(cell);
		return null;
	}
	/**
	 * 获取一行中所有的单元格的值
	 * @param sheetName
	 * @param rowIndex 行索引，0开始，lastCellNum相当于单元格数量，lastCellNum-1可以作为最后一个单元格的索引
	 * @return
	 */
	public List<Object> getRowValue(String sheetName, int rowIndex){
		Row row = row(sheetName, rowIndex);
		if(row!=null){
			int lastCellNum = row.getLastCellNum();
			if(lastCellNum>=0){
				List<Object> list = new ArrayList<Object>();
				for (int i = 0; i < lastCellNum; i++)list.add(getValue(cell(row, i)));
				boolean isNullRow = true;
				for (Object obj : list) {
					if(obj==null || obj.toString().trim().equals(""))continue;
					isNullRow = false;
					break;
				}
				if(isNullRow)return null;
				return list;
			}
		}
		return null;
	}
	/**
	 * titleRow指定行的数据作为同列数据的key，从firstRow行开始读取第一个sheet中的数据，一行数据写入到一个LinkedHashMap；
	 * lastRowNum和lastCellNum不一样，lastRowNum就是最后一行的索引
	 * @param titleRow 标题行索引0开始
	 * @param firstRow 数据开始行索引0开始
	 * @return
	 */
	public List<LinkedHashMap<String, Object>> firstSheetData(int titleRow, int firstRow){
		String sheetName = firstSheetName();
		int lastRowNum = getLastRowNum(sheetName);
		if(lastRowNum>=0){
			List<LinkedHashMap<String, Object>> mapList = new ArrayList<LinkedHashMap<String,Object>>();
			List<Object> titleList = getRowValue(sheetName, titleRow);
			if(titleList!=null && titleList.size()>0){
				for (int i = firstRow; i <= lastRowNum; i++) {
					List<Object> list = getRowValue(sheetName, i);
					if(list==null)break;
					LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
					for (int j = 0; j < titleList.size(); j++) {
						map.put(titleList.get(j)+"", j<list.size()?list.get(j):null);
					}
					mapList.add(map);
				}
			}
			return mapList;
		}
		return null;
	}

	private int getLastRowNum(String sheetName) {
		if(sheet(sheetName)!=null)return sheet(sheetName).getLastRowNum();
		return 0;
	}

	public String firstSheetName(){
		Sheet sheet = book.getSheetAt(0);
		if(sheet==null || book.isSheetHidden(0)){
			sheet = book.getSheet("Sheet1");
			if(sheet==null || book.isSheetHidden(book.getSheetIndex(sheet))){
				int sheetCount = book.getNumberOfSheets();
				if(sheetCount>0){
					for (int i = 0; i < sheetCount; i++) {
						sheet = book.getSheetAt(i);
						if(sheet!=null && !book.isSheetHidden(i))break;
					}
				}else{
					return "no sheet";
				}
			}
		}
		return sheet.getSheetName();
	}

	public static void main(String[] args) {
		ExcelRead read = new ExcelRead("C:/Documents and Settings/Administrator/桌面/xls/test.xls");
		System.out.println(read.firstSheetName());
	}
}
