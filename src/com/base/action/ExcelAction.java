package com.base.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.excel.ExcelExport;
import com.base.excel.ExcelImport;
import com.base.util.JSONUtil;

@Controller
@RequestMapping(value="/excel")
public class ExcelAction {

	/**
	 * 打开文件上传页面
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/fileForm")
	public String fileForm(ModelMap map, HttpServletRequest request ,HttpServletResponse response) throws Exception{
		map.put("tip", request.getParameter("tip"));
		map.put("parameters", getParameters(request));
		return "excel/fileForm";
	}
	/**
	 * 更新进度
	 * @param request
	 * @param response
	 * @param message
	 * @throws Exception
	 */
	@RequestMapping(value="/updateProgress")
	public void updateProgress(HttpServletRequest request ,HttpServletResponse response, String message) throws Exception{
		long start = System.currentTimeMillis();
		String newMessage = (String) request.getSession().getAttribute("importProgress");
		while((newMessage==null || newMessage.equals(message)) && System.currentTimeMillis()-start<10000){
			Thread.sleep(100);
			newMessage = (String) request.getSession().getAttribute("importProgress");
		}
		if(newMessage!=null && newMessage.equals("导入结束")){
			request.getSession().setAttribute("importProgress", null);
		}
		JSONUtil.print(response, newMessage);
	}
	/**
	 * 检查进度
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/checkProgress")
	public void checkProgress(HttpServletRequest request ,HttpServletResponse response) throws Exception{
		String newMessage = (String) request.getSession().getAttribute("importProgress");
		System.out.println("ExcelAction_checkProgress:"+newMessage);
		JSONUtil.print(response, newMessage);
	}
	/**
	 * 取得url参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getParameters(HttpServletRequest request){
		StringBuffer buffer = new StringBuffer();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++) {
				buffer.append("&"+name+"="+values[i]);
			}
		}
		String parameters = buffer.toString();
		parameters = parameters.replaceAll("&1=1", "");
		return parameters;
	}
	/**
	 * 进入excel导入页面
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/entryImport")
	public String entryImportDialog(ModelMap map, HttpServletRequest request ,HttpServletResponse response) throws Exception{
		map.put("tip", request.getParameter("tip"));
		map.put("parameters", getParameters(request));
		return "excel/importDialog";
	}
	/**
	 * excel导入
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/import")
	public String excelImport(ModelMap map, HttpServletRequest request ,HttpServletResponse response) {
		try {
			map.put("tip", request.getParameter("tip"));
			map.put("parameters", getParameters(request));
			System.out.println("ExcelAction_excelImport_parameters:"+getParameters(request));
			String message = ExcelImport.process(request, response);
			map.put("message", "succeed".equals(message)?"导入成功":message);
			request.getSession().setAttribute("importProgress", "导入结束");
		} catch (UnsupportedEncodingException e) {
			request.getSession().setAttribute("importProgress", "导入结束");
			e.printStackTrace();
		} catch (IOException e) {
			request.getSession().setAttribute("importProgress", "导入结束");
			e.printStackTrace();
		} catch (Exception e) {
			request.getSession().setAttribute("importProgress", "导入结束");
			e.printStackTrace();
		}
		return "excel/fileForm";
	}
	/**
	 * excel导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/export")
	public void excelExport(HttpServletRequest request ,HttpServletResponse response) throws Exception {
		
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		InputStream is = ExcelExport.process(request);
		
		if(is==null)return;
		String fileName = request.getParameter("fileName");
		String workbook = null;
		if(fileName==null || fileName.trim().equals("")){
			workbook = request.getParameter("workbook")+".xls";
		}else{
			workbook = fileName+".xls";
		}
		response.setContentType(".xls");
		
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(workbook,"UTF-8"));
		}else{
			response.setHeader("Content-disposition", "attachment;filename="+new String(workbook.getBytes("UTF-8"),"ISO8859-1"));
		}
		response.setHeader("Content-Length", "");
		
		
		bis= new BufferedInputStream(is);
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while(-1 != (bytesRead = bis.read(buff, 0, buff.length))){
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
		return;
	}
}
