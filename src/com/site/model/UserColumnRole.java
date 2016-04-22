package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="site_user_column_role")
public class UserColumnRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	static{
		Table t = UserColumnRole.class.getAnnotation(Table.class);
		tableName = t.name();
	}

	@Id
	@GeneratedValue
	private Integer id;
	private Integer userId;
	private Integer roleId;
	private Integer siteId;
	private Integer checked;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getChecked() {
		return checked;
	}
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
}
