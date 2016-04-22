package com.site.action;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.FileUtil;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.util.ZipUtils;
import com.site.service.SiteService;

import de.schlichtherle.util.zip.ZipOutputStream;

@Controller
@RequestMapping(value="fileManager")
public class FileManager {
	@Autowired
	private SiteService siteService;

	/**
	 * 样式文件夹
	 */
	public static final String STYLE_DIR = "/template/";

	/**
	 * 页面文件夹
	 */
	public static final String PAGE_DIR = "/WEB-INF/page/template/";

	/**
	 * 是否为正确的访问权限目录
	 * @param request
	 * @param dir
     * @return
     */
	private boolean isProperDir(HttpServletRequest request, String dir){
		Integer siteId = User.getCurrentSiteId(request);
		Map<String, Object> site = siteService.load(siteId);
		String directory = (site.get("directory") + "").trim();
		dir = ("/" + dir.trim()).replaceAll("/+", "/");
		return dir.startsWith(STYLE_DIR + directory) || dir.startsWith(PAGE_DIR + directory);
	}

	/**
	 * 进入模版文件管理界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId>0) {
			Map<String, Object> site=siteService.load(siteId);
			if (site!=null) {
				String directory=site.get("directory")+"";
				String root=request.getSession().getServletContext().getRealPath("/");
				File d=new File(root+"/template/"+directory);
				if (StringUtil.isEmpty(directory)) {
					Map<String, Object> siteTemp=new HashMap<String, Object>();
					siteTemp.put("id", site.get("id"));
					String newDirectory=this.newTemplateName(request,null);
					if (!StringUtil.isEmpty(newDirectory)) {
						siteTemp.put("directory",newDirectory );
						this.siteService.update(siteTemp);
					}
				}else if (!StringUtil.isEmpty(directory)&&(!d.exists()||!d.isDirectory())) {
					Map<String, Object> siteTemp=new HashMap<String, Object>();
					siteTemp.put("id", site.get("id"));
					String newDirectory=this.newTemplateName(request,directory);
					if (!StringUtil.isEmpty(newDirectory)) {
						siteTemp.put("directory",newDirectory );
						this.siteService.update(siteTemp);
					}
				}
			}
		}
		return "site/fileManager/index";
	}
	/**
	 * 进入编辑文件界面
	 * @author lifq
	 * @param id 文件相对路径
	 * @return
	 */
	@RequestMapping(value="toEdit")
	public String toEdit(HttpServletRequest request,ModelMap modelMap,String id){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId>0) {
			String root=request.getSession().getServletContext().getRealPath("/");
			if (!StringUtil.isEmpty(id) && isProperDir(request, id)) {
				File file=new File(root+id);
				if (file.exists()) {
					 modelMap.put("id", id);
					 modelMap.put("name", file.getName());
					 String content=FileUtil.getFileText(root+id);
					 modelMap.put("content", content);
				}
			}
		}
		return "site/fileManager/edit";
	}
	/**
	 * 进入新增文件夹界面
	 * @author lifq
	 * @param id 文件夹上级目录相对路径
	 * @return
	 */
	@RequestMapping(value="toAdd")
	public String toAdd(HttpServletRequest request,ModelMap modelMap,String id){
		modelMap.put("id", id);
		return "site/fileManager/add";
	}
	/**
	 * 保存文件 [安全问题，关闭]
	 * @author lifq
	 * @param id 文件相对路径
	 * @param content 文件内容
	 * @return
	 */
	@Deprecated
	//@RequestMapping(value="save")
	public void save(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String id,String content){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId>0) {
			String root=request.getSession().getServletContext().getRealPath("/");
			if (StringUtil.isEmpty(content)) {
				content="";
			}
			if (!StringUtil.isEmpty(id) && isProperDir(request, id)) {
				File file=new File(root+id);
				if (file.exists()) {
					boolean result= FileUtil.writeToFile(root+id,content,false);
					JSONUtil.print(response, result?Init.SUCCEED:Init.FAIL);
				}
			}
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	/**
	 * 新增新文件
	 * @author lifq
	 * @param id 上级文件相对路径
	 * @param name 新增的文件夹名
	 * @return 新增程返回succeed,新增失败返回fail,目录已存在返回false
	 */
	@RequestMapping(value="add")
	public void add(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String id,String name){
		Integer siteId=User.getCurrentSiteId(request);
		String code=Init.FAIL;
		try {
			if (siteId>0) {
				String root=request.getSession().getServletContext().getRealPath("/");
				if (!StringUtil.isEmpty(id)&&!StringUtil.isEmpty(name) && isProperDir(request, id)) {
					File file=new File(root+id);
					if (file.exists()&&file.isDirectory()) {
						File dir=new File(root+id+"/"+name+"/");
						if (dir.exists()&&dir.isDirectory()) {
							code=Init.FALSE;
						}else{
							boolean succeed= dir.mkdirs();
							code=succeed?Init.SUCCEED:Init.FALSE;
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
		JSONUtil.print(response, code);
	}
	
	
	/**
	 * 获取模版目录
	 * @author lifq
	 * @param openDirectory 默认展开的目录（根目录默认展开）
	 */
	@RequestMapping(value="loadDirectory")
	public void loadDirectory(HttpServletRequest request,HttpServletResponse response,String openDirectory){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId>0) {
			Map<String, Object> site=siteService.load(siteId);
			String directoryName=site.get("directory")+"";
			String root=request.getSession().getServletContext().getRealPath("/");
			File staticFile=new File(root+"template/"+directoryName);
			File pageFile=new File(root+"WEB-INF/page/template/"+directoryName);
			List<Map<String, String>> directoryList=new ArrayList<Map<String,String>>();
			
			if (staticFile.exists()&&staticFile.isDirectory()) {
				List<Map<String, String>> tempList=this.getDirectoryList(root,staticFile.getPath(),true,openDirectory);
				if (tempList.size()>0) {
					directoryList.addAll(tempList);
				}
			}
			
			if (pageFile.exists()&&pageFile.isDirectory()) {
				List<Map<String, String>> tempList=this.getDirectoryList(root,pageFile.getPath(),true,openDirectory);
				if (tempList.size()>0) {
					directoryList.addAll(tempList);
				}
			}
			JSONUtil.printToHTML(response, directoryList);
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	
	
	/**
	 *  获取制定目录下的文件列表
	 * @author lifq
	 */
	@RequestMapping(value="loadFileList")
	public void loadFileList(HttpServletRequest request,HttpServletResponse response,String directory){
		List<Map<String, Object>> fileList=new ArrayList<Map<String,Object>>();
		if (!StringUtil.isEmpty(directory) && isProperDir(request, directory)) {
			
			String root=request.getSession().getServletContext().getRealPath("/");
			File file=new File(root+directory);
			if (file.exists()&&file.isDirectory()&&file.listFiles().length>0) {
				for (int i = 0; i < file.listFiles().length; i++) {
					File subFile=file.listFiles()[i];
					Map<String, Object> fileMap=new HashMap<String, Object>();
					fileMap.put("id", subFile.getPath().replace(root, "\\").replace("\\", "/"));
					fileMap.put("name", subFile.getName());
					fileMap.put("pid", subFile.getParent().replace(root, "\\").replace("\\", "/"));
					if (subFile.isFile()) {
						fileMap.put("type", "1");
						fileList.add(fileMap);
					}else if(!".svn".equals(subFile.getName())){
						fileMap.put("type", "0");
						fileList.add(fileMap);
					}
					
				}
			}
		}
		JSONUtil.printToHTML(response, fileList);
	}
	
	/**
	 * 检索文件是否存在
	 * @author lifq
	 * @param filePath
	 */
	@RequestMapping(value="checkExists")
	public void checkExists(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String filePath){
		String root=request.getSession().getServletContext().getRealPath("/");
		File file=new File(root+filePath);
		if(!isProperDir(request, filePath)) return;
		if (file.exists()) {
			JSONUtil.print(response, Init.TRUE);
		}else{
			JSONUtil.print(response, Init.FALSE);
		}
	}

	/**
	 * 上传文件
	 * @author lifq
	 * @param modelMap
	 * @param directory 上传到的文件目录 相对路径
	 */
	@RequestMapping(value="upload")
	public void upload(HttpServletRequest request,HttpServletResponse response,HttpSession httpSession,ModelMap modelMap,String directory){
		Map<String, Object> result=new HashMap<String, Object>();//最后输出的json数据
		result.put("code", "fail");
		result.put("msg", "上传失败");
		if( !isProperDir(request, directory) ){
			JSONUtil.printToHTML(response, result);
			return;
		}
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 这里是表单的名字在swfupload.js中this.ensureDefault("file_post_name","filedata");
			CommonsMultipartFile uploadFile = (CommonsMultipartFile) multipartRequest.getFile("attachmentTempPath");
			String fileName = uploadFile.getOriginalFilename();
			InputStream is = uploadFile.getInputStream();
			String filePath =httpSession.getServletContext().getRealPath("/")+directory;
			// 文件不存在先创建
			File creatfile = new File(filePath);
			if (!creatfile.exists()) {
				creatfile.mkdirs();
			}
			File file = new File(filePath +"/"+ fileName);
			if (!file.exists()) {
				FileUtil.copyFile(is, file);
				if(fileName.endsWith(".jsp")){ // 校验jsp页面是否有非法代码
					String code = "";
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line;
					while ((line = reader.readLine()) != null)
						code += line;
					reader.close();
					// 校验代码
					/*if(Pattern.matches("(?:.|\\n)*(?:<%[^@](?:.|\\n)*%>)(?:.|\\n)*", code)){
						file.delete();
						result.put("code", Init.FALSE);
						result.put("msg", "上传失败，非法文件");
						JSONUtil.printToHTML(response, result);
						return;
					}*/
				}
				
				result.put("code", "succeed");
				result.put("msg", "上传成功");
				result.put("url", directory+"/"+fileName);
				result.put("error", 0);
			}else{
				result.put("code", Init.FALSE);
				result.put("msg", "文件["+fileName+"]上传失败，已存在");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "上传失败");
			result.put("error", 1);
		}
		JSONUtil.printToHTML(response, result);
	}
	

	/**
	 * 导出文件/文件夹为zip包
	 * directory和ids必须有一个有值
	 * @author lifq
	 * @param directory 非必须，导出文件所在的目录 ，相对路径
	 * @param ids		非必须，文件地址，多个用逗号隔开，相对路径
	 * @throws Exception 
	 * @throws IOException 
	 */
	@RequestMapping(value="download")
	public void download(HttpServletRequest request,HttpServletResponse response,String directory,String ids) throws IOException, Exception{
		response.setContentType("application/zip;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-Disposition", "attachment;filename="+(new Date()).getTime()+".zip");
		String root=request.getSession().getServletContext().getRealPath("/");
		if( !isProperDir(request, directory)) return;
		if (!StringUtil.isEmpty(directory)&&StringUtil.isEmpty(ids)) {//导出整个目录
			File file=new File(root+directory);
			if (file.exists()&&file.isDirectory()) {
				ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
				ZipUtils.newZip(null, zos, file);
				zos.close();
			}
			
		}else if (!StringUtil.isEmpty(ids)) { //导出指定的文件列表
			String [] array=ids.split(",");
			if (array!=null&&array.length>0) {
				File [] files=new File[array.length];
				for (int i = 0; i < array.length; i++) {
					File file=new File(root+array[i]);
					files[i]=file;
				}
				ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
				ZipUtils.newZip(null, zos, files);
				zos.close();
			}
		}else{//导出所有
			Integer siteId=User.getCurrentSiteId(request);
			Map<String, Object> site=siteService.load(siteId);
			String dir=site.get("directory")+"";
			if (!"".equals(dir)) {
				File file1=new File(root+"/template/"+dir);
				File file2=new File(root+"/WEB-INF/page/template/"+dir);
				ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
				if (file1.exists()&&file1.isDirectory()) {
					ZipUtils.newZip(dir+"_1", zos, file1);
				}
				if (file2.exists()&&file2.isDirectory()) {
					ZipUtils.newZip(dir+"_2", zos, file2);
				}
				zos.close();
			}
		}
		
	}
	
	/**
	 * 删除文件    成功返回：succeed,删除失败返回：fail,删除目录不为空则返回false
	 * @param filePath
	 * @param type 1文件，0文件夹
	 */
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String filePath,String type){
		String root=request.getSession().getServletContext().getRealPath("/");
		String result=Init.FAIL;
		if( !isProperDir(request, filePath) ){
			JSONUtil.print(response, result);
			return;
		}
		if (!StringUtil.isEmpty(filePath)&&!StringUtil.isEmpty(type)) {
			File file=new File(root+filePath);
			if (file.exists()) {
				if ("1".equals(type)&&file.isFile()) {
					boolean succeed=file.delete();
					result=succeed?Init.SUCCEED:Init.FAIL;
				}else if ("0".equals(type)&&file.isDirectory()) {
					if (file.listFiles().length==0) {
						boolean succeed=file.delete();
						result=succeed?Init.SUCCEED:Init.FAIL;
					}else{
						result=Init.FALSE;//目录不为空时不容许删除
					}
				}
			}
		}
		JSONUtil.print(response, result);
	}
	
	/**
	 * 获取站点的模版文件名
	 * @author lifq
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	private String newTemplateName(HttpServletRequest request,String directory){
		try {
			String root=request.getSession().getServletContext().getRealPath("/");
			String dirName=StringUtil.isEmpty(directory)?User.getCurrentSiteId(request)+"":directory;
			File f=new File(root+"/template/"+dirName);
			File pageFile=new File(root+"/WEB-INF/page/template/"+dirName);
			if (!f.exists()&&!pageFile.exists()) {
				f.mkdir();
				File f1=new File(root+"/template/"+dirName+"/images");
				File f2=new File(root+"/template/"+dirName+"/css");
				File f3=new File(root+"/template/"+dirName+"/js");
				f1.mkdir();
				f2.mkdir();
				f3.mkdir();
				pageFile.mkdir();
			}else{
				File d=new File(root+"/template");
				dirName="0";
				if (d.exists()&&d.listFiles().length>0) {
					for (int i = 0; i < d.listFiles().length; i++) {
						File file=d.listFiles()[i];
						if (file.isDirectory()&&file.getName().compareToIgnoreCase(dirName)>0) {
							dirName=""+(Integer.parseInt("0"+file.getName())+1);
						}
					}
				}
				f=new File(root+"/template/"+dirName);
				f.mkdir();
				File f1=new File(root+"/template/"+dirName+"/images");
				File f2=new File(root+"/template/"+dirName+"/css");
				File f3=new File(root+"/template/"+dirName+"/js");
				f1.mkdir();
				f2.mkdir();
				f3.mkdir();
				pageFile=new File(root+"/WEB-INF/page/template/"+dirName);
				pageFile.mkdir();
			}
			return  dirName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据路径得到文件夹列表
	 * @author lifq
	 * @param path
	 * @return
	 */
	private List<Map<String, String>> getDirectoryList(String root,String path,boolean isroot,String openDirectory){
		List<Map<String, String>> directoryList=new ArrayList<Map<String,String>>();
		File directory=new File(path);
		if (directory.exists()&&directory.isDirectory()&&!".svn".equals(directory.getName())) {
			Map<String, String> fileMap=new HashMap<String, String>();
			fileMap.put("id", directory.getPath().replace(root, "\\").replace("\\", "/"));		//文绝对路径
			fileMap.put("pid", directory.getParent().replace(root, "\\").replace("\\", "/"));	
			fileMap.put("name", directory.getName());	//文件名
			fileMap.put("type", "0");					//type:0标识为文件夹，1标识为文件
			if (isroot||((!StringUtil.isEmpty(openDirectory))&&openDirectory.equals(fileMap.get("id")+""))) {//根目录和选择的目录展开
				fileMap.put("open", "true");
			}
			directoryList.add(fileMap);
			if (directory.listFiles().length>0) {
				for (int i = 0; i < directory.listFiles().length; i++) {
					List<Map<String, String>> tempList= this.getDirectoryList(root,directory.listFiles()[i].getPath(),false,openDirectory);
					if (tempList.size()>0) {
						directoryList.addAll(tempList);
					}
				}
			}
		}else if (directory.exists()&&directory.isFile()) {
			Map<String, String> fileMap=new HashMap<String, String>();
			fileMap.put("id", directory.getPath().replace(root, "\\").replace("\\", "/"));
			fileMap.put("pid", directory.getParent().replace(root, "\\").replace("\\", "/"));
			fileMap.put("name", directory.getName());
			fileMap.put("type", "1");
			directoryList.add(fileMap);
		}
		return directoryList;
	}
}
