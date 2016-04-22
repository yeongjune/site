package com.site.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.authority.model.User;
import com.base.util.*;
import com.site.service.SiteService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.base.config.Init;
import com.site.model.Image;
import com.site.service.ArticleService;
import com.site.service.ImageService;

@Controller
@RequestMapping(value = "image")
public class ImageAction {
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private SiteService siteService;
	
	/**
	 *  保存文件
	 * @author lifq
	 * @param modleName 模块名
	 * @param kindeditor 标识是不是kindeditor插件上传的文件(如是是请将kindeditor的上传名filePostName设置为：attachmentTempPath ,应该该方法保存的文件名为attachmentTempPath，而kindeditor上传文件参数名默认为imageFile)
	 * @param type 分类：1、图片类型的新闻图片文件； 2、新闻的缩略图文件； 3、新闻的附件文件，4、其他文件
	 * @param tempId 区分是不是同一条新闻的文件
	 * @param articleId 文章ID，新增新闻的时候不需要传
	 */
	@RequestMapping(value = "save")
	public void save(HttpServletRequest request, HttpServletResponse response,HttpSession httpSession, 
			String modleName,String kindeditor,
			@RequestParam(value = "type")Integer type,
			@RequestParam(value = "tempId")String tempId,
			Integer articleId) { 
		Map<String, Object> result=new HashMap<String, Object>();//最后输出的json数据
		result.put("code", Init.FAIL);//上传成功或失败的返回标志:fail失败，succeed成功
		result.put("data", "");		  //保存成功后返回的文件记录信息
		result.put("msg", "上传失败");		//消息

        Integer siteId= User.getCurrentSiteId(request);
        String siteFilePath = "upload/" + siteId + "/";// 网站上传目录
		//上传成功后返回的文件记录信息
		Image image=new Image();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 这里是表单的名字在swfupload.js中this.ensureDefault("file_post_name","filedata");
			CommonsMultipartFile uploadFile = (CommonsMultipartFile) multipartRequest.getFile("attachmentTempPath");
			
			//上传的图片名
			String fileName = uploadFile.getOriginalFilename();
			//输入流
			InputStream is = uploadFile.getInputStream();	   
			// 获取文件后缀名
			String ext = RegexUtil.parse("\\.(\\w+)", fileName);
			
			if (type==1 || type==2) {
				if (!Arrays.<String>asList("gif,jpg,jpeg,png,bmp".split(",")).contains(ext.toLowerCase())) {
					result.put("msg","上传失败,不支持该格式文上传，请确保格式为：gif,jpg,jpeg,png,bmp");
					JSONUtil.printToHTML(response, result);
					return;
				}
				//大小限制
				if (uploadFile.getSize()>(1024*1024*10)) {
					result.put("msg","上传失败,上传文件过大，请确保上传的文件小于10M");
					JSONUtil.printToHTML(response, result);
					return;
				}
			}
			
			//处理kindeditor能上传的视频格式.ogg\mp4\flv\webm
			if (!StringUtil.isEmpty(kindeditor) && type==4) {
				//定义容许上传的文件扩展名
				if (!Arrays.<String>asList("gif,jpg,jpeg,png,bmp,swf,flv,mp4,webm,ogg".split(",")).contains(ext.toLowerCase())) {
					response.setContentType("text/html;charset=UTF-8");
					response.getWriter().println("上传失败,不支持该格式文上传，请确保格式为：gif,jpg,jpeg,png,bmp,swf,flv,mp4,webm,ogg");
					return;
				}
				//大小限制
				if (uploadFile.getSize()>(1024*1024*20)) {
					response.setContentType("text/html;charset=UTF-8");
					response.getWriter().println("上传失败,上传文件过大，请确保上传的文件小于20M");
					return;
				}
			}
			
			// 上传到服务器文件名字 ,前缀区分是哪条新闻的
			long currentTimeMillis = System.currentTimeMillis();
			String name = currentTimeMillis+ "." + ext;
			// 原图文件名
			String originalName = currentTimeMillis+ "_original" + "." + ext;
			
			if (type!=null && type==4) {//其他文件加上前缀标识为新闻内容的文件
				name =  tempId+"_"+System.currentTimeMillis()+ "." + ext;
			}
			
			// 保存文件路径 
			String filePath =httpSession.getServletContext().getRealPath("/") + siteFilePath;

			// 文件不存在先创建
			File creatfile = new File(filePath);
			if (!creatfile.exists()) {
				creatfile.mkdirs();
			}
			
			String newFile=filePath + name;
			String originalFile = filePath + originalName;
			boolean isExistsOriginal = false; // 是否已保存原图
			if (type==1 && uploadFile.getSize()>1024*50 ) {//图片列表的图片压缩
				//ImageUitl.maxWidthHeight2(is, file, 1366, 900);
				ImageUitl.imageZip(is, newFile, null, 1440, 900, true);
                originalFile = newFile;
				isExistsOriginal = true;
			}else if (type==2 && uploadFile.getSize()>1024*50 ) {//缩略图压缩
				//ImageUitl.maxWidthHeight2(is, file, 1366, 900);
				ImageUitl.imageZip(is, newFile, originalFile, 600, 450, false);
				isExistsOriginal = true;
			}else if (type==3) {//新闻附件不需要压缩
				FileUtil.copyFile(is, new File(newFile));
			}else if (Arrays.<String>asList("gif,jpg,jpeg,png,bmp".split(",")).contains(ext) && uploadFile.getSize()>1024*50 ) {//其他图片
				//ImageUitl.maxWidthHeight2(is, file, 1366, 900);
				ImageUitl.imageZip(is, newFile, null, 1440, 900, true);
                originalFile = newFile;
				isExistsOriginal = true;
			}else{//其他文件
				FileUtil.copyFile(is, new File(newFile));
			}
			File file = new File(newFile);
			
			image.setType(type);
			image.setArticleId(articleId);
			image.setTempId(tempId);
			image.setCreateTime(new Date());
			image.setSize(file.length());
			//文件路径,从根目录算起
			image.setPath("/" + siteFilePath + name);
			image.setFileName(fileName);//文件名
			
			Integer id=(Integer) imageService.save(image);
			if (id!=null&&id>0) {
				image.setId(id);
			}
			Map<String, Object> imageMap = ListUtils.ObjectToMap(image);
			if( isExistsOriginal ){
				imageMap.put("originalPath", "/" + siteFilePath +originalName);
			}else{
                imageMap.put("originalPath", image.getPath());
            }
			result.put("data", imageMap);//返回的保存文件信息
			result.put("code", Init.SUCCEED);
			result.put("msg", "上传成功");
			
			if (!StringUtil.isEmpty(kindeditor) && "1".equals(kindeditor)) {//如果是kindeditor插件上传 的文件返回数据格式处理
				result=new HashMap<String, Object>();
				result.put("url", image.getPath());
				result.put("msg", "上传成功");
				result.put("error", 0);
			}
		} catch (Exception e) {
			if (!StringUtil.isEmpty(kindeditor) && "1".equals(kindeditor)) {//如果是kindeditor插件上传 的文件返回数据格式处理
				result=new HashMap<String, Object>();
				result.put("msg", "上传失败");
				result.put("error", 1);
			}
			e.printStackTrace();
		}
		
