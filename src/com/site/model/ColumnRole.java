package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 栏目角色
 * @author Administrator
 *
 */
@Entity
@Table(name="site_column_role")
public class ColumnRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	static{
		Table t = ColumnRole.class.getAnnotation(Table.class);
		tableName = t.name();
	}

	@Id
	@GeneratedValue
	private Integer id;
	private Integer siteId;
	private String name;
	private Integer sort;
	private Integer pid;

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
}
