package com.site.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.util.JSONUtil;
import com.site.dao.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.base.config.Init;
import com.base.util.DataUtil;
import com.base.util.ListUtils;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.qq.connect.utils.json.JSONArray;
import com.qq.connect.utils.json.JSONObject;
import com.site.model.Data;
import com.site.model.Label;
import com.site.service.DataService;
import com.site.vo.ArticleSearchVo;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	private DataDao dataDao;

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private ColumnDao columnDao;

	@Autowired
	private ImageDao imageDao;
	
	@Autowired
	private FriendlyLinkDao friendlyLinkDao;
	
	@Autowired
	private ContactDao contactDao;

    @Autowired
    private MessageDao messageDao;
	
	@Override
	public Integer save(Data data) {
		return dataDao.save(data);
	}
	
	@Override
	public Map<String, Object> getTemplateDate(ModelMap modelMap, Integer siteId, Integer templateId, Integer currentPage, Integer pageSize, Integer columnId, Integer articleId, Map<String, Object> paramMap){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if(siteId != null){
			modelMap.put("siteId", siteId);
		}
		if(templateId != null){
			modelMap.put("templateId", templateId);
		}
		if(currentPage != null){
			modelMap.put("currentPage", currentPage);
		}
		if(pageSize != null){
			modelMap.put("pageSize", pageSize);
		}
		if(columnId != null){
			modelMap.put("columnId", columnId);
			//获取父栏目ID，格式parentColumnId + i , i从0开始
			/**
			Integer parentId = getTopColumnId(columnId);
			int i = 0;
			while(parentId != 0){
				modelMap.put("parentColumnId"+i, parentId);
				parentId = getTopColumnId(parentId);
				i++;
			}*/
		}
		if(articleId != null){
			modelMap.put("articleId", articleId);
		}
		if(paramMap.get("keyword") != null){
			modelMap.put("keyword", paramMap.get("keyword").toString());
		}
		
		// 根据模板ID查询所有的数据源 和 对应标签
		List<Data> dataList = dataDao.getList(templateId, siteId);
		for(Data data: dataList){
            // 列表标签
			if(Label.Item.List.equals(data.getLabelId())){
				
				System.out.println("-------"+data.getName()+"--------");
				if ((columnId!=null && columnId>0)) {
					if (data.getColumnIds().equals("0")||data.getColumnIds().equals("-1")) {//查询整站
						processList(modelMap, data, siteId, columnId, currentPage, pageSize,paramMap);
					}else if(Arrays.asList(data.getColumnIds()).contains(columnId)){//判断当前数据源是否选择了该栏目
						processList(modelMap, data, siteId, columnId, currentPage, pageSize,paramMap);
					}else{//判断当前数据源下的所有栏目是否包含有该栏目
						String [] clumnIds=data.getColumnIds().split(",");
						List<Integer> includeIds=new ArrayList<Integer>();
                        for (int i = 0; i < clumnIds.length; i++) {
							List<Integer> tempList=columnDao.getSelfAndAllChildrenId(Integer.parseInt(clumnIds[i]));
							if (tempList!=null&&tempList.size()>0) {
								includeIds.addAll(tempList);
							}
						}
                        //数据源包含传入的栏目的id根据传入的栏目id查询
						if (includeIds.contains(columnId)) {
							processList(modelMap, data, siteId, columnId, currentPage, pageSize,paramMap);
						}else{//不是查询整站并且数据源不包含要查的栏目id时不根据传入的栏目id查询
							processList(modelMap, data, siteId, null, currentPage, pageSize,null);
						}
					}
				}else{//没传入栏目id时查询查询数据源的数据
					processList(modelMap, data, siteId, null, currentPage, pageSize, paramMap);
				}
			}
			// 位置导航
			else if(Label.Item.Location.equals(data.getLabelId())){
				processLocation(modelMap, data, siteId, columnId, articleId);
			}
			// 栏目标签
			else if(Label.Item.Column.equals(data.getLabelId())){
				processColumn(modelMap, data, siteId, columnId, articleId);
			}
			// 文章内容标签
			else if(Label.Item.Article.equals(data.getLabelId())){
				processArticle(modelMap, data, siteId, articleId);
			}
			//站点菜单标签
			else if(Label.Item.Menu.equals(data.getLabelId())){
				processMenu(modelMap, data, siteId);
			}
			//单网页
			else if(Label.Item.Single.equals(data.getLabelId())){
				processSingle(modelMap, data, siteId, columnId);
			//友情链接
			}else if (Label.Item.Friendlylink.equals(data.getLabelId())) {
				processFriendlyLink(modelMap, data, siteId);
			//联系方式
			}else if (Label.Item.Contcat.equals(data.getLabelId())) {
				processContact(modelMap, data, siteId);
			}else if (Label.Item.Message.equals(data.getLabelId())) { // 加载留言板
                processMessage(modelMap, data, siteId, currentPage, pageSize);
            }
		}
		return dataMap;
	}
    /**
     * 留言板处理
     * @param modelMap
     * @param data
     * @param siteId
     * @param currentPage
     * @param pageSize
     */
    private void processMessage(ModelMap modelMap, Data data, Integer siteId, Integer currentPage, Integer pageSize) {
        if(currentPage == null || currentPage <= 0 ) currentPage = 1;
        if(pageSize == null || pageSize <= 0 ) pageSize = 20;
        PageList pageList = messageDao.findPageList(currentPage, pageSize, siteId);
        modelMap.put(data.getName(), pageList);
    }
	/**
	 * 联系方式处理
	 * @param modelMap
	 * @param data
	 * @param siteId
	 */
	private void processContact(ModelMap modelMap, Data data, Integer siteId) {
		List<Map<String, Object>> listMap = contactDao.findContactList(siteId);
		if(listMap.size() > 0){
			modelMap.put(data.getName(), listMap);
		}
	}
	/**
	 * 友情链接处理
	 * @param modelMap
	 * @param data
	 * @param siteId
	 */
	private void processFriendlyLink(ModelMap modelMap, Data data, Integer siteId) {
		List<Map<String, Object>> listMap = friendlyLinkDao.findFriendlyLinkList(siteId, null, null, null,"pass");
		if(listMap.size() > 0){
			modelMap.put(data.getName(), listMap);
		}
	}
	
	/**
	 * 单网页标签处理
	 * @param modelMap
	 * @param data
	 * @param siteId
	 * @param columnId
	 */
	private void processSingle(ModelMap modelMap, Data data, Integer siteId, Integer columnId) {
		ArticleSearchVo searchVo = new ArticleSearchVo();
		searchVo.setLimit(1);
		searchVo.setColumnId(columnId);
		searchVo.setIncludeSub(false);
		List<Map<String, Object>> listMap = articleDao.findArticleList(searchVo);
		if(listMap.size() > 0){
            Map<String, Object> map = listMap.get(0);
            if(map != null){
                String extra = (String) map.get("extra");
                if(StringUtil.isNotEmpty(extra))
                    map.put("extra", JSONUtil.getJSONField(extra));
            }
            modelMap.put(data.getName(), map);
		}
	}

	/**
	 * 列表标签，可以使用于多个新闻栏目列表调用，
	 * 或者传参列表页带分页情况
	 * @param modelMap
	 * @param data
	 * @param siteId
	 * @param columnId
	 * @param currentPage
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	private void processList(ModelMap modelMap, Data data, Integer siteId, Integer columnId, Integer currentPage, Integer pageSize,Map<String, Object> paramMap){
		ArticleSearchVo searchVo = new ArticleSearchVo();
		searchVo.setSiteId(siteId);
		searchVo.setIncludeSub(true);
		searchVo.setLimit(data.getSize());
		searchVo.setSortType(data.getSortType());
		searchVo.setRecommendId(data.getRecommendId());
		if(data.getIsImages().equals(1)){
			searchVo.setImage(true);
		}
		if (paramMap!=null&&paramMap.containsKey("keyword")
				&& paramMap.get("keyword") != null && StringUtil.isNotEmpty(paramMap.get("keyword").toString())) {
			searchVo.setKeyWord(paramMap.get("keyword")+"");
		}
		
		int len=0;//标识新闻内容是否要截取，-1去掉新闻内容,0不截取新闻内容，大于0则为截取的长度
		if (data!=null&&data.getDisplayContentSize()!=null) {
			len=data.getDisplayContentSize();
		}
		boolean jsonData=false;//返回的新闻数据是否为json格式
		if (data!=null&&data.getIsJsonData()!=null&&data.getIsJsonData()==1) {
			jsonData=true;
		}

		if(data.getColumnIds().equals("-1")){// 传参方式
			searchVo.setColumnId(columnId);
		}else if (data.getColumnIds().equals("0")) { // 整站
			searchVo.setColumnId(null);
			searchVo.setColumnIds(null);
		}else if (columnId!=null&&columnId>0) {
			searchVo.setColumnId(columnId);
		}else{
			searchVo.setColumnIds(data.getColumnIds());
		}
		if (data.getEveryColumn()==null||data.getEveryColumn()==0) {//不分栏目查询
			if(data.getDisplayPage() == 1){//分页查询
				if (currentPage==null||currentPage<1) {
					currentPage=1;
				}
					if (data.getSize()==null||data.getSize()<1) {
						pageSize=Init.getPageSize(null);
					}
				
				PageList pageList = articleDao.findArticlePageList(currentPage, pageSize, searchVo, false);
				/*Data data=dataDao.getByNameAndSiteId(name, siteId);*/
				//查处相关联的文件列表
				if (pageList!=null&&pageList.getList().size()>0) {
                    List<Integer> ids = new ArrayList<Integer>();
                    List<Map<String, Object>> listMap = pageList.getList();
                    for (Map<String, Object> map : listMap)
                        ids.add((Integer) map.get("id"));
                    List<Map<String, Object>> imageList = imageDao.findList(ids, 1,null, null);
                    Map<Object, List<Map<String, Object>>> imageLists = ListUtils.classifyMapList("articleId", imageList);
                    List<Map<String, Object>> fileList = imageDao.findList(ids, 3,null, null);
                    Map<Object, List<Map<String, Object>>> fileLists = ListUtils.classifyMapList("articleId", fileList);
					for (int i=0;i<pageList.getList().size();i++) {
						Map<String, Object> articleMap=(Map<String, Object>) pageList.getList().get(i);
						if (articleMap!=null) {
							Integer id=(Integer) articleMap.get("id");
							articleMap.put("imageList", imageLists.get(id));//图片类型的文章图片列表
							articleMap.put("fileList", fileLists.get(id)); //附件列表
							articleMap.put("videoList", DataUtil.findArticleFileList(articleMap.get("content")+"")); //新闻内容包含的视频列表
                            // 转化附加信息为map
                            String extra = (String) articleMap.get("extra");
                            if(StringUtil.isNotEmpty(extra)){
								Map<String, Object> jsonField = JSONUtil.getJSONField(extra);
								Map<String, Object> ex = new HashMap<String, Object>();
								if(jsonField != null && jsonField.size() > 0)
									ex.putAll(jsonField);
								articleMap.put("extra", ex);
							}
						}
					}
				}
				//处理文章的内容
				if (len==-1) {
					DataUtil.deleteListMapByKey((List<Map<String, Object>>) pageList.getList(), "content");
				}else if (len>0) {
					DataUtil.trimListMapValue((List<Map<String, Object>>) pageList.getList(), "content", len, true);
				}
				//处理返回的数据格式json或对象类型
				if (jsonData) {
					modelMap.put(data.getName(), (new JSONObject(pageList)).toString());
				}else{
					modelMap.put(data.getName(), pageList);
				}
			}else{//非分页列表查询
				List<Map<String, Object>> listMap = articleDao.findArticleList(searchVo);
				//查处相关联的文件列表
				if (listMap!=null&&listMap.size()>0) {
                    List<Integer> ids = new ArrayList<Integer>();
                    for (Map<String, Object> map : listMap)
                        ids.add((Integer) map.get("id"));
                    List<Map<String, Object>> imageList = imageDao.findList(ids, 1,null, null);
                    Map<Object, List<Map<String, Object>>> imageLists = ListUtils.classifyMapList("articleId", imageList);
                    List<Map<String, Object>> fileList = imageDao.findList(ids, 3,null, null);
                    Map<Object, List<Map<String, Object>>> fileLists = ListUtils.classifyMapList("articleId", fileList);
                    for (int i=0;i<listMap.size();i++) {
                        Map<String, Object> articleMap = listMap.get(i);
                        Integer id=Integer.parseInt(articleMap.get("id")+"");
                        articleMap.put("imageList", imageLists.get(id));//图片类型的文章图片列表
                        articleMap.put("fileList", fileLists.get(id)); //附件列表
                        articleMap.put("videoList", DataUtil.findArticleFileList(articleMap.get("content") + "")); //新闻内容包含的视频列表
                        // 转化附加信息为map
                        String extra = (String) articleMap.get("extra");
                        if(StringUtil.isNotEmpty(extra))
                            articleMap.put("extra", JSONUtil.getJSONField(extra));
					}
					//处理文章的内容
					if (len==-1) {
						DataUtil.deleteListMapByKey(listMap, "content");
					}else if (len>0) {
						DataUtil.trimListMapValue(listMap, "content", len, true);
					}
					//处理返回的数据格式json或对象类型
					if (jsonData) {
						modelMap.put(data.getName(), (new JSONArray(listMap)).toString());
					}else{
						modelMap.put(data.getName(), listMap);
					}
				}
			}
		}else{//分栏目查询
            final String dataIdKey = "data-identification-id-key";
            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			if (!StringUtil.isEmpty(data.getColumnIds()) && !",".equals(data.getColumnIds())) {
                List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
                for(String currentColumnId : data.getColumnIds().split(",")){
					if (StringUtil.isEmpty(currentColumnId)||Integer.parseInt(currentColumnId)<1) {
						continue;
					}
					Map<String, Object> columnMap = columnDao.load(Integer.parseInt(currentColumnId));
					if (columnMap ==null || columnMap.isEmpty()) {
						continue;
					}
                    Integer cId = Integer.parseInt(currentColumnId);
                    searchVo.setColumnId(cId);
                    List<Map<String, Object>> articleList = articleDao.findArticleList(searchVo);
                    for (Map<String, Object> map : articleList)
                        map.put(dataIdKey, cId);
                    listMap.addAll(articleList);
                    resultList.add(columnMap);
                }
                if(resultList.size() > 0){
                    //查处相关联的文件列表
                    if (listMap!=null&&listMap.size()>0) {
                        // 旧的数据处理
//						for (int i=0;i<listMap.size();i++) {
//								Integer id=Integer.parseInt(listMap.get(i).get("id")+"");
//								listMap.get(i).put("imageList", imageDao.find(id, 1,null, null));//图片类型的文章图片列表
//								listMap.get(i).put("fileList", imageDao.find(id, 3,null, null)); //附件列表
//								listMap.get(i).put("videoList", DataUtil.findArticleFileList(listMap.get(i).get("content")+"")); //新闻内容包含的视频列表
//						}
                        List<Integer> ids = new ArrayList<Integer>();
                        for (int i=0;i<listMap.size();i++) {
                            Map<String, Object> articleMap = listMap.get(i);
                            Integer id=Integer.parseInt(articleMap.get("id")+"");
                            ids.add(id);
                            articleMap.put("videoList", DataUtil.findArticleFileList(articleMap.get("content") + "")); //新闻内容包含的视频列表
                        }
                        List<Map<String, Object>> imageList = imageDao.findList(ids, 1,null, null);
                        Map<Object, List<Map<String, Object>>> imageLists = ListUtils.classifyMapList("articleId", imageList);
                        List<Map<String, Object>> fileList = imageDao.findList(ids, 3,null, null);
                        Map<Object, List<Map<String, Object>>> fileLists = ListUtils.classifyMapList("articleId", fileList);
                        for (Map<String, Object> map : listMap) {
                            map.put("imageList", imageLists.get(map.get("id")));
                            map.put("fileList", fileLists.get(map.get("id")));
                            // 转化附加信息为map
                            String extra = (String) map.get("extra");
                            if(StringUtil.isNotEmpty(extra))
                                map.put("extra", JSONUtil.getJSONField(extra));
                        }
                        //处理文章的内容
                        if (len==-1) {
                            DataUtil.deleteListMapByKey(listMap, "content");
                        }else if (len>0) {
                            DataUtil.trimListMapValue(listMap, "content", len, true);
                        }
                        Map<Object, List<Map<String, Object>>> columnListMap = ListUtils.classifyMapList(dataIdKey, listMap);
                        for (Map<String, Object> map : resultList){
                            map.put("list", columnListMap.get(map.get("id")));
                        }
                    }
                }
			}
			//组装链接
			recursionByMenu(resultList);
			//处理返回的数据格式json或对象类型
			if (jsonData) {
				modelMap.put(data.getName(), (new JSONArray(resultList)).toString());
			}else{
				modelMap.put(data.getName(), resultList);
			}
		}
	}
	
	/**
	 * 当前位置标签处理，根据是否有栏目编号 和文章编号对应查出层级关系
	 * @param modelMap
	 * @param data
	 * @param siteId
	 * @param columnId
	 * @param articleId
	 */
	private void processLocation(ModelMap modelMap, Data data, Integer siteId, Integer columnId, Integer articleId){
		if(columnId == null && articleId != null){
			Map<String, Object> articleMap = articleDao.load(articleId);
			if (articleMap!=null) {
				columnId = (Integer) articleMap.get("columnId");
			}
		}
		if(columnId != null){
			List<Map<String, Object>> locationList = columnDao.selfAndParents(siteId, columnId);
			recursionByMenu(locationList);
			modelMap.put(data.getName(), locationList);
		}
	}
	
	/**
	 * 遍历最上级的栏目ID
	 * @param columnId
	 * @return
	 */
	private Integer getTopColumnId(Integer columnId){
		return (Integer) columnDao.load(columnId).get("pid");
		
	}
	
	/**
	 * 栏目标签处理 
	 * 如果传参则查询子栏目，
	 * 如果整站则查询所有栏目，
	 * 指定查询某个栏目的所有子栏目包括自身
	 * @param modelMap
	 * @param data
	 * @param siteId
	 * @param columnId
	 */
	private void processColumn(ModelMap modelMap, Data data, Integer siteId, Integer columnId, Integer articleId){
		// 查询全部导航栏目
		if("0".equals(data.getColumnIds())){
			List<Map<String, Object>> columnListMap = columnDao.navigationWithChildren(siteId);
			modelMap.put(data.getName(), columnListMap);
		}else if("-1".equals(data.getColumnIds())){
			// 如果是内容页
			if(columnId == null && articleId != null){
				columnId = articleDao.get(articleId).getColumnId();
			}
			// 根据页面传参查询所有子栏目 method getTopColumnId
			Integer temp = columnId;
			Integer pid = getTopColumnId(columnId);
			while(pid != 0){
				temp = pid;
				pid = getTopColumnId(temp);
			}
			
			Map<String, Object> selfColumn = columnDao.selfWithChildren(siteId, temp);
			List<Map<String, Object>> selfColumnByList = new ArrayList<Map<String, Object>>();
			selfColumnByList.add(selfColumn);
			recursionByMenu(selfColumnByList);
			modelMap.put(data.getName(), selfColumn);
		}else{
			// 查询指定某个栏目的所有子栏目
			Integer currentColumnId = Integer.parseInt(data.getColumnIds());
			Map<String, Object> selfColumn = columnDao.selfWithChildren(siteId, currentColumnId);
			List<Map<String, Object>> selfColumnByList = new ArrayList<Map<String, Object>>();
			selfColumnByList.add(selfColumn);
			recursionByMenu(selfColumnByList);
			modelMap.put(data.getName(), selfColumn);
		}
	}
	
	/**
	 * 文章内容标签处理，必须传入文章ID
	 * @param modelMap
	 * @param data
	 * @param siteId
	 * @param articleId	
	 */
	private void processArticle( ModelMap modelMap, Data data, Integer siteId, Integer articleId ){
		Map<String, Object> articleMap = articleDao.load(articleId);
		if (articleMap!=null) {
			List<Map<String,Object>> imageList=imageDao.find(articleId, 1,null, null);//图片列表
			List<String> videoList=DataUtil.findArticleFileList(articleMap.get("content")+"");//视频列表
			
			articleMap.put("imageListJson", (new JSONArray(imageList)).toString());//图片类型的文章图片列表,json格式
			articleMap.put("imageList", imageList);	// 图片列表
			
			//articleMap.put("imageList", imageList);//图片类型的文章图片列表
			articleMap.put("fileList", imageDao.find(articleId, 3,null, null)); //附件列表
			//articleMap.put("videoList", videoList); //新闻内容包含的视频列表
			articleMap.put("videoListJson", (new JSONArray(videoList)).toString()); //新闻内容包含的视频列表
            String extra = (String) articleMap.get("extra");
            if(StringUtil.isNotEmpty(extra))
                articleMap.put("extra", JSONUtil.getJSONField(extra));
		}
		Map<String, Object> nextAndPre = articleDao.findNextAndPre(articleId);
		if(articleMap!=null && nextAndPre!=null)articleMap.putAll(nextAndPre);
		modelMap.put(data.getName(), articleMap);
	}
	
	/**
	 * 站点菜单标签处理，不显示在首页的则不出现
	 * @param modelMap
	 * @param data
	 * @param siteId
	 */
	@Override
	public void processMenu(ModelMap modelMap, Data data, Integer siteId){
		List<Map<String, Object>> columnListMap = columnDao.navigationWithChildren(siteId);
		recursionByMenu(columnListMap);
		modelMap.put(data.getName(), columnListMap);
	}
	
	/**
	 * 递归判断栏目类型  拼接url	
	 * @param mapList
	 */
	private void recursionByMenu(List<Map<String, Object>> mapList){
		for(Map<String, Object> map : mapList){
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> childrenList = ((List<Map<String, Object>>) map.get("children"));
			if(childrenList != null && childrenList.size() > 0){
				recursionByMenu(childrenList);
			}
			String id = map.get("id") + "";
			String type = map.get("type") + "";
			if("新闻栏目".equals(type) || "图片栏目".equals(type)){
				//测试
				String pageSize = "20";
				try{
					pageSize = map.get("pageSize") == null? "20" : map.get("pageSize").toString();
				}catch(Exception e){
					e.printStackTrace();
					pageSize = "20";
				}
				map.put("url", "/list-" + id + "-1-" + pageSize + ".html");
			}else if("单网页".equals(type)){
				map.put("url", "/single-" + id + ".html");
			}else if("外部链接".equals(type)){
				
			}
		}
	}

	@Override
	public void delete(Integer id, Integer siteId) {
		dataDao.delete(id, siteId);
	}

	@Override
	public void update(Data data) {
		dataDao.update(data);
	}

	@Override
	public List<Map<String, Object>> listByLabel(Integer siteId, String labelId) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		if(Label.Item.List.equals(labelId)){
			listMap = dataDao.listByList(siteId);
		}else if(Label.Item.Column.equals(labelId)){
			listMap = dataDao.listByColumn(siteId);
		}else if(Label.Item.Recommend.equals(labelId)){
			listMap = dataDao.listByRecommend(siteId);
		}
		for(Map<String, Object> map : listMap){
			String columnIds = map.get("columnIds") + "";
			if("0".equals(columnIds)){
				map.put("columnName", "整站");
			}else if("-1".equals(columnIds)){
				map.put("columnName", "传参");
			}else if(columnIds.length() > 0){
				String ids = StringUtil.changeForSQL(columnIds);
				List<String> columnNames = columnDao.getListByIds(ids);
				map.put("columnName", columnNames);
			}
		}
		return listMap;
	}

	@Override
	public List<Map<String, Object>> getList(Integer siteId) {
		return dataDao.getList(siteId);
	}

	@Override
	public List<Data> getList(Integer siteId, Integer templateId) {
		return dataDao.getList(templateId, siteId);
	}

	@Override
	public Map<String, Object> load(Integer id, Integer siteId) {
		return dataDao.load(id, siteId);
	}

	@Override
	public Data getByNameAndSiteId(String name, Integer siteId) {
		return dataDao.getByNameAndSiteId(name, siteId);
	}

	@Override
	public Map<String, Object> getTemplateDate(ModelMap modelMap,
			Integer siteId, Integer templateId, String dataName,
			Integer currentPage, Integer pageSize, Integer columnId,
			Integer articleId, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}
}
