package com.base.excel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.StringUtils;

/**
 * 导出Excel工具类
 * 需要将导出的数据封装成VO实体
 * @author hg_tyf
 */
public final class ExcelTools {
	/**
	 * 导出Excel方法
	 * @param sheetName 工作单的名称
	 * @param headerTitle 第一行标题
	 * @param sources 数据源 // List<VO>
	 * @param fileName 生成的文件名
	 * @param request 请求对象
	 * @param response 响应对象
	 * @throws Exception
	 */
	public static void exportExcel(String sheetName, String[] headerTitle,
			List<?> sources, String fileName,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 设置文字居中显示
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 创建工作单
		HSSFSheet sheet = workbook.createSheet(sheetName);
//		sheet.setDefaultColumnWidth(10);
		sheet.setColumnWidth(0, 3000);
		// 创建标题行
		int rowIndex = 0;
		if (headerTitle != null && headerTitle.length > 1){
			HSSFRow headRow = sheet.createRow(rowIndex++);
			for (int i=0; i < headerTitle.length; i++){
				HSSFCell cell = headRow.createCell(i);
				cell.setCellValue(headerTitle[i]);
				// 设置文字居中显示
				cell.setCellStyle(cellStyle);
			}
		}
		// 创建其他行
		for (int i=0; i < sources.size(); i++){
			HSSFRow row = sheet.createRow(rowIndex++);
			// VO实体利用这个对象反射获取它的每一个字段的数据
			Object obj = sources.get(i);
			// 获取这个对象的所有Field的目的：得到一个get属性方法名  getXxx()
			Field[] fields = obj.getClass().getDeclaredFields();
			for (int j=0; j < fields.length;j++){
				String fieldName = fields[j].getName(); // 获取field名
				String getMethodName = "get" + StringUtils.capitalize(fieldName);//首字母大写
				// 获取get方法
				Method getMethod = obj.getClass().getDeclaredMethod(getMethodName);
				// 调用方法
				Object res = getMethod.invoke(obj);
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(res == null ? "" : res.toString());
				// 设置文字居中显示
				cell.setCellStyle(cellStyle);
			}
		}
		fileName = getCharsetStr(fileName, request);
		// 设置输出类型
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		 // 以附件的形式输出
		response.setHeader("content-Disposition", "attachment;fileName=" + fileName +".xls");
		// 最后输出
		workbook.write(response.getOutputStream());
	}
	
	/**
	 * 防止下载时出现乱码
	 * @param fileName
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCharsetStr(String fileName,
			HttpServletRequest request) throws UnsupportedEncodingException {
		// 判断浏览器的类型     《此方法可抽出用来判断浏览器的类型防止下载时出现乱码！》
		String agent = request.getHeader("user-agent");
		if (agent.toLowerCase().indexOf("firefox") != -1){
			fileName = new String(fileName.getBytes(),"iso8859-1");
		}else{
			fileName = URLEncoder.encode(fileName,"UTF-8");
		}
		return fileName;
	}
}
