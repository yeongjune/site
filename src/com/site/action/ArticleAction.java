package com.site.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.site.model.Article;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.DataUtil;
import com.base.util.FileUtil;
import com.base.util.ImageUitl;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.service.ArticleService;
import com.site.service.ColumnService;
import com.site.service.ImageService;
import com.site.service.RecommendService;
import com.site.service.SiteService;
import com.site.vo.ArticleSearchVo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("article")
public class ArticleAction {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ColumnService columnService;
	
	@Autowired
	private RecommendService recommendService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private SiteService siteService;
	
	/**
	 * 跳转到新闻管理首页
	 * @author lifqiang
	 * @param map
	 * @param columnId 栏目ID
	 * @return
	 */
	@RequestMapping("index")
	public String index(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer columnId){
		Integer siteId = User.getCurrentSiteId(request);
		map.put("siteId", siteId);
		map.put("site", siteService.load(siteId));
		map.put("isCheck", 0);
		Map<String, Object> user = User.getCurrentUser(request);
		if("superAdmin".equals(user.get("account"))){
			map.put("isCheck", 1);
		}
		return "site/article/index";
	}
	/**
	 * 有审核功能的新闻管理首页
	 * @param map
	 * @param request
	 * @param response
	 * @param columnId
	 * @return
	 */
	@RequestMapping("index1")
	public String index1(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer columnId){
		Integer siteId = User.getCurrentSiteId(request);
		map.put("siteId", siteId);
		map.put("site", siteService.load(siteId));
		map.put("isCheck", 1);
		return "site/article/index";
	}
	/**
	 * 新闻列表
	 * @author lifqiang
	 * @param columnId
	 * @param currentPage
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Integer columnId, Integer currentPage, Integer pageSize,Integer sortType){
		try {
			Integer siteId =  User.getCurrentSiteId(request);
			if (siteId!=null&&siteId.intValue()!=0) {
				currentPage = Init.getCurrentPage(currentPage);
				pageSize = Init.getPageSize(pageSize);
				String keyword = request.getParameter("keyword");
				ArticleSearchVo searchVo=new ArticleSearchVo();
				searchVo.setKeyWord(keyword);
				searchVo.setColumnId(columnId);
				searchVo.setSiteId(siteId);
				searchVo.setIncludeSub(true);
				if(sortType == null){searchVo.setSortType(3);}else{searchVo.setSortType(sortType);}//排序类型默认为按照更新时间
				String columnIds="";
				List<Map<String, Object>> columnMapList = columnService.getList(siteId, User.getCurrentUserId(request));
				if (columnMapList!=null&&columnMapList.size()>0) {
					for (int i = 0; i < columnMapList.size(); i++) {
						columnIds+=columnMapList.get(i).get("id");
						if (i!=columnMapList.size()-1) {
							columnIds+=",";
						}
					}
				}
				if (columnIds=="") {
					searchVo.setColumnIds("-1");//表示没任何栏目的新闻管理权限
				}else{
					searchVo.setColumnIds(columnIds);
				}
				
				
				PageList pageList =articleService.findArticlePageList(currentPage, pageSize, searchVo, true);
				pageList.setList(DataUtil.deleteListMapByKey((List<Map<String, Object>>) pageList.getList(), "content")) ;
				JSONUtil.printToHTML(response, pageList);
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入新闻添加页面
	 * @author lifqiang
	 * @param map
	 * @param columnId
	 * @return
	 */
	@RequestMapping(value="add")
	public String add(ModelMap map, HttpServletRequest request, HttpServletResponse response,Integer columnId){
		Integer siteId =User.getCurrentSiteId(request);
		Map<String, Object>  columnMap=columnService.load(columnId);
		map.put("siteId", siteId);
		map.put("column", columnMap);// 栏目类型：单网页、新闻栏目、图片栏目、外部链接
		map.put("recommendList", recommendService.getAll(siteId));
		map.put("tempId", UUID.randomUUID().toString());//临时文件关联ID
        map.put("extraList", articleService.getArticleExtraListMap(siteId));
		return "site/article/addNewsArticle";
	}
	
