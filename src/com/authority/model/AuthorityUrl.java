package com.authority.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="authority_url")
public class AuthorityUrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	static{
		Table t = AuthorityUrl.class.getAnnotation(Table.class);
		tableName = t.name();
	}

	@Id
	private String id;
	private String url;
	private String pid;
	private Integer common;
	private Integer isPublic;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Integer getCommon() {
		return common;
	}
	public void setCommon(Integer common) {
		this.common = common;
	}
	public Integer getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
}
