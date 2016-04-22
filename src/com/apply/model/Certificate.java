package com.apply.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 证件类型
 * @author lfq
 *
 */
@Entity
@Table( name="apply_certificate")
public class Certificate  implements Serializable {
	
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Certificate.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Certificate.class.getSimpleName();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 证件名
	 */
	private String name;
	
	/**
	 * 证件备注
	 */
	private String remark;
	
	/**
	 * 所属站点siteId
	 */
	private Integer siteId;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
	
}
