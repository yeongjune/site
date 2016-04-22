package com.weixin.common;

import com.weixin.vo.ImageText;

import java.util.Calendar;
import java.util.List;

/**
 * Created by dzf on 2015/7/13.
 */
public class MessageFactory {

    /**
     * 文字消息格式
     * @param to 接收者
     * @param from 发送者
     * @param content 内容
     * @return
     */
    public static String text(String to, String from, String content){
        long createTime = getTime();
        String msgType = "text";
        StringBuffer sb = new StringBuffer("<xml>");
        sb.append(XMLParser.node("ToUserName", to, true));
        sb.append(XMLParser.node("FromUserName", from, true));
        sb.append(XMLParser.node("CreateTime", String.valueOf(createTime), false));
        sb.append(XMLParser.node("MsgType", msgType, true));
        sb.append(XMLParser.node("Content", content, true));
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 图文信息格式
     * @return
     */
    public static String news(String to, String from, List<ImageText> textList){
        long createTime = getTime();
        String msgType = "news";
        StringBuffer sb = new StringBuffer("<xml>");
        sb.append(XMLParser.node("ToUserName", to, true));
        sb.append(XMLParser.node("FromUserName", from, true));
        sb.append(XMLParser.node("CreateTime", String.valueOf(createTime), false));
        sb.append(XMLParser.node("MsgType", msgType, true));

        sb.append(XMLParser.node("ArticleCount", String.valueOf(textList.size()), false));
        sb.append("<Articles>");
        for (int i = 0; i < textList.size(); i++) {
            ImageText text = textList.get(i);
            sb.append("<item>");
            sb.append(XMLParser.node("Title", text.getTitle(), true));
            sb.append(XMLParser.node("Description", text.getDescription(), true));
            if(text.getBigPicUrl() != null && text.getSmallPicUrl() != null ){
                sb.append(XMLParser.node("PicUrl", i > 0 ? text.getSmallPicUrl() : text.getBigPicUrl(), true));
            }else if(text.getBigPicUrl() != null){
                sb.append(XMLParser.node("PicUrl", text.getBigPicUrl(), true));
            }else if(text.getSmallPicUrl() != null){
                sb.append(XMLParser.node("PicUrl", text.getSmallPicUrl(), true));
            }
            sb.append(XMLParser.node("Url", text.getUrl(), true));
            sb.append("</item>");
        }
        sb.append("</Articles>");
        sb.append("</xml>");
        return sb.toString();
    }

    private static long getTime(){
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

}
