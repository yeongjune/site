package com.base.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Excel {

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
	/**
	 * 获取sheet中内容结尾第一个空行的索引
	 * @param sheet
	 * @return
	 */
	public static int firstNullRowIndex(HSSFSheet sheet){
		int lastIndex = sheet.getLastRowNum();
		HSSFRow row = sheet.getRow(lastIndex);
		if(row.getLastCellNum()>0)return lastIndex+1;
		return lastIndex;
	}
	/**
	 * 设置单元格的值
	 * @param cell
	 * @param value
	 */
//	public static void setValue(HSSFCell cell, Object value){
//		if(value==null)return;
//		if(value instanceof Integer){
//			cell.setCellValue(Integer.valueOf(value.toString()));
//		}else if(value instanceof Long){
//			cell.setCellValue(Long.valueOf(value.toString()));
//		}else if(value instanceof Float){
//			cell.setCellValue(Float.valueOf(value.toString()));
//		}else if(value instanceof Double){
//			cell.setCellValue(Double.valueOf(value.toString()));
//		}else if(value instanceof Short){
//			cell.setCellValue(Short.valueOf(value.toString()));
//		}else if(value instanceof Byte){
//			cell.setCellValue(Byte.valueOf(value.toString()));
//		}else if(value instanceof Boolean){
//			cell.setCellValue(Boolean.valueOf(value.toString()));
//		}else if(value instanceof Date){
//			cell.setCellValue((Date)value);
//			cell.setCellStyle(style)
//		}else if(value instanceof Calendar){
//			cell.setCellValue((Calendar)value);
//		}else{
//			cell.setCellValue(value.toString());
//		}
//	}
	/**
	 * 获得文件输入流
	 * @return
	 */
	public static InputStream getInputStream(HSSFWorkbook book){
		if(book==null)return null;
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
	 * 写出文件
	 * @param book
	 * @param path
	 * @throws FileNotFoundException
	 */
	public static void write(HSSFWorkbook book, String path) throws FileNotFoundException{
		if(book==null || path==null)return;
		FileOutputStream out = new FileOutputStream(path);
		InputStream is = null;
		try {
			is = getInputStream(book);
			byte[] b = new byte[1024];
			while(is.read(b)!=-1){
				out.write(b);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加地区数据到隐藏sheet
	 * @param topName 预设顶级名称
	 * @param excel xls写出对象
	 * @param key 地区范围限制参数，如："中国","广东" 将仅仅加入广东地区的市区县信息数据到隐藏sheet
	 * @return 返回实际创建的顶级名称
	 */
	public static String hideWorld(Writer excel, String...key){
		return excel.hideWorld(key);
	}
	public static void main(String[] args) throws FileNotFoundException {
		Writer excel = new Writer();
		String name = excel.hideWorld();
		String china = excel.hideWorld("中国");
		excel.formula("Sheet1", name, 0, 65535, 0, 0);
		linkage(excel, "Sheet1", 0, 1);
		linkage(excel, "Sheet1", 1, 2);
		excel.formula("Sheet1", china, 0, 65535, 3, 3);
		linkage(excel, "Sheet1", 3, 4);
		linkage(excel, "Sheet1", 4, 5);
		write(excel.getBook(), "C:/Documents and Settings/Administrator/桌面/xls/test.xls");
	}
	/**
	 * excel中名为sheetName的sheet的指定区域所有单元格加上公式
	 * @param excel
	 * @param sheetName
	 * @param formula 公式
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public static void formula(Writer excel, String sheetName, String formula, int firstRow, int lastRow, int firstCol, int lastCol){
		excel.formula(sheetName, formula, firstRow, lastRow, firstCol, lastCol);
	}
	/**
	 * 给sheetName的指定区域所有单元格加上数据显示格式
	 * @param excel Writer对象
	 * @param sheetName
	 * @param fm 数据格式化表达式
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public static void format(Writer excel, String sheetName, String fm, int firstCol, int lastCol){
		excel.format(sheetName, firstCol, lastCol, fm);
	}
	/**
	 * excel的sheetName的to列关联from列的值，为了做地区数据联动下拉选择
	 * @param excel
	 * @param sheetName
	 * @param from 被关联的列索引
	 * @param to 关联的列索引
	 */
	public static void linkage(Writer excel, String sheetName, int from, int to){
		excel.formula(sheetName, "INDIRECT(INDIRECT(ADDRESS(ROW(),"+(from+1)+")))", 0, 65535, to, to);
	}
	public static void linkage(Writer excel, String sheetName, String from, String to){
		Integer fromIndex = excel.getColumnIndexOfRowCellValue(excel.row(sheetName, 0), from);
		if(fromIndex==null)return ;
		Integer toIndex = excel.getColumnIndexOfRowCellValue(excel.row(sheetName, 0), to);
		if(toIndex==null)return ;
		excel.formula(sheetName, "INDIRECT(INDIRECT(ADDRESS(ROW(),"+(fromIndex+1)+")))", 1, 65535, toIndex, toIndex);
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
}