		JSONUtil.printToHTML(response, result);
	}

	@RequestMapping("upload")
	public void upload(HttpServletRequest request, HttpServletResponse response, HttpSession session
			, @RequestParam("file") MultipartFile uploadFile){
		Map<String, Object> result=new HashMap<String, Object>();//最后输出的json数据
		result.put("code", -1);//上传成功或失败的返回标志
		result.put("data", "");		  //保存成功后返回的文件记录信息
		result.put("msg", "上传失败");		//消息
		Integer siteId = siteService.getSiteId(request);
		String siteFilePath = "upload/" + siteId + "/";// 网站上传目录
		try {
			//上传的图片名
			String fileName = uploadFile.getOriginalFilename();
			//输入流
			InputStream is = uploadFile.getInputStream();
			// 获取文件后缀名
			String ext = RegexUtil.parse("\\.(\\w+)", fileName);
			if (!Arrays.<String>asList("gif,jpg,jpeg,png,bmp".split(",")).contains(ext.toLowerCase())) {
				result.put("msg","上传失败,不支持该格式文上传，请确保格式为：gif,jpg,jpeg,png,bmp");
				JSONUtil.printToHTML(response, result);
				return;
			}
			//大小限制
			if (uploadFile.getSize()>(1024*1024*10)) {
				result.put("msg","上传失败,上传文件过大，请确保上传的文件小于10M");
				JSONUtil.printToHTML(response, result);
				return;
			}
			// 上传到服务器文件名字 ,前缀区分是哪条新闻的
			long currentTimeMillis = System.currentTimeMillis();
			String name = currentTimeMillis+ "." + ext;
			// 保存文件路径
			String filePath = session.getServletContext().getRealPath("/") + siteFilePath;
			// 文件不存在先创建
			File creatfile = new File(filePath);
			if (!creatfile.exists()) creatfile.mkdirs();
			String newFile=filePath + name;
			File file = new File(newFile);
			FileUtil.copyFile(is, file);
			result.put("data", "/" + siteFilePath + name);//返回的保存文件信息
			result.put("code", 0);
			result.put("msg", "上传成功");
		}catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}

	/**
	 * 删除文件,公用的删除文件Action
	 * 不存在Image表的只要传对路径一样可以删除，如果存在image表记录的会连记录一起删除掉
	 * @param path 需要删除的文件路径，从根目录算起
	 * @throws IOException
	 */
	@RequestMapping(value = "deleteByPath")
	public void deleteByPath(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "path") String path) {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "删除失败");
		try {
			String pathname =session.getServletContext().getRealPath("/")+path;
			File file = new File(pathname);
			if (file.exists()) {
				FileUtil.delete(file);
			}
			imageService.deleteByPath(path);
			result.put("code", Init.SUCCEED);
			result.put("msg", "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	

	/**
	 * 根据文件记录ID删除文件
	 * @author lifq
	 * @param id 文件记录ID
	 */
	@RequestMapping(value = "deleteById")
	public void deleteById(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "id") Integer id) {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "删除失败");
		try {
			Map<String, Object> image=imageService.load(id);
			if (image != null) {
				String pathname =session.getServletContext().getRealPath("/")+image.get("path");
				File file = new File(pathname);
				if (file.exists()) {
					FileUtil.delete(file);
				}
				imageService.delete(id+"");
				result.put("code", Init.SUCCEED);
				result.put("msg", "删除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	/**
	 * 根据文件记录tempId删除文件
	 * @author lifq
	 * @param tempId 文件记录tempId
	 */
	@RequestMapping(value = "deleteByTempId")
	public void deleteByTempId(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "tempId") String tempId) {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "删除失败");
		try {
			Map<String, Object> image=imageService.loadByTempId(tempId);
			if (image != null) {
				String pathname =session.getServletContext().getRealPath("/")+image.get("path");
				File file = new File(pathname);
				if (file.exists()) {
					FileUtil.delete(file);
				}
				imageService.deleteByTempId(tempId);
				result.put("code", Init.SUCCEED);
				result.put("msg", "删除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	/**
	 * 根据文件记录文章Id删除文件
	 * @author lifq
	 * @param articleId 文件记录articleId
	 * @param type		分类：1、图片类型的新闻图片文件； 2、新闻的缩略图文件； 3、新闻的附近文件，4、其他文件，不传表示删除该新闻的所有文件：
	 */
	@RequestMapping(value = "deleteByArticleId")
	public void deleteByArticleId(HttpServletRequest request,HttpServletResponse response,HttpSession session,
			@RequestParam(value = "articleId") String articleId,Integer type) {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "删除失败");
		try {
			List<Map<String, Object>> imageList=imageService.find(Integer.parseInt(articleId),type, null, null);
			if (imageList != null&&imageList.size()>0) {
				for (Map<String, Object> image : imageList) {
					String pathname =session.getServletContext().getRealPath("/")+image.get("path");
					File file = new File(pathname);
					if (file.exists()) {
						FileUtil.delete(file);
					}
				}
				imageService.deleteByArticleId(articleId);
				result.put("code", Init.SUCCEED);
				result.put("msg", "删除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 根据Id获取一条文件记录
	 * @author lifq
	 * @param id
	 */
	@RequestMapping(value = "loadById")
	public void loadById(HttpServletRequest request,HttpServletResponse response,HttpSession session,Integer id){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("data", null);
		result.put("msg", "");
		try {
			Map<String, Object> image=imageService.load(id);
			result.put("data", image);
			result.put("code", Init.SUCCEED);
		} catch (Exception e) {
			result.put("msg", "加载异常");
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	/**
	 * 根据tempId获取一条文件记录
	 * @author lifq
	 * @param id
	 */
	@RequestMapping(value = "loadByTempId")
	public void loadByTempId(HttpServletRequest request,HttpServletResponse response,HttpSession session,String tempId){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("data", null);
		result.put("msg", "");
		try {
			Map<String, Object> image=imageService.loadByTempId(tempId);
			result.put("data", image);
			result.put("code", Init.SUCCEED);
		} catch (Exception e) {
			result.put("msg", "加载异常");
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	/**
	 * 根据path获取一条文件记录
	 * @author lifq
	 * @param 
	 */
	@RequestMapping(value = "loadByPath")
	public void loadByPath(HttpServletRequest request,HttpServletResponse response,HttpSession session,String path){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("data", null);
		result.put("msg", "");
		try {
			Map<String, Object> image=imageService.loadByPath(path);
			result.put("data", image);
			result.put("code", Init.SUCCEED);
		} catch (Exception e) {
			result.put("msg", "加载异常");
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	/**
	 * 根据id获取一条文件记录
	 * @author lifq
	 * @param id 文件ID
	 */
	@RequestMapping(value = "toEdit")
	public String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id){
		modelMap.put("image", this.imageService.load(id));
		return "site/article/editImg";
	}
	
	

	/**
	 * 更新文件链接和描述信息
	 * @param id 文件Id
	 * @param href 链接地址
	 * @param description 描述
	 * @return
	 */
	@RequestMapping(value = "update")
	public void update(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap
            ,Integer id,String href,String description, Integer stars){
		Map<String, Object> image=new HashMap<String, Object>();
		image.put("id", id);
		image.put("href", href);
		image.put("description", description);
        image.put("stars", stars);
		int result= this.imageService.update(image);
		JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
	}
	
	
	/**
	 * 获取图片列表数据
	 * @author lifq
	 * @param tempId	临时Id
	 * @param path		路径
	 * @param articleId 文章ID
	 * @param type 分类：1、图片类型的新闻图片文件； 2、新闻的缩略图文件； 3、新闻的附近文件，4、其他文件
	 */
	@RequestMapping(value = "list")
	public void list(HttpServletRequest request,HttpServletResponse response,HttpSession session,
			String tempId,String path,Integer articleId,Integer type){
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("data", null);
		result.put("msg", "");
		try {
			List<Map<String, Object>> imageList=imageService.find(articleId ,type, tempId, path);
			result.put("data", imageList);
			result.put("code", Init.SUCCEED);
		} catch (Exception e) {
			result.put("msg", "加载异常");
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 下载文件
	 * @param path 文件路径，从根目录算起
	 * @throws IOException
	 */
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "path") String path) throws IOException {
		String pathname = session.getServletContext().getRealPath("/")+path;
		String source_name="";
		if (path.lastIndexOf("/")!=-1) {
			source_name=path.substring(path.lastIndexOf("/")+1, path.length());
		}else{
			source_name=path;
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
	
	/**
	 * 删除文件,公用的删除文件Action
	 * 不存在Image表的只要传对路径一样可以删除，如果存在image表记录的会连记录一起删除掉
	 * @param path 需要删除的文件路径，从根目录算起
	 * @throws IOException
	 */
	@RequestMapping(value = "deleteSmallPath")
	public void deleteSmallPath(HttpServletRequest request,HttpServletResponse response,HttpSession session,@RequestParam(value = "path") String path,Integer articleId) {
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("code", Init.FAIL);
		result.put("msg", "删除失败");
		try {
			if (articleId != null &&articleId>0) {
				Map<String, Object> article=new HashMap<String, Object>();
				article.put("id", articleId);
				article.put("smallPicUrl", "");
				articleService.update(article);
			}
			
			String pathname =session.getServletContext().getRealPath("/")+path;
			File file = new File(pathname);
			if (file.exists()) {
				FileUtil.delete(file);
			}
			imageService.deleteByPath(path);
			
			result.put("code", Init.SUCCEED);
			result.put("msg", "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printToHTML(response, result);
	}
	
	/**
	 * 返回kindeditor的文件管理列表
	 * @author lifq
	 * @param dir  目录名 "image", "flash", "media", "file"  kindeditor自动传入的默认目录名
	 * @param path 目录相对路径								  kindeditor自动传入的默认相对路径
	 * @param order 文件排序方式： NAME,SIZE,TYPE  			  kindeditor自动传入的默认排序方式
	 * @param tempId  //新闻tempId，传是标识新闻的文件
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping( value ="fileManager")
	public void fileManager(HttpServletRequest request,HttpServletResponse response,HttpSession session,
			String dir,
			String path,
			String order,
			@RequestParam(value = "tempId")String tempId){
		
		try {
			PrintWriter out=response.getWriter();
			
			//根目录路径，可以指定绝对路径，比如 /var/www/upload/
			String rootPath = request.getSession().getServletContext().getRealPath("/") + "upload/";
			//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/upload/
			String rootUrl  = request.getContextPath() + "/upload/";
			
			//图片扩展名
			String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

			String dirName = dir;//目录名
			if (dirName != null) {
				if(!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(dirName)){
					out.println("找不到目录.");
					return;
				}
				
				/*rootPath += dirName + "/";
				rootUrl  += dirName + "/";*/
					
				File saveDirFile = new File(rootPath);
				if (!saveDirFile.exists()) {
					saveDirFile.mkdirs();
				}
			}
			//根据path参数，设置各路径和URL
			String currentPath = rootPath + path;
			String currentUrl = rootUrl + path;
			String currentDirPath = path;
			String moveupDirPath = "";
			if (!"".equals(path)) {
				String str = currentDirPath.substring(0, currentDirPath.length() - 1);
				moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
			}
			
			//不允许使用..移动到上一级目录
			if (path.indexOf("..") >= 0) {
				out.println("不允许使用..移动到上一级目录.");
				return;
			}
			//最后一个字符不是/
			if (!"".equals(path) && !path.endsWith("/")) {
				out.println("参数有误.");
				return;
			}
			//目录不存在或不是目录
			File currentPathFile = new File(currentPath);
			if(!currentPathFile.isDirectory()){
				out.println("目录不存在");
				return;
			}
			
			//定义容许获取列表的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");
			extMap.put("flash", "swf,flv");
			extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
			
			//遍历目录取的文件信息
			List<Hashtable> fileList = new ArrayList<Hashtable>();
			if(currentPathFile.listFiles() != null) {
				for (File file : currentPathFile.listFiles()) {
					Hashtable<String, Object> hash = new Hashtable<String, Object>();
					String fileName = file.getName();
					if(file.isDirectory()&&!fileName.equals("applyFace")&&!fileName.equals("contact")) {
						hash.put("is_dir", true);
						hash.put("has_file", (file.listFiles() != null));
						hash.put("filesize", 0L);
						hash.put("is_photo", false);
						hash.put("filetype", "");
					} else if(file.isFile()){
						String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
						
						//因为所有个人文件度放在了一个目录上，所有读取图片或媒体列表时需要过滤掉不相关的文件
						if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt) || !fileName.startsWith(tempId+"_")){
							//不包含的扩展名不读放到列表
							continue;
						}
						hash.put("is_dir", false);
						hash.put("has_file", false);
						hash.put("filesize", file.length());
						hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
						hash.put("filetype", fileExt);
					}
					if(!fileName.equals("applyFace")&&!fileName.equals("contact")){
						hash.put("filename", fileName);
						hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
						fileList.add(hash);
					}
				}
			}
			if ("size".equals(order)) {
				Collections.sort(fileList, new SizeComparator());
			} else if ("type".equals(order)) {
				Collections.sort(fileList, new TypeComparator());
			} else if(fileList!=null&&fileList.size()>0){
				Collections.sort(fileList, new NameComparator());
			}
			JSONObject result = new JSONObject();
			result.put("moveup_dir_path", moveupDirPath);
			result.put("current_dir_path", currentDirPath);
			result.put("current_url", currentUrl);
			result.put("total_count", fileList.size());
			result.put("file_list", fileList);

			response.setContentType("application/json; charset=UTF-8");
			out.println(result.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @RequestMapping("/toClip")
    public String toClip(ModelMap map, Integer id){
        Map<String, Object> image = imageService.load(id);
        if(image != null){
            map.put("path", image.get("path"));
            map.put("id", id);
        }
        return "site/article/clipImg";
    }

    /**
     * 截图
     * @author lfq
     * @param imgSource 图片来源，相对路径，从根目录算起
     */
    @RequestMapping("clip")
    public void clip(HttpServletRequest request,HttpServletResponse response,String imgSource){
        Integer siteId= User.getCurrentSiteId(request);
        Map<String, Object> result=new HashMap<String, Object>();
        result.put("code", Init.SUCCEED);
        Integer imgWidth = (int) Double.parseDouble(request.getParameter("imgWidth"));
        Integer imgHeight = (int) Double.parseDouble(request.getParameter("imgHeight"));
        Integer imgX = (int) Double.parseDouble(request.getParameter("imgX"));
        Integer imgY = (int) Double.parseDouble(request.getParameter("imgY"));
        Integer selectorX = (int) Double.parseDouble(request.getParameter("selectorX"));
        Integer selectorY = (int) Double.parseDouble(request.getParameter("selectorY"));
        Integer cutterWidth = (int) Double.parseDouble(request.getParameter("cutterWidth"));
        Integer cutterHeight = (int) Double.parseDouble(request.getParameter("cutterHeight"));
        try {
            long time=(new Date()).getTime();
            imgSource=imgSource.substring(imgSource.lastIndexOf("/")+1,imgSource.length());
            String siteFilePath = "upload/" + siteId + "/";
            String savePath = request.getSession().getServletContext().getRealPath("/") + siteFilePath;
            String fromFile=savePath+imgSource;
            String newFile=imgSource.substring(0, imgSource.lastIndexOf("/")+1)+time+"."+ImageUitl.getFormat(fromFile);
            String toFile=savePath +newFile;
            String tempFile= ImageUitl.corp(fromFile, toFile, imgWidth, imgHeight, imgX, imgY, selectorX, selectorY, cutterWidth, cutterHeight);
            //删除临时文件
            File file=new File(tempFile);
            if (file.exists()&&file.isFile()) {
                file.delete();
            }
            result.put("url", "/" + siteFilePath + newFile);
            result.put("msg", "截取成功");
            System.out.println(toFile);
        } catch (Exception e) {
            result.put("code", Init.FAIL);
            result.put("msg", "截取失败");
            e.printStackTrace();
            //log.error(e.getClass().getCanonicalName()+":"+e.getMessage()+"\n截图异常",e);
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("/updateImg")
    public void updateImg(HttpServletResponse response, Integer id, String path){
        Map<String, Object> image = new HashMap<String, Object>(2);
        image.put("id", id);
        image.put("path", path);
        imageService.update(image);
        JSONUtil.print(response, "ok");
    }
	
	@SuppressWarnings("rawtypes")
	public class NameComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
			}
		}
	}
	@SuppressWarnings("rawtypes")
	public class SizeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
					return 1;
				} else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}
	@SuppressWarnings("rawtypes")
	public class TypeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
			}
		}
	}
}

