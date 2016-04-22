package com.site.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.site.model.ArticleExtra;
import com.site.model.MsgConfig;
import com.site.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.SiteService;

@Controller
@RequestMapping("site")
public class SiteAction {

	@Autowired
	private SiteService service;

    @Autowired
    private ArticleService articleService;

	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "site/site/index";
	}
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize){
		currentPage = Init.getCurrentPage(currentPage);
		pageSize = Init.getPageSize(pageSize);
		String keyword = request.getParameter("keyword");
		Map<String, Object> pageListMap = service.getListByPage(currentPage, pageSize, keyword);
		JSONUtil.printToHTML(response, pageListMap);
	}
	@RequestMapping(value="add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		return "site/site/add";
	}
	@RequestMapping(value="save")
	public void save(HttpServletRequest request, HttpServletResponse response){
		String name = request.getParameter("name");
		String domain = request.getParameter("domain");
		String directory = request.getParameter("directory");
		if(domain!=null && domain.startsWith("http://"))domain=domain.replace("http://", "");
		if(name==null || name.trim().equals("") || domain==null || domain.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			Serializable id = service.save(name, domain, directory);
			if(id!=null){
				JSONUtil.print(response, Init.SUCCEED);
			}else{
				JSONUtil.print(response, Init.FAIL);
			}
		}
	}
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
		if(id==null || id<=0){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.delete(id);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="edit")
	public String edit(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer id){
		map.put("id", id);
		return "site/site/edit";
	}
	@RequestMapping(value="load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> map = service.load(id);
		JSONUtil.printToHTML(response, map);
	}
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id){
		String name = request.getParameter("name");
		String domain = request.getParameter("domain");
		String directory = request.getParameter("directory");
		String isUseCheck = request.getParameter("isUseCheck");
		if(domain!=null && domain.startsWith("http://"))domain=domain.replace("http://", "");
		if(name==null || name.trim().equals("") || domain==null || domain.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.update(id, name, domain, directory, isUseCheck);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="domainIsExistWithSelf")
	public void domainIsExistWithSelf(HttpServletRequest request, HttpServletResponse response, Integer id){
		String domain = request.getParameter("domain");
		if(domain==null || domain.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = service.countByDomainWithSelf(id, domain);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
	@RequestMapping(value="domainIsExist")
	public void domainIsExist(HttpServletRequest request, HttpServletResponse response){
		String domain = request.getParameter("domain");
		if(domain==null || domain.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = service.countByDomain(domain);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
	@RequestMapping(value="directoryIsExistWithSelf")
	public void directoryIsExistWithSelf(HttpServletRequest request, HttpServletResponse response, Integer id){
		String domain = request.getParameter("directory");
		if(domain==null || domain.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = service.countByDirectoryWithSelf(id, domain);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
	@RequestMapping(value="directoryIsExist")
	public void directoryIsExist(HttpServletRequest request, HttpServletResponse response){
		String domain = request.getParameter("directory");
		if(domain==null || domain.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = service.countByDirectory(domain);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
	@RequestMapping(value="updateOpen")
	public void updateOpen(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status){
		if(id==null || status==null){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.updateOpen(id, status);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping("toSetSiteName")
	public String toSetSiteName(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			Map<String, Object> siteMap=this.service.load(siteId);
			if (siteMap!=null) {
				modelMap.put("name", siteMap.get("name"));
			}
            MsgConfig config = service.getSMSConfig(siteId);
            if(config != null) modelMap.put("phones", config.getPhone());
            modelMap.put("smallPicOriginalUrl", siteMap.get("smallPicOriginalUrl"));
		}
		return "site/site/setName";
	}
	@RequestMapping(value="updateSiteName")
	public void updateSiteName(HttpServletRequest request, HttpServletResponse response,String name, String phones,String smallPicOriginalUrl){
		Integer siteId=User.getCurrentSiteId(request);
		String result=Init.FAIL;
		if(siteId!=null && siteId>0 && !StringUtil.isEmpty(name)){
			Map<String, Object> siteMap=new HashMap<String, Object>();
			siteMap.put("id", siteId);
			siteMap.put("name", name);
			siteMap.put("smallPicOriginalUrl", smallPicOriginalUrl);
			int count= service.update(siteMap);
            if(StringUtil.isNotEmpty(phones)){
                String[] split = phones.split(",");
                String phone = "";
                for (int i = 0; i < split.length; i++) {
                    if(StringUtil.isNotEmpty(split[i]) && Pattern.matches("[0-9]{11}", split[i].trim())){
                        phone += (i > 0 ? "," : "") + split[i].trim();
                    }
                }
                if(StringUtil.isNotEmpty(phone)) service.saveOrUpdateSMSConfigPhone(siteId, phone);
            }
			result=count>0?Init.SUCCEED:Init.FAIL;
		}
		JSONUtil.print(response, result);
	}

    /**
     * 跳转到更新通知短信配置
     * @param id
     * @return
     */
    @RequestMapping("/toUpdateSMSConfig")
    public String toUpdateSMSConfig(ModelMap map, Integer id){
        map.put("siteId", id);
        MsgConfig config = service.getSMSConfig(id);
        map.put("config", config);
        return "site/site/sms";
    }

    /**
     * 保存通知短信配置
     */
    @RequestMapping("/updateSMSConfig")
    public void updateSMSConfig(HttpServletRequest request, HttpServletResponse response
            , Integer siteId, Integer number, String content){
        String result=Init.FAIL;
        if(siteId!=null && siteId>0){
            service.saveOrUpdateSMSConfig(siteId, number, content);
            result = Init.SUCCEED;
        }
        JSONUtil.print(response, result);
    }

    /**
     * 跳转编辑自定义新闻附加信息配置
     */
    @RequestMapping("/toEditArticleExtra")
    public String toEditArticleExtra(ModelMap modelMap, Integer id){
        List<ArticleExtra> articleExtraList = articleService.getArticleExtraList(id);
        modelMap.put("articleExtraList", articleExtraList);
        modelMap.put("siteId", id);
        return "site/site/articleExtra";
    }

    @RequestMapping("/editArticleExtra")
    public void editArticleExtra(HttpServletRequest request, HttpServletResponse response
            , Integer[] index, Integer siteId){
        String result=Init.FAIL;
        if(siteId!=null && siteId>0){
            List<Map<String, Object>> extraList = new ArrayList<Map<String, Object>>(index.length);
            Map<String, Object> extra;
            for (int i = 0; i < index.length; i++) {
                extra = new HashMap<String, Object>(3);
                String title = request.getParameter("title_"+index[i]);
                String field = request.getParameter("field_"+index[i]);
                if(StringUtil.isEmpty(title) || StringUtil.isEmpty(field)) continue;
                extra.put("title", title.trim());
                extra.put("field", field.trim());
                extraList.add(extra);
            }
            if(extraList.size() > 0) articleService.saveArticleExtraList(siteId, extraList);
            result = Init.SUCCEED;
        }
        JSONUtil.print(response, result);
    }

}
