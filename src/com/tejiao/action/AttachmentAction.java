package com.tejiao.action;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.FileUtil;
import com.base.util.JSONUtil;
import com.base.util.RegexUtil;
import com.base.vo.PageList;
import com.site.service.SiteService;
import com.tejiao.model.Attachment;
import com.tejiao.model.Parent;
import com.tejiao.service.AttachmentService;
import com.tejiao.service.ParentService;
import com.tejiao.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 学生档案管理
 * Created by dzf on 2016/2/23.
 */
@Controller
@RequestMapping("/attachment")
public class AttachmentAction {

    @Autowired
    private SiteService siteService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ParentService parentService;

    /**
     * 网站前端上传档案
     * @param uploadFile
     * @param declareId 申报学生id
     */
    @RequestMapping("upload")
    public void upload(HttpServletRequest request, HttpServletResponse response, HttpSession session
            , @RequestParam("file") MultipartFile uploadFile, Integer declareId){
        Map<String, Object> result = new HashMap<String, Object>();//最后输出的json数据
        result.put("code", -1);//上传成功或失败的返回标志
        result.put("data", "");		  //保存成功后返回的文件记录信息
        result.put("msg", "上传失败");		//消息
        Integer siteId = siteService.getSiteId(request);
        String siteFilePath = "upload/" + siteId + "/";// 网站上传目录
        try {
            Parent parent = parentService.getCurrent(session);
            if(parent == null) return;
            //上传的文件名
            String fileName = uploadFile.getOriginalFilename();
            //输入流
            InputStream is = uploadFile.getInputStream();
            // 获取文件后缀名
            String ext = RegexUtil.parse("\\.(\\w+)", fileName);
            if (!Arrays.<String>asList("gif,jpg,jpeg,png,bmp,pdf,7z,rar,zip,doc,docx,xls".split(",")).contains(ext.toLowerCase())) {
                result.put("msg","上传失败,不支持该格式文上传，请确保格式为：gif,jpg,jpeg,png,bmp,pdf,7z,rar,zip,doc,docx,xls");
                JSONUtil.printToHTML(response, result);
                return;
            }
            //大小限制
            if (uploadFile.getSize()>(1024*1024*20)) {
                result.put("msg","上传失败,上传文件过大，请确保上传的文件小于20M");
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
            // 保存学生档案信息
            Attachment attachment = new Attachment();
            attachment.setPath("/" + siteFilePath + name);
            attachment.setExt(ext);
            attachment.setName(fileName);
            attachment.setSiteId(siteId);
            attachment.setDeclareId(declareId);
            attachmentService.save(attachment);
            result.put("data", "/" + siteFilePath + name);//返回的保存文件信息
            result.put("code", 0);
            result.put("msg", "上传成功");
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            JSONUtil.printToHTML(response, result);
        }
    }

    /**
     * 前端加载学生档案信息
     * @param declareId 申报学生id
     */
    @RequestMapping("listDeclareFile")
    public void listDeclareFile(HttpServletResponse response, HttpServletRequest request, HttpSession session
            , Integer pageSize, Integer currentPage, Integer declareId, String keyword){
        pageSize = Init.getPageSize(pageSize);
        currentPage = Init.getCurrentPage(currentPage);
        Parent parent = parentService.getCurrent(session);
        if(parent == null) return;
        PageList pageList = attachmentService.getWebPageList(currentPage, pageSize, parent.getSiteId(), declareId, keyword);
        JSONUtil.printToHTML(response, pageList);
    }

    /**
     * 删除
     */
    @RequestMapping("webDelete")
    public void webDelete(HttpServletResponse response, HttpServletRequest request, HttpSession session, Integer id){
        Map<String, Object> result = new HashMap<String, Object>();//最后输出的json数据
        result.put("code", -1);//上传成功或失败的返回标志
        Parent parent = parentService.getCurrent(session);
        int count = attachmentService.delete(parent.getSiteId(), id);
        if(count > 0) result.put("code", 0);
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 删除
     */
    @RequestMapping("delete")
    public void delete(HttpServletResponse response, HttpServletRequest request, HttpSession session, Integer id){
        Map<String, Object> result = new HashMap<String, Object>();//最后输出的json数据
        result.put("code", -1);//上传成功或失败的返回标志
        Integer siteId = User.getCurrentSiteId(request);
        int count = attachmentService.delete(siteId, id);
        if(count > 0) result.put("code", 0);
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 后台 档案管理首页
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap modelMap){
        Integer siteId = User.getCurrentSiteId(request);
        modelMap.put("schoolList", schoolService.getBySiteId(siteId));
        return "tejiao/attachment/index";
    }

    /**
     * 后台加载档案
     */
    @RequestMapping("list")
    public void list(HttpServletResponse response, HttpServletRequest request, Integer pageSize, Integer currentPage
            , String keyword, Integer schoolId){
        Integer siteId = User.getCurrentSiteId(request);
        pageSize = Init.getPageSize(pageSize);
        currentPage = Init.getCurrentPage(currentPage);
        PageList pageList = attachmentService.getPageList(currentPage, pageSize, siteId, schoolId, keyword);
        JSONUtil.printToHTML(response, pageList);
    }

}
