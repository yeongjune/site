package com.weixin.event;

import com.weixin.annotation.EventState;
import com.weixin.common.MessageFactory;
import com.weixin.vo.ImageText;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/7/13.
 */
public class ClickEvent {

    public static String push(Map<String, Object> xmlMap){
        String EventKey = (String) xmlMap.get("EventKey");
        String[] split = EventKey.split("-");
        String key = split[0];
        Class<ClickEvent> eventClass = ClickEvent.class;
        Method[] methods = eventClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            EventState state = method.getAnnotation(EventState.class);
            if(state != null && state.type() != null && state.value() != null
                    && state.value().length > 0 && state.type().equals("CLICK")
                    && state.value()[0].trim().equals(key)){
                try {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object[] params = new Object[parameterTypes.length];
                    params[0] = xmlMap;

                    return (String) method.invoke(null, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    @EventState("ColumnLaQue")
    public static String activityConsultation(Map<String, Object> xmlMap){
        String myId = (String) xmlMap.get("ToUserName");
        String userId = (String) xmlMap.get("FromUserName");
        List<ImageText> list = new ArrayList<ImageText>(5);
        String[] images = {"http://120.25.225.155/image/1.png", "http://120.25.225.155/image/2.png"};
        for (int i = 0; i < 5; i++) {
            ImageText it = new ImageText();
            it.setBigPicUrl(images[0]);
            it.setSmallPicUrl(images[1]);
            it.setTitle("这是很好的新闻" + i);
            it.setDescription("都说了是好新闻，你们怎么就不相信呢，现在信了吧？");
            it.setUrl("http://www.baidu.com");
            list.add(it);
        }
        return MessageFactory.news(userId, myId, list);
    }

}
