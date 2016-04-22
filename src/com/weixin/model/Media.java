package com.weixin.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 素材
 * Created by dzf on 2015/11/13.
 */
@Entity
@Table(name="weixin_media")
public class Media implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<?> clazz = Media.class;
        tableName = clazz.getAnnotation(Table.class).name();
        modelName = clazz.getSimpleName();
    }

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 网站id
     */
    private Integer siteId;

    /**
     * 素材类型id
     */
    private Integer typeId;

    /**
     * 图文信息素材id
     */
    private String mediaId;

    /**
     * 缩略图素材id 必须
     */
    private String thumbMediaId;

    /**
     * 文件路径
     */
    private String thumbPath;

    /**
     * 标题 必须
     */
    private String title;

    /**
     * 内容 必须
     */
    @Column(columnDefinition = "text")
    private String content;

    /**
     * 作者 可选
     */
    private String author;

    /**
     * 原文地址 可选
     */
    private String contentSourceUrl;

    /**
     * 描述 可选
     */
    private String digest;

    /**
     * 是否显示封面,1为显示，0为不显示 可选
     */
    @Column(columnDefinition="int default 0")
    private Integer showCoverPic = 0;

    /**
     * 素材创建时间
     */
    private String createMediaTime;

    /**
     * 消息创建时间
     */
    private Date createTime;

    @Column(columnDefinition = "int default 0")
    private int sort = 0;

    /**
     * 微信
     */
    private String mediaUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Integer getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(Integer showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String getCreateMediaTime() {
        return createMediaTime;
    }

    public void setCreateMediaTime(String createMediaTime) {
        this.createMediaTime = createMediaTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
