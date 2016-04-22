package com.weixin.action;

import com.authority.model.User;
import com.base.util.JSONUtil;
import com.base.util.ListUtils;
import com.base.util.StringUtil;
import com.site.service.ColumnService;
import com.site.service.SiteService;
import com.weixin.common.Configuration;
import com.weixin.common.ErrorCode;
import com.weixin.common.TokenFactory;
import com.weixin.model.Config;
import com.weixin.model.WeiXinMenu;
import com.weixin.service.ConfigService;
import com.weixin.service.MenuService;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/12.
 */
@Controller("weiXinMenuAction")
@RequestMapping("/weiXinMenu")
public class MenuAction {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SiteService siteService;

    @RequestMapping("/index")
    public String index(){
        return "weixin/menu/index";
    }

    @RequestMapping("/loadAll")
    public void loadAll(HttpServletRequest request, HttpServletResponse response){
        Integer siteId = User.getCurrentSiteId(request);
        List<Map<String, Object>> menuList = menuService.getAll(siteId);
        JSONUtil.printToHTML(response, menuList);
    }

    @RequestMapping("/add")
    public String add(ModelMap modelMap, HttpServletRequest request, @RequestParam(required = false) Integer pid){
        Integer siteId = User.getCurrentSiteId(request);
        if(pid != null && pid > 0)
            modelMap.put("pmenu", menuService.get(pid));
        // 网站栏目
        List<Map<String, Object>> columnList = columnService.getList(siteId);
        modelMap.put("columnList", columnList);
        return "weixin/menu/add";
    }

