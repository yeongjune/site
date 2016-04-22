package com.weixin.action;

import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.service.ArticleService;
import com.site.service.ColumnService;
import com.site.service.SiteService;
import com.site.vo.ArticleSearchVo;
import com.weixin.common.*;
import com.weixin.event.ClickEvent;
import com.weixin.vo.ImageText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/7/10.
 */
@Controller("WeiXinMessageAction")
@RequestMapping("/weiXinMsg")
public class MessageAction {

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SiteService siteService;

    @RequestMapping(value = "/into", method = RequestMethod.GET)
    public void into(HttpServletRequest request, HttpServletResponse response){
        Map<String, String[]> map = request.getParameterMap();
        if(map.containsKey("signature") && map.containsKey("timestamp")
                && map.containsKey("nonce") && map.containsKey("echostr")){
            String signature = request.getParameter("signature");
            String echostr = request.getParameter("echostr");
            Map<String, Object> checkMap = new HashMap<String, Object>();
            checkMap.put("token", Configuration.TOKEN);
            checkMap.put("timestamp", request.getParameter("timestamp"));
            checkMap.put("nonce", request.getParameter("nonce"));
            try {
                String sign = Signature.getSign(checkMap);
                if(sign.equals(signature)){
                    JSONUtil.print(response, echostr);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    @RequestMapping(value = "/into", method = RequestMethod.POST)
    public void intoPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/xml");

        // 微信加密签名
        String msg_signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");

        //从请求中读取整个post数据
        String xmlStr = Util.inputStreamToString(request.getInputStream());
        try {
            Map<String, Object> xmlMap = XMLParser.getMapFromXML(xmlStr);
            if(xmlMap.get("MsgType").equals("event")){// 事件类型
                if(xmlMap.get("Event").equals("CLICK")){// 点击事件
                    String EventKey = (String) xmlMap.get("EventKey");
                    String myId = (String) xmlMap.get("ToUserName");
                    String userId = (String) xmlMap.get("FromUserName");
                    String[] split = EventKey.split("-");
                    String key = split[0];
                    if("ColumnLaQue".equals(key)){
                        Integer siteId = Integer.valueOf(split[1]);
                        Integer columnId = Integer.valueOf(split[2]);
                        Integer pageSize = Integer.valueOf(split[3]);
                        if(pageSize == null || pageSize < 1 || pageSize > 10) pageSize = 10;

                        Map<String, Object> site = siteService.load(siteId);
                        ArticleSearchVo searchVo=new ArticleSearchVo();
                        searchVo.setColumnId(columnId);
                        searchVo.setSiteId(siteId);
                        searchVo.setIncludeSub(true);
                        searchVo.setSortType(3);//排序类型默认为按照更新时间
                        PageList pageList =articleService.findArticlePageList(1, pageSize, searchVo, false);
                        List<Map<String, Object>> articleList = pageList.getList();
                        List<ImageText> list = new ArrayList<ImageText>(5);
                        for (int i = 0; i < articleList.size(); i++) {
                            ImageText it = new ImageText();
                            Map<String, Object> article = articleList.get(i);
                            String path = (String) article.get("smallPicUrl");
                            if(StringUtil.isNotEmpty(path)){
                                String bigPicUrl = site.get("domain").toString()+ "/" + path;
                                bigPicUrl = "http://" + bigPicUrl.replaceAll("http://", "").replaceAll("/+", "/");
                                it.setBigPicUrl(bigPicUrl);
                                it.setSmallPicUrl(bigPicUrl);
                            }
                            it.setTitle((String) article.get("title"));
                            it.setDescription((String) article.get("title2"));
                            String columnType = (String) article.get("columnType");
                            String url = "";
                            if("单网页".equals(columnType)){
                                url = "/single-" + article.get("id") + ".html";
                            }else{
                                url = "/article-" + article.get("id") + ".html";
                            }
                            String webUrl = site.get("domain").toString() + url;
                            webUrl = "http://" + webUrl.replaceAll("http://", "").replaceAll("/+", "/");
                            it.setUrl(webUrl);
                            list.add(it);
                        }
                        JSONUtil.print(response, MessageFactory.news(userId, myId, list));
                    }
                }else if(xmlMap.get("Event").equals("subscribe")){// 关注推送

                }
            }else if(xmlMap.get("MsgType").equals("text")){// 发送消息回复推送

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
