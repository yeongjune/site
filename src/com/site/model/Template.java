package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 页面模板；首页、栏目页、列表页、内容页
 * @author Administrator
 *
 */
@Entity
@Table(name="site_template")
public class Template implements Serializable{

	/**
	 * 模板id
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Template.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Template.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 站点id
	 */
	private Integer siteId;
	
	/**
	 * 模板名称
	 */
	private String name;
	
	/**
	 * 文件文件名
	 */
	private String fileName;
	
	/**
	 * 是否内置模板页面
	 * 默认0  否
	 * 1	  是
	 */
	private Integer state = 0;
	
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