	/**
	 * 保存新闻
	 * @param columnId	栏目ID
	 * @param type		栏目类型：单网页、新闻栏目、图片栏目、外部链接
	 * @param title		文章标题
	 * @param content	文章内容
	 * @param recommendIds 推荐位置ID,多个逗号隔开
	 * @param smallPicUrl 缩略图
	 * @param imageIds  新闻相关联的文件ID,多个用逗号隔开(包含缩略图文件、图片列表文件、新闻内容图片文件、附件文件)
	 * @param tempId    保存文章之前标识文件为某一新闻的
	 * @param title2	副标题
	 * @param introduce 简介
     * @param extraFieldKey 附加信息的key
     * @param waterMark 水印
     * @param transparency 透明度
	 */
	@RequestMapping(value="save")
	public void save(HttpServletRequest request, HttpServletResponse response, 
			Integer columnId,String type,String title,String content,
			String recommendIds,String smallPicUrl, String smallPicOriginalUrl,
			String imageIds,String tempId,String title2,String introduce,
            @RequestParam(required =  false) String[] extraFieldKey,String waterMark,int transparency ){
		try {
			
			Integer siteId=User.getCurrentSiteId(request);
			Integer userId=User.getCurrentUserId(request);
			if (siteId==null||siteId==0||columnId == null||columnId == 0||userId==null||userId==0||StringUtil.isEmpty(type)||StringUtil.isEmpty(tempId)) {
				JSONUtil.print(response, Init.FAIL);
			}else{
                // 如果没有上传缩略图则获取正文内容第一张图片作为缩略图
                if(StringUtil.isEmpty(smallPicUrl) && !StringUtil.isEmpty(content)){
                    try {
                        String result = getContentImage(request, content);
                        if(result != null){
                            smallPicOriginalUrl = smallPicUrl = result;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(StringUtil.isEmpty(smallPicUrl) && !StringUtil.isEmpty(imageIds)){
                    Map<String, Object> column = columnService.load(columnId);
                    if(column != null && column.get("type") != null && column.get("type").equals("图片栏目")){
                        try{
                            String[] ids = imageIds.split(",");
                            Integer imageId = Integer.valueOf(ids[0]);
                            String result = getNewImagePath(request, imageId);
                            if(result != null){
                                smallPicOriginalUrl = smallPicUrl = result;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                Map<String, Object> article=new HashMap<String, Object>();
				article.put("userId", User.getCurrentUserId(request));
				article.put("siteId", siteId);
				article.put("columnId", columnId);
				article.put("createTime", new Date());
				article.put("clickCount", 0);
				article.put("viewCount", 0);
				article.put("tempId", tempId);
				article.put("updateTime", new Date());
				article.put("title", title);
				article.put("content", content);
				article.put("smallPicUrl", smallPicUrl);
				article.put("smallPicOriginalUrl", smallPicOriginalUrl);
				article.put("title2", title2);
				if(StringUtils.isBlank(introduce)) {//假如简介为空，那么就截取content的前150个字符(去除html代码和空格回车)做为简介
					if(StringUtils.isNotBlank(content)) {
						String introduceContent=StringUtil.delHTMLTag(content);
						article.put("introduce", StringUtil.subStr(introduceContent, 250));
					}
				}else {
					article.put("introduce", introduce);		
				}
				article.put("status", 0);
                // 附加信息
                JSONObject result = new JSONObject();
                if(extraFieldKey != null && extraFieldKey.length > 0){
                    for (int i = 0; i < extraFieldKey.length; i++) {
                        String value = request.getParameter(extraFieldKey[i]);
                        if(StringUtil.isEmpty(value)) continue;
                        result.put(extraFieldKey[i], value.trim());
                    }
                }
                article.put("extra", result.toString());
				// 判断是否需要审核
				Map<String, Object> site = siteService.load(siteId);
				Integer isUseCheck = (Integer) site.get("isUseCheck");
				article.put("checked", isUseCheck.equals(1) ? 1 : 0 );
				
				Serializable id =articleService.save(article,recommendIds,imageIds,request);
				if(id!=null){
					if(StringUtils.isNotEmpty(waterMark)) {//是否加水印
						StringBuilder basePath=new StringBuilder();
						basePath.append(request.getSession().getServletContext().getRealPath("/").replaceAll("\\\\", "/")).deleteCharAt(basePath.length()-1);
						Map<String, Object> map=siteService.load(siteId);
						List<String> listPath=imageService.findMarkPath((Integer)id, 1);
						listPath.add(smallPicUrl);
						listPath.add(smallPicOriginalUrl);
						if(map.get("smallPicOriginalUrl")!=null&&StringUtils.isNotBlank(map.get("smallPicOriginalUrl").toString())) {
							printWaterMark(basePath.toString(),map.get("smallPicOriginalUrl").toString(), listPath, 0, transparency, 5);							
						}
					}
				}else{
					JSONUtil.print(response, Init.FAIL);
				}
				JSONUtil.print(response, Init.SUCCEED);
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	/**
	 * 图片加水印
	 * @param markPath 水印地址
	 * @param picPath 图片地址
	 * @param degree 旋转角度
	 * @param transparency 透明度
	 * @param location 位置
	 */
	private void printWaterMark(String webPath,String markPath,List<String> picPath,int degree,int transparency,int location) {
		for(String data:picPath) {
			ImageUitl.markImageByIcon(webPath.concat(markPath),webPath.concat(data), degree, transparency, location);			
		}
	}
    /**
     * 从正文中获取第一张图
     * @param request
     * @param content
     * @return
     * @throws IOException
     */
    private String getContentImage(HttpServletRequest request, String content) throws IOException {
        List<String> imgNodes = StringUtil.searchStr(content.toLowerCase(), "<img", ">");
        for(String imgNode : imgNodes){
            List<String> srcList = StringUtil.searchStr(imgNode, "src=\"", "\"");
            for(String src : srcList){
                String url = src.substring(5, src.length() -1).trim();
                if(url.charAt(0) == '/'){
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    File file = new File(realPath + url);
                    if(file.exists()){
                        File parent = file.getParentFile();
                        File newFile = new File(parent, "copy_" + file.getName());
                        FileUtil.copyFile(file, newFile);
                        String newUrl = newFile.getPath().substring(realPath.length()).replaceAll("\\\\{1,2}", "/");
                        String newFilePath = newFile.getPath();
                        ImageUitl.imageZip(newFilePath, newFilePath, 1024, 768, true);
                        return "/" + newUrl;
                    }
                }
            }
        }
        return null;
    }

    private String getNewImagePath(HttpServletRequest request,Integer imageId) throws IOException {
        Map<String, Object> load = imageService.load(imageId);
        String url = (String) load.get("path");
        if(url.charAt(0) == '/'){
            String realPath = request.getSession().getServletContext().getRealPath("/");
            File file = new File(realPath + url);
            if(file.exists()){
                File parent = file.getParentFile();
                File newFile = new File(parent, "copy_" + file.getName());
                FileUtil.copyFile(file, newFile);
                String newUrl = newFile.getPath().substring(realPath.length()).replaceAll("\\\\{1,2}", "/");
                String newFilePath = newFile.getPath();
                ImageUitl.imageZip(newFilePath, newFilePath, 1024, 768, true);
                return "/" + newUrl;
            }
        }
        return null;
    }

	/**
	 * 删除新闻
	 * @author lifqiang
	 * @param ids 新闻ID,多个用逗号隔开
	 */
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request, HttpServletResponse response,HttpSession session, String ids){
		try {
			if(StringUtil.isEmpty(ids)){
				JSONUtil.print(response, Init.FAIL);
			}else{
				int result=articleService.delete(ids,request);
				JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	/**
	 * 进入新闻编辑界面
	 * @author lifqiang
	 * @param map
	 * @param id 新闻id
	 * @return
	 */
	@RequestMapping(value="edit")
	public String edit(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer id){
		Integer siteId=User.getCurrentSiteId(request);
		Map<String,Object> article= articleService.load(id);//文章
		if (article!=null) {
			Map<String, Object> column=columnService.load(Integer.parseInt(article.get("columnId")+""));//栏目
			map.put("tempId", article.get("tempId"));
			if (StringUtil.isEmpty(article.get("tempId")+"")) {
				map.put("tempId", UUID.randomUUID().toString());//临时文件关联ID
			}
			map.put("column", column);// 栏目
			map.put("article", article);
			
			//图片列表
			List<Map<String, Object>> imageList=imageService.find(id,1, null,null);
			map.put("imageList", imageList);
			
			//附件列表
			List<Map<String, Object>> fileList=imageService.find(id, 3, null,null);
			map.put("fileList", fileList);
			
			//推荐位列表
			List<Map<String, Object>> recommendList=recommendService.getAll(siteId);
			if (recommendList!=null&&article.containsKey("arList")) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> arList=(List<Map<String, Object>>) article.get("arList");
				for (Map<String, Object> item : recommendList) {
					for (Map<String, Object> map2 : arList) {
						if (map2.get("recommendId").equals(item.get("id"))) {
							item.put("checked", "checked");
							break;
						}
					}
				}
			}
			map.put("recommendList", recommendList);//推荐
            // 附加信息
            List<Map<String, Object>> extraListMap = articleService.getArticleExtraListMap(siteId);
            String extra = (String) article.get("extra");
            if(StringUtil.isNotEmpty(extra)){
                JSONObject values = JSONObject.fromObject(extra);
                for (Map<String, Object> ex : extraListMap) {
                    String field = (String) ex.get("field");
                    if(!values.containsKey(field)) continue;
                    ex.put("value", values.getString(field));
                }
            }
            map.put("extraList", extraListMap);
		}
		return "site/article/addNewsArticle";
	}
	/**
	 * 进入新闻详情界面
	 * @author lifqiang
	 * @param map
	 * @param id  新闻ID
	 * @return
	 */
	@RequestMapping(value="toView")
	public String toView(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer id){
		Integer siteId=User.getCurrentSiteId(request);
		Map<String,Object> article= articleService.load(id);
		Map<String, Object> column=columnService.load(Integer.parseInt(article.get("columnId")+""));
		String type=column.get("type")+"";// 栏目类型；单页栏目、新闻栏目、外部栏目、图片栏目、下载栏目
		map.put("columnType", type);
		map.put("article", articleService.load(id));//文章
		
		if ("新闻栏目".equals(type)) {//新闻栏目加载多推荐列表
			List<Map<String, Object>> recommendList=recommendService.getAll(siteId);
			if (recommendList!=null&&article.containsKey("arList")) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> arList=(List<Map<String, Object>>) article.get("arList");
				for (Map<String, Object> item : recommendList) {
					for (Map<String, Object> map2 : arList) {
						if (map2.get("recommendId").equals(item.get("id"))) {
							item.put("checked", "checked");
							break;
						}
					}
				}
			}
			map.put("recommendList", recommendList);//推荐
			
			//附件列表
			List<Map<String, Object>> fileList=imageService.find(id, 3, null,null);
			map.put("fileList", fileList);
		}
		if ("图片栏目".equals(type)) {//图片文章加载多图片列表
			//图片栏目连图片一起加载
			List<Map<String, Object>> imageList=imageService.find(id, 1, null,null);
			map.put("imageList", imageList);
		}
        // 附加信息
        List<Map<String, Object>> extraListMap = articleService.getArticleExtraListMap(siteId);
        String extra = (String) article.get("extra");
        if(StringUtil.isNotEmpty(extra)){
            JSONObject values = JSONObject.fromObject(extra);
            for (Map<String, Object> ex : extraListMap) {
                String field = (String) ex.get("field");
                if(!values.containsKey(field)) continue;
                ex.put("value", values.getString(field));
            }
        }
        map.put("extraList", extraListMap);
		return "site/article/view";
	}


	/**
	 * 更新新闻
	 * @author lifq
	 * @param id		新闻id
	 * @param title		标题
	 * @param title2	副标题
	 * @param introduce	简介
	 * @param type		新闻类型
	 * @param content	内容
	 * @param smallPicUrl	缩略图地址，相对路径
	 * @param recommendIds	推荐位id,多个用逗号隔开
	 * @param imageIds		图片文件id
	 * @param transparency 水印透明度
	 */
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, 
			Integer id,String title,String title2,String introduce,String type,
			String content,String smallPicUrl, String smallPicOriginalUrl,
			String recommendIds,String imageIds,String createTime,
            @RequestParam(required =  false) String[] extraFieldKey,String waterMark,int transparency){
		try {
		
			if (id==null||id==0) {
				JSONUtil.print(response, Init.FAIL);
			}else{
                // 如果没有上传缩略图则获取正文内容第一张图片作为缩略图
                if(StringUtil.isEmpty(smallPicUrl) && !StringUtil.isEmpty(content)){
                    try {
                        String result = getContentImage(request, content);
                        if(result != null){
                            smallPicOriginalUrl = smallPicUrl = result;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(StringUtil.isEmpty(smallPicUrl) && !StringUtil.isEmpty(imageIds)){
                    Article article = articleService.get(id);
                    Map<String, Object> column = columnService.load(article.getColumnId());
                    if(column != null && column.get("type") != null && column.get("type").equals("图片栏目")){
                        try{
                            String[] ids = imageIds.split(",");
                            Integer imageId = Integer.valueOf(ids[0]);
                            String result = getNewImagePath(request, imageId);
                            if(result != null){
                                smallPicOriginalUrl = smallPicUrl = result;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                Integer userId=User.getCurrentUserId(request);
				Map<String, Object> article=new HashMap<String, Object>();
				article.put("id", id);
				article.put("content", content);
				article.put("title", title);
				article.put("title2", title2);
				article.put("smallPicUrl", smallPicUrl);
				article.put("smallPicOriginalUrl", smallPicOriginalUrl);
				article.put("introduce", introduce);
				article.put("createTime", createTime);
				article.put("userId", userId);
                // 附加信息
                JSONObject result = new JSONObject();
                if(extraFieldKey != null && extraFieldKey.length > 0){
                    for (int i = 0; i < extraFieldKey.length; i++) {
                        String value = request.getParameter(extraFieldKey[i]);
                        if(StringUtil.isEmpty(value)) continue;
                        result.put(extraFieldKey[i], value.trim());
                    }
                }
                
                article.put("extra", result.toString());
				// 判断是否需要审核
				Map<String, Object> site = siteService.load(User.getCurrentSiteId(request));
				Integer isUseCheck = (Integer) site.get("isUseCheck");
				if( isUseCheck.equals(1) ){
					article.put("checked", 1);
				}
				articleService.update(article,recommendIds,imageIds);
				if(StringUtils.isNotEmpty(waterMark)) {//是否加水印
					StringBuilder basePath=new StringBuilder();
					basePath.append(request.getSession().getServletContext().getRealPath("/").replaceAll("\\\\", "/")).deleteCharAt(basePath.length()-1);
					Map<String, Object> map=siteService.load(Integer.valueOf(site.get("id").toString()));
					List<String> listPath=imageService.findMarkPath((Integer)id, 1);
					listPath.add(smallPicUrl);
					listPath.add(smallPicOriginalUrl);
					if(map.get("smallPicOriginalUrl")!=null&&StringUtils.isNotBlank(map.get("smallPicOriginalUrl").toString())) {
						printWaterMark(basePath.toString(),map.get("smallPicOriginalUrl").toString(), listPath, 0, transparency, 5);							
					}
			}
				JSONUtil.print(response, Init.SUCCEED);
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取栏目树数据
	 * @author lifqiang
	 */
	@RequestMapping(value="getTreeData")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response){
		Integer siteId =  User.getCurrentSiteId(request);
		Integer userId = User.getCurrentUserId(request);
		List<Map<String, Object>> mapList = columnService.getList(siteId, userId);
		if(mapList!=null){
			for (Map<String, Object> map : mapList) {
				map.put("open", true);
			}
		}
		JSONUtil.printToHTML(response, mapList);
	}
	
	/**
	 * 获取文章数量
	 * @author lifq
	 * @param columnId 可以按栏目计数,不传差当前站点的所有
	 * @throws IOException 
	 */
	@RequestMapping(value = "getArticleCount")
	public void getArticleCount(HttpServletRequest request, HttpServletResponse response,Integer columnId) throws IOException{
		Integer siteId =  User.getCurrentSiteId(request);
		int count=articleService.getArticleCount(siteId, columnId);
		response.getWriter().print(count);
	}
	
	
	/**
	 * 进入移动新闻栏目界面
	 * @author lifq
	 * @param map
	 * @param ids       要移动的新闻ID，多个用逗号隔开
	 * @param columType 移动新闻所属栏目的类型类型
	 * @return
	 */
	@RequestMapping("toMoveColumn")
	public String toMoveColumn(ModelMap map, HttpServletRequest request, HttpServletResponse response, String ids,String columType){
		map.put("ids", ids);
		map.put("columType", columType);
		return "site/article/moveColumn";
	}
	
	/**
	 * 批量修改文章的所属栏目
	 * @author lifq
	 * @param columnId 修改为的栏目ID
	 * @param ids      文章ID，多个用逗号隔开
	 */
	@RequestMapping(value="updateArticleColumn")
	public void updateArticleColumn(HttpServletRequest request, HttpServletResponse response, 
			Integer columnId,String ids){
		try {
			if (columnId==null||columnId==0||StringUtil.isEmpty(ids)) {
				JSONUtil.print(response, Init.FAIL);
			}else{
				int result=articleService.updateColumn(columnId, ids);
				JSONUtil.print(response, result>0? Init.SUCCEED :Init.FAIL );
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	
	 /**
	 * 更新文章相对应最后修改时间的排序
	 * @author lifq
	 * @param id 文章ID
	 * @param flag 更新标识：1、上移一条;2、下移一条;3、置顶;4、置尾
	 * @return
	 */
	@RequestMapping(value="updateArticleSort") 
	public void updateArticleSort(HttpServletResponse response,Integer id,Integer flag){
			try {
				if (id!=null&&id!=0&&flag!=null&&flag!=0) {
					articleService.updateArticleSort(id, flag);
					JSONUtil.print(response,Init.SUCCEED);
				}else{
					JSONUtil.print(response, Init.FAIL);
				}
			} catch (Exception e) {
				JSONUtil.print(response, Init.FAIL);
				e.printStackTrace();
			}
	}
	
	/**
	 * 设置文章浏览次数
	 * @param request
	 * @param response
	 * @param viewCount 浏览次数
	 * @param id 文章ID
	 */
	@RequestMapping("updateViewCount")
	public void updateViewCount(HttpServletRequest request, HttpServletResponse response, 
			Long viewCount, Integer id){
		boolean result = false;
		if(viewCount != null && id != null){
			int num = articleService.updateviewCount(id, viewCount);
			if(num > 0){
				result = true;
			}
			JSONUtil.print(response, result);
		}
	}
	
	/**
	 * 更改新闻的新旧状态
	 * @param request
	 * @param response
	 * @param ids
	 * @param lastTime
	 */
	@RequestMapping("updateStatus")
	public void updateLastTime(HttpServletRequest request, HttpServletResponse response, 
			String ids, String status){
		if(ids != null && status != null){
			int result = articleService.updateStatus(ids, status);
			if(result > 0){
				JSONUtil.printToHTML(response, true);
			}else {
				JSONUtil.printToHTML(response, false);
			}
		}
	}
	
	/**
	 * 更改新闻的审核状态
	 */
	@RequestMapping("/changeCheckState")
	public void changeCheckState(HttpServletRequest request, HttpServletResponse response, 
			Integer id, Integer state){
		if( id != null && state != null){
			int result = articleService.updateCheckedStatus(id, state.equals(1));
			if(result > 0){
				JSONUtil.printToHTML(response, true);
			}else {
				JSONUtil.printToHTML(response, false);
			}
		}else{
			JSONUtil.printToHTML(response, false);
		}
	}

    /**
     * 图片点赞
     */
    @RequestMapping("/imgSmile")
    @ResponseBody
    public String imgSmile(HttpServletRequest request, HttpServletResponse response, Integer id){
        Integer siteId =  siteService.getSiteId(request);
        if(siteId != null && siteId > 0 && id != null && id > 0){
            Cookie[] cookies = request.getCookies();
            String checkIds = "";
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                String name = cookie.getName();
                if(name.equals("imgSmileId") && StringUtil.isNotEmpty(cookie.getValue()))
                    checkIds = cookie.getValue().trim();
            }
            String[] ids = checkIds.split(",");
            for (int i = 0; i < ids.length; i++)
                if(id.toString().equals(ids[i].trim())) return "exist"; //已存在
            if(StringUtil.isNotEmpty(checkIds)) checkIds += ",";
            checkIds += id.toString();
            Cookie cookie = new Cookie("imgSmileId", checkIds);
            cookie.setMaxAge(60 * 60 * 24 * 15 );
            cookie.setPath("/");
            response.addCookie(cookie);
            imageService.updateSmile(siteId, id);
            return "succeed";
        }
       return "fail";
    }

}
