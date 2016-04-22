package com.weixin.common;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;

/**
 * 请求接口管理
 * Created by dzf on 2015/7/17.
 */
public class InterfaceUtil {

    /**
     * 获取用户信息接口
     * @param token 网页版授权
     * @param openid 用户openid
     * @return
     */
    public static Map<String, Object> getUserInfo(String token, String openid){
        try{
            HttpClient client = HttpClients.createDefault();
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("access_token", token));
            params.add(new BasicNameValuePair("openid", openid));
            params.add(new BasicNameValuePair("lang", "zh_CN"));
            HttpPost httpPost = new HttpPost(Configuration.Urls.WEB_GET_USER_INFO + "?" + URLEncodedUtils.format(params, "UTF-8"));
            httpPost.addHeader("Content-Type", "text/html;charset=utf-8");
            HttpResponse response = client.execute(httpPost);
            Header[] headers = response.getAllHeaders();
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String content = EntityUtils.toString(response.getEntity());

                JSONObject obj = (JSONObject) new JSONTokener(new String(content.getBytes("ISO-8859-1"),"UTF-8")).nextValue();
                Map<String, Object> result = new HashMap<String, Object>();
                Iterator keys = obj.keys();
                while (keys.hasNext()){
                    String key = keys.next().toString();
                    result.put(key, obj.get(key));
                }
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户信息接口
     * @param webToken 获取到的webtoken信息集合
     * @return
     */
    public static Map<String, Object> getUserInfo(Map<String, Object> webToken){
        String token = webToken.get("access_token").toString();
        String openid = webToken.get("openid").toString();
        return getUserInfo(token, openid);
    }
    
    public static String getEncoding(String str) {      
        String encode = "GB2312";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s = encode;      
               return s;      
            }      
        } catch (Exception exception) {      
        }      
        encode = "ISO-8859-1";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s1 = encode;      
               return s1;      
            }      
        } catch (Exception exception1) {      
        }      
        encode = "UTF-8";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s2 = encode;      
               return s2;      
            }      
        } catch (Exception exception2) {      
        }      
        encode = "GBK";      
       try {      
           if (str.equals(new String(str.getBytes(encode), encode))) {      
                String s3 = encode;      
               return s3;      
            }      
        } catch (Exception exception3) {      
        }      
       return "";      
    } 

}
