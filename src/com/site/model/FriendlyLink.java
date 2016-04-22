package com.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 友情链接
 * @author lfq
 *
 */
@Entity
@Table(name="site_friendly_link")
public class FriendlyLink implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = FriendlyLink.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = FriendlyLink.class.getSimpleName();
	}
	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 连接地址
	 */
	private String linkUrl;
	/**
	 * 站点ID
	 */
	private Integer siteId;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建人ID
	 */
	private Integer userId;
	/**
	 * 友情链接状态
	 */
	private int effective;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getEffective() {
		return effective;
	}
	
}
