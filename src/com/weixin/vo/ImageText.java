package com.weixin.vo;

/**
 * 图文消息
 * Created by dzf on 2015/7/13.
 */
public class ImageText {

    /**
     * 图文消息标题
     */
    private String title;

    /**
     * 图文消息描述
     */
    private String description;

    /**
     * 大图外链 320*200
     */
    private String bigPicUrl;

    /**
     * 小图外链 200*200
     */
    private String smallPicUrl;

    /**
     * 点击消息跳转地址
     */
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBigPicUrl() {
        return bigPicUrl;
    }

    public void setBigPicUrl(String bigPicUrl) {
        this.bigPicUrl = bigPicUrl;
    }

    public String getSmallPicUrl() {
        return smallPicUrl;
    }

    public void setSmallPicUrl(String smallPicUrl) {
        this.smallPicUrl = smallPicUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
