package com.base.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.apply.model.Student;

public class ExcelUtil {
	public static void main(String[] args) {
		try {
			ExcelUtil.write();
			//ExcelUtil.read();
			//ExcelUtil.update();
			//ExcelUtil.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入excel
	 * @author lifq
	 * @throws Exception
	 */
	public static void write() throws Exception{
		String pathname="c:\\text.xls";
		File file=new File(pathname);
		if (!file.exists()|| !file.isFile()) {
			file.createNewFile();
		}
		WritableWorkbook book=Workbook.createWorkbook(new File(pathname));
		WritableSheet sheet=book.createSheet("信息表", 0);//表单
		WritableFont font=new WritableFont(WritableFont.ARIAL,16,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);//表单字体
		WritableCellFormat titleFormat=new WritableCellFormat(font);//表格格式
		String [] title={"名称","密码"};
		for (int i = 0; i < title.length; i++) {
			Label excelTitle=new Label(i, 0, title[i], titleFormat);
			sheet.addCell(excelTitle);
		}
		
		
		List<Student> students=new ArrayList<Student>();
		for (int i = 0; i < 10; i++) {
			Student student=new Student();
			student.setAccount(i+"-"+i);
			student.setName("张三"+i);
			students.add(student);
		}
		for (int i = 0; i < students.size(); i++) {
			Student student=students.get(i);
			Label label1=new Label(0, i+1, student.getAccount());
			Label label2=new Label(1, i+1, student.getName());
			sheet.addCell(label1);
			sheet.addCell(label2);
		}
		book.write();
		book.close();
	}
	/**
	 * 读取execl
	 * @author lifq
	 * @throws Exception
	 */
	public static void read() throws Exception{
		String pathname="c:\\text.xls";
		File file=new File(pathname);
		if (!file.exists()|| !file.isFile()) {
			file.createNewFile();
		}
		Workbook book=Workbook.getWorkbook(file);
		Sheet sheet=book.getSheet(0);
		for (int i = 0; i <sheet.getRows(); i++) {
			Cell cell=sheet.getCell(0, 0);
			String result=cell.getContents();
			System.out.println(result);
		}
		
		book.close();
	}
	
	/**
	 * 更新execl
	 * @author lifq
	 * @throws Exception
	 */
	public static void update() throws Exception{
		String pathname="c:\\text.xls";
		File file=new File(pathname);
		if (!file.exists()|| !file.isFile()) {
			file.createNewFile();
		}
		Workbook wb=Workbook.getWorkbook(file);
		WritableWorkbook book=Workbook.createWorkbook(file, wb);
		WritableSheet sheet=book.createSheet("信息2", 1);
		sheet.addCell(new Label(0,0,"test2"));
		book.write();
		book.close();
	}
}