    @RequestMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response
            , @RequestParam(required = false) Integer pid, String type, String name
            , @RequestParam(required = false) String url, @RequestParam(required = false) Integer number
            , @RequestParam(required = false) Integer relationId){
        Integer siteId = User.getCurrentSiteId(request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 1);
        if(pid == null || pid <= 0 ) pid = 0;
        Integer count = menuService.getMenuCountByPid(siteId, pid);
        if(StringUtil.isEmpty(name)){
            result.put("msg", "名称不能为空");
        }else if(( pid == 0 && name.trim().getBytes().length >= 14)
                || (pid > 0 && name.trim().getBytes().length >= 40)){
            result.put("msg", "名称长度超出");
        }else if(( pid == 0 && count >= 3)
                || (pid > 0 && count >= 5)){
            result.put("msg", "菜单个数超出最大值");
        }else{
            WeiXinMenu menu = new WeiXinMenu();
            menu.setType(type);
            menu.setName(name);
            menu.setPid(pid);
            menu.setUrl(url);
            menu.setRelationId(relationId);
            menu.setNumber(number);
            menuService.save(menu);
            result.put("code", 0);
        }
        JSONUtil.printToHTML(response, result);
    }

    @RequestMapping("/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
        menuService.delete(id);
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, ModelMap modelMap, Integer id){
        Integer siteId = User.getCurrentSiteId(request);
        WeiXinMenu menu = menuService.get(id);
        modelMap.put("menu", menu);
        if(menu.getPid() != null && menu.getPid() > 0)
            modelMap.put("pmenu", menuService.get(menu.getPid()));
        // 网站栏目
        List<Map<String, Object>> columnList = columnService.getList(siteId);
        modelMap.put("columnList", columnList);
        return "weixin/menu/edit";
    }

    @RequestMapping("/update")
    public void update(HttpServletRequest request, HttpServletResponse response, Integer id
            , @RequestParam(required = false) Integer pid, String type, String name
            , @RequestParam(required = false) String url, @RequestParam(required = false) Integer number
            , @RequestParam(required = false) Integer relationId){
        Integer siteId = User.getCurrentSiteId(request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 1);
        if(pid == null || pid <= 0 ) pid = 0;
        if(StringUtil.isEmpty(name)){
            result.put("msg", "名称不能为空");
        }else if(( pid == 0 && name.trim().getBytes().length >= 14)
                || (pid > 0 && name.trim().getBytes().length >= 40)){
            result.put("msg", "名称长度超出");
        }else{
            WeiXinMenu menu = menuService.get(siteId, id);
            menu.setType(type);
            menu.setName(name);
            menu.setPid(pid);
            menu.setUrl(url);
            menu.setRelationId(relationId);
            menu.setNumber(number);
            menuService.update(menu);
            result.put("code", 0);
        }
        JSONUtil.printToHTML(response, result);
    }

    /**
     * 移动
     * @param id
     * @param tarId 目标id
     * @param type 相对目标而言，当前属性：0子节点 1 next  2 prev
     */
    @RequestMapping("/move")
    public void move(HttpServletResponse response, HttpServletRequest request, Integer id, Integer tarId, Integer type){
        Integer siteId = User.getCurrentSiteId(request);
        int count = menuService.updateSort(id, tarId, type, siteId);
        JSONUtil.print(response, String.valueOf(count));
    }

    /**
     * 生成微信菜单
     */
    @RequestMapping("/generate")
    public void generate(HttpServletRequest request, HttpServletResponse response){
        Integer siteId = User.getCurrentSiteId(request);
        List<Map<String, Object>> menuList = menuService.getAll(siteId);
        Map<Object, List<Map<String, Object>>> listMap = ListUtils.classifyMapList("pid", menuList);
        List<Map<String, Object>> oneMenu = listMap.get(0);
        if(oneMenu != null && oneMenu.size() > 0){
            Map<String, Object> menu = new HashMap<String, Object>(1);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(3);
            menu.put("button", list);
            for (Map<String, Object> map : oneMenu) {
                List<Map<String, Object>> twoMenu = listMap.get(map.get("id"));
                if(twoMenu != null && twoMenu.size() > 0){
                    Map<String, Object> m = new HashMap<String, Object>(2);
                    m.put("name", map.get("name"));
                    List<Map<String, Object>> twoList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> twoMap : twoMenu)
                        twoList.add(transformMap(twoMap));
                    m.put("sub_button", twoList);
                    list.add(m);
                }else{
                    list.add(transformMap(map));
                }
            }
            System.out.println(menu);
            // 提交请求
            try{
                HttpClient client = HttpClients.createDefault();
                List<NameValuePair> params = new ArrayList<NameValuePair>(3);
                Config config = configService.getObjBySiteId(siteId);
                params.add(new BasicNameValuePair("access_token"
                        , TokenFactory.get(config.getAppID(), config.getAppsecret()).getAccess_token()));
                HttpPost httpPost = new HttpPost(Configuration.Urls.CREATE_MENU + "?" + URLEncodedUtils.format(params, "UTF-8"));
                StringEntity entity = new StringEntity(JSONObject.fromObject(menu).toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                HttpResponse resp = client.execute(httpPost);
                if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    String result = EntityUtils.toString(resp.getEntity());
                    JSONObject json = JSONObject.fromObject(result);
                    json.put("errmsg", ErrorCode.get(json.getInt("errcode"), "生成失败"));
                    JSONUtil.printToHTML(response, json);
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        JSONUtil.print(response, "error");
    }

    private Map<String, Object> transformMap(Map<String, Object> menu){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("name", menu.get("name"));
        String type = (String) menu.get("type");
        if(type.equals("外部链接")){
            result.put("type", "view");
            result.put("url", menu.get("url"));
        }else if(type.equals("网站栏目")){
            result.put("type", "view");
            Integer relationId = (Integer) menu.get("relationId");
            Map<String, Object> column = columnService.get(relationId);
            Map<String, Object> site = siteService.load((Integer) column.get("siteId"));
            String url = site.get("domain").toString()+ "/" + column.get("url").toString();
            url = url.replaceAll("http://", "").replaceAll("/+", "/");
            result.put("url", "http://" + url);
        }else if(type.equals("网站拉取")){
            result.put("type", "click");
            Integer relationId = (Integer) menu.get("relationId");
            Integer number = (Integer) menu.get("number");
            Integer siteId = (Integer) menu.get("siteId");
            result.put("key", "ColumnLaQue-" + siteId + "-" + relationId + "-" + number);
        }else if(type.equals("微信拉取")){
            //TODO 微信拉取的菜单类型实现
            result.put("type", "click");
            result.put("key", "WeiXinLaQue");
        }else if(type.equals("素材拉取")){
            result.put("type", "media_id");
            result.put("media_id", menu.get("relationId"));
        }else if(type.equals("跳转图文")){
            result.put("type", "view_limited");
            result.put("media_id", menu.get("relationId"));
        }
        return result;
    }


    @RequestMapping("/drop")
    @ResponseBody
    public String delete(HttpServletRequest request){
        Integer siteId = User.getCurrentSiteId(request);
        Config config = configService.getObjBySiteId(siteId);
        try{
            HttpClient client = HttpClients.createDefault();
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("access_token", TokenFactory.get(config.getAppID(), config.getAppsecret()).getAccess_token()));
            HttpPost httpPost = new HttpPost(Configuration.Urls.DELETE_MENU + "?" + URLEncodedUtils.format(params, "UTF-8"));
            HttpResponse response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                return EntityUtils.toString(response.getEntity());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response){
        Integer siteId = User.getCurrentSiteId(request);
        Config config = configService.getObjBySiteId(siteId);
        try{
            HttpClient client = HttpClients.createDefault();
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("access_token", TokenFactory.get(config.getAppID(), config.getAppsecret()).getAccess_token()));
            HttpPost httpPost = new HttpPost(Configuration.Urls.QUERY_MENU + "?" + URLEncodedUtils.format(params, "UTF-8"));
            HttpResponse resp = client.execute(httpPost);
            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                JSONUtil.printToHTML(response, EntityUtils.toString(resp.getEntity()));
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONUtil.printToHTML(response, "error");
    }

}
