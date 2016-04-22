package com.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="site_article")
public class Article implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = Article.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Article.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 副标题
	 */
	private String title2;
	/**
	 * 内容
	 */
	@Column(columnDefinition="longtext")
	private String content;
	/**
	 * 发布时间
	 */
	private Date createTime = new Date();
	/**
	 * 发布人id
	 */
	private String userId;
	/**
	 * 网站id
	 */
	private Integer siteId;
	/**
	 * 栏目id
	 */
	private Integer columnId;
	/**
	 * 点击数
	 */
	private Long clickCount = 0L;
	
	/**
	 * 浏览数
	 */
	@Column(columnDefinition = "BIGINT default 0")
	private Long viewCount = 0L;
	
	
	/**
	 * 缩略图地址
	 */
	private String smallPicUrl;
	
	/**
	 * 缩略图原图地址:一般用于头部展示，或首页
	 */
	private String smallPicOriginalUrl;
	
	/**
	 * 临时文件关联的ID
	 */
	private String tempId;
	
	/**
	 * 排序时间，默认是新增的时间
	 * */
	private Date updateTime;
	
	/**
	 * 最迟有效日期
	 */
	private Date lastTime;
	
	/**
	 * 简介
	 */
	private String introduce;
	
	/**
	 * 设置新旧状态，1为新，0为无，默认0
	 */
	private Integer status = 0;
	
	/**
	 * 审核状态 
	 * 0 ，无需审核
	 * 1 ，需要审核(待审核)
	 * 10，审核不通过
	 * 11，审核通过
	 */
	@Column(columnDefinition = "int default 0")
	private int checked = 0;

    /**
     * 附加信息 JSONObject
     */
    @Column(columnDefinition="longtext")
    private String extra;

	public String getSmallPicUrl() {
		return smallPicUrl;
	}
	public void setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getColumnId() {
		return columnId;
	}
	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}
	public Long getClickCount() {
		return clickCount;
	}
	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	
	public Long getViewCount() {
		return viewCount;
	}
	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public String getSmallPicOriginalUrl() {
		return smallPicOriginalUrl;
	}
	public void setSmallPicOriginalUrl(String smallPicOriginalUrl) {
		this.smallPicOriginalUrl = smallPicOriginalUrl;
	}

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
