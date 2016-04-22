package com.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 友情链接
 * @author Administrator
 *
 */
@Entity
@Table(name="site_links")
public class Links implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = Links.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Links.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * url
	 */
	private String url;
	/**
	 * 网站id
	 */
	private Integer siteId;
	/**
	 * 状态；0：关闭，1：开启
	 */
	private Integer status = 1;
	/**
	 * 发布时间
	 */
	private Date createTime;
	/**
	 * 是否死链;0:有效,1:死链
	 */
	private Integer effective;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setEffective(Integer effective) {
		this.effective = effective;
	}
	public Integer getEffective() {
		return effective;
	}
}
