package com.weixin.vo;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/10.
 */
public class Token {

    private String access_token;

    private int expires_in;

    private long create_time = new Date().getTime();

    public Token(String access_token, int expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    /**
     * 获取凭证
     * @return
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * 获取超时时间（秒）
     * @return
     */
    public int getExpires_in() {
        return expires_in;
    }

    public long getCreate_time() {
        return create_time;
    }
}
