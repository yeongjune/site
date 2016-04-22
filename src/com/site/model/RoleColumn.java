package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 栏目角色与栏目关系表
 * @author Administrator
 *
 */
@Entity
@Table(name="site_column_role_column")
public class RoleColumn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	static{
		Table t = RoleColumn.class.getAnnotation(Table.class);
		tableName = t.name();
	}

	@Id
	@GeneratedValue
	private Integer id;
	private Integer siteId;
	private Integer roleId;
	private Integer columnId;
	private Integer checked;

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
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getColumnId() {
		return columnId;
	}
	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}
	public Integer getChecked() {
		return checked;
	}
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
}
