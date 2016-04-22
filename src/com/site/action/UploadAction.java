package com.site.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.base.util.FileUtil;
import com.base.util.ImageUitl;
import com.base.util.JSONUtil;
import com.base.util.RegexUtil;
import com.base.util.StringUtil;

/**
 * 文件上传下载公共控制器
 * 
 */

@Controller
@RequestMapping(value = "uploadFile")
public class UploadAction {


	/**
	 *  保存文件
	 * @param modleName 模块名，传入时文件保存在upload/+modleName目录下，不传保存在upload目录下
	 * @throws Exception
	 */
	@RequestMapping(value = "save")
	public void save(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession, String modleName) throws Exception
			 { 
		Map<String, Object> result=new HashMap<String, Object>();//最后输出的json数据
		result.put("code", "fail");
		result.put("msg", "上传失败");
		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 这里是表单的名字在swfupload.js中this.ensureDefault("file_post_name","filedata");
			CommonsMultipartFile uploadFile = (CommonsMultipartFile) multipartRequest.getFile("attachmentTempPath");
			String fileName = uploadFile.getOriginalFilename();
			InputStream is = uploadFile.getInputStream();
			// 获取文件后缀名
			String ext = RegexUtil.parse("\\.(\\w+)", fileName);
			// 上传到服务器文件名字 
			String name =  System.currentTimeMillis()+ "." + ext;
			// 保存文件路径 
			String filePath =httpSession.getServletContext().getRealPath("/")+"upload/";
			if (!StringUtil.isEmpty(modleName)) {
				filePath+= modleName+"/";
			}
			//上传大小限制
			if (uploadFile.getSize()>(1024*1024*10)) {
				result.put("msg","上传失败,上传文件过大，请确保上传的文件小于1M");
				result.put("error", 1);
				JSONUtil.printToHTML(response, result);
				return;
			}
			//报名系统上传缩略图限制格式
			if ((modleName+"").equals("applyFace") ) {
				if (!Arrays.<String>asList("gif,jpg,jpeg,png,bmp".split(",")).contains(ext.toLowerCase())) {
					result.put("msg","上传失败,不支持该格式文上传，请确保格式为：gif,jpg,jpeg,png,bmp");
					result.put("error", 1);
					JSONUtil.printToHTML(response, result);
					return;
				}else if (uploadFile.getSize()>1024*1024*10) {
					result.put("msg","上传失败,上传的图片过大，请确保上传的图片小于10M");
					result.put("error", 1);
					JSONUtil.printToHTML(response, result);
					return;
				}
		
			}
			
			// 文件不存在先创建
			
			File creatfile = new File(filePath);
			if (!creatfile.exists()) {
				creatfile.mkdirs();
			}
			String newFile=filePath + name;
			
			//压缩图片
			if (Arrays.<String>asList("gif,jpg,jpeg,png,bmp".split(",")).contains(ext) && uploadFile.getSize()>1024*50 ) {
				if ((modleName+"").equals("applyFace")) {//华颖报名系统的头像上传
					//ImageUitl.maxWidthHeight2(is, file, 500,500);
					ImageUitl.imageZip(is, newFile, null, 500, 500, true);
				}else{//其他图片
					ImageUitl.imageZip(is, newFile, null, 1440, 900, true);
				}
			}else{//其他文件
				FileUtil.copyFile(is, new File(newFile));
			}
			
			result.put("code", "success");
			result.put("sourceName", fileName);
			result.put("msg", "上传成功");
			if (!StringUtil.isEmpty(modleName)) {
				//result.put("filePath","/upload/"+modleName+"/"+name);
				result.put("url", "/upload/"+modleName+"/"+name);
			}else{
				//result.put("filePath", "/upload/"+name);
				result.put("url", "/upload/"+name);
			}
			result.put("error", 0);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "上传失败");
			result.put("error", 1);
		}
		JSONUtil.printToHTML(response, result);
	}


	/**
	 * 删除文件
	 * @param filePath 需要删除的文件路径，从根目录算起
	 * @throws IOException
	 */
	@RequestMapping(value = "delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "filePath") String filePath) throws IOException {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", "fail");
		result.put("msg", "删除失败");
		try {
			String pathname =session.getServletContext().getRealPath("/")+filePath;
			File file = new File(pathname);
			if (file.exists()) {
				FileUtil.delete(file);
			}
			result.put("code", "success");
			result.put("msg", "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}

	
	/**
	 * 下载文件
	 * @param filePath 文件路径，从根目录算起
	 * @throws IOException
	 */
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "filePath") String filePath) throws IOException {
		String pathname = session.getServletContext().getRealPath("/")+filePath;
		String source_name="";
		if (filePath.lastIndexOf("/")!=-1) {
			source_name=filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
		}else{
			source_name=filePath;
		}
		this.readFile(pathname,source_name,response,request);
	}

	/**
	 * 读取文件
	 * 
	 * @param filePath 文件路径
	 * @param source_name 文件名真实名
	 * @param response
	 * @throws IOException
	 */
	public  void readFile(String filePath, String source_name,
			HttpServletResponse response,HttpServletRequest request ) throws IOException {
		File file = new File(filePath);
		response.setCharacterEncoding("UTF-8");
		
		
		// 先判断文件存在
		if (!file.exists()) {
			PrintWriter outMessage = response.getWriter(); 
			outMessage.print( "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
					"<html>" +
					"<script type='text/javascript' src='"+request.getContextPath()+"/plugs/artDialog/artDialog.js?skin=default'></script>" +
					"<script type='text/javascript' src='"+request.getContextPath()+"/plugs/artDialog/plugins/iframeTools.js'></script>" +
				" <script> art.dialog.through({background:'white',opacity:0.6,fixed:true,icon:'warning',width:230,height:100,title :'提示',content :'您下载的文件已经不存在!'," +
					"lock : true,ok : function(){window.history.go(-1);}});</script>" +
					"</html>"); 
			outMessage.flush();
			outMessage.close();
			
			/*outMessage.print( " <script>alert(''); window.history.go(-1);</script>"); 
			outMessage.flush();
			outMessage.close();*/
		} else {
			ServletOutputStream out = response.getOutputStream();
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(file));
			
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				response.setHeader("Content-disposition", "attachment; filename="
				+ URLEncoder.encode(source_name, "UTF-8"));// 设定输出文件头
	            /*response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(title, "UTF-8"),"UTF-8"));*/
	        }else{
	        	response.setHeader("Content-disposition", "attachment;filename="+new String(source_name.getBytes("UTF-8"),"ISO8859-1"));
	        }
			
			/*String disposition = "attachment;filename="
					+ URLEncoder.encode(source_name, "UTF-8");
			response.setHeader("Content-Disposition", disposition);*/
			int fileLen = (int) file.length();
			byte[] bs = new byte[fileLen];
			int len = 0;
			
			while ((len = is.read(bs)) > 0) {
				out.write(bs, 0, len);
			}
			out.close();
			is.close();
		}
		
	}

}
