package com.weixin.common;

import com.weixin.vo.Token;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzf on 2015/8/27.
 */
public class MediaUtil {

    /**
     * 上传其他素材
     * @param file
     * @param type
     *  媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param appId
     * @param appSecret
     * @return
     */
    public static JSONObject uploadOtherMedia(File file, String type, String appId, String appSecret){
        try {
            HttpClient client = HttpClients.createDefault();
            Token token = TokenFactory.get(appId, appSecret);
            HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("media", file).build();
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("access_token", token.getAccess_token()));
            params.add(new BasicNameValuePair("type", type));
            HttpPost post = new HttpPost("https://api.weixin.qq.com/cgi-bin/material/add_material?"+ URLEncodedUtils.format(params, "UTF-8"));
            post.setEntity(entity);
            HttpResponse httpResponse = client.execute(post);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String content = EntityUtils.toString(httpResponse.getEntity());
                return (JSONObject) new JSONTokener(content).nextValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject pushStr(String url, String body){
        try {
            HttpClient client = HttpClients.createDefault();
            StringEntity entity = new StringEntity(body, "UTF-8");
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            HttpResponse httpResponse = client.execute(post);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String content = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(content);
                return (JSONObject) new JSONTokener(content).nextValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean delete(String accessToken, String mediaId){
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("access_token", accessToken));
        String url = "https://api.weixin.qq.com/cgi-bin/material/del_material?" + URLEncodedUtils.format(params, "UTF-8");
        JSONObject body = new JSONObject();
        body.put("media_id", mediaId);
        JSONObject result = pushStr(url, body.toString());
        return result.containsKey("errcode") && "0".equals(result.get("errcode"));
    }

    public static JSONObject sendNewsAll(String accessToken, String mediaId){
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("access_token", accessToken));
        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?" + URLEncodedUtils.format(params, "UTF-8");
        JSONObject body = new JSONObject();
        JSONObject filter = new JSONObject();
        filter.put("is_to_all", true);
        body.put("filter", filter);
        JSONObject mpnews = new JSONObject();
        mpnews.put("media_id", mediaId);
        body.put("mpnews", mpnews);
        body.put("msgtype", "mpnews");
        return pushStr(url, body.toString());
    }

}
