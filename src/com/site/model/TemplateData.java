package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模板<数据源中间关系表
 * @author Administrator
 *
 */
@Entity
@Table(name="site_template_data")
public class TemplateData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = TemplateData.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = TemplateData.class.getSimpleName();
	}
	
	@Id
	@GeneratedValue	
	private Integer id;
	/**
	 * 数据源id
	 */
	private Integer dataId;
	/**
	 * 模板id
	 */
	private Integer templateId;
	
	/**
	 * 站点ID
	 */
	private Integer siteId;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDataId() {
		return dataId;
	}
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
}
