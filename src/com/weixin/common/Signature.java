package com.weixin.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by dzf on 2015/7/10.
 */
public class Signature {

    public static String getSign(Map<String,Object> map) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(!entry.getValue().toString().equals("")){
                list.add(entry.getValue().toString());
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        //Util.log("Sign Before MD5:" + result);
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        byte[] end = digest.digest(result.getBytes());
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < end.length; i++) {
            res.append(Integer.toString((end[i] & 0xff) + 0x100, 16).substring(1));
        }
        return res.toString();
    }

}
