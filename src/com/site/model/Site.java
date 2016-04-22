package com.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="site_site")
public class Site implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Site.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Site.class.getSimpleName();
	}

	/**
	 * 站点id
	 */
	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 域名
	 */
	private String domain;
	/**
	 * 页面文件目录
	 */
	private String directory;
	/**
	 * 是否开启
	 */
	private Integer open;
	/**
	 * 站点创建时间
	 */
	private Date createTime = new Date();
	/**
	 * 修改时间
	 */
	private Date updateTime;
	
	/**
	 * 是否开启发布文章审核
	 * 0 ，关闭
	 * 1 ，开启审核
	 */
	@Column(columnDefinition = "int default 0")
	private int isUseCheck = 0;
	/**
	 * 水印图
	 */
	private String smallPicOriginalUrl;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public Integer getOpen() {
		return open;
	}
	public void setOpen(Integer open) {
		this.open = open;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getIsUseCheck() {
		return isUseCheck;
	}
	public void setIsUseCheck(int isUseCheck) {
		this.isUseCheck = isUseCheck;
	}
	public void setSmallPicOriginalUrl(String smallPicOriginalUrl) {
		this.smallPicOriginalUrl = smallPicOriginalUrl;
	}
	public String getSmallPicOriginalUrl() {
		return smallPicOriginalUrl;
	}
}
