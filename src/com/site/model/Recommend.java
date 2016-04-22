package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推荐位表
 * @author lostself
 */
@Entity
@Table(name="site_recommend")
public class Recommend implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String tableName;
	public static final String modelName;
	static{
		Table t = Recommend.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName =Recommend.class.getSimpleName();
	}
	
	@Id
	@GeneratedValue
	private Integer id;
	
	
	/**
	 * 站点ID
	 */
	private Integer siteId;
	
	/**
	 * 栏目ID
	 * default 0 不限制
	 */
	private Integer columnId = 0;
	
	/**
	 * 推荐位名称
	 */
	private String name; 

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

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
