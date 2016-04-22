package com.site.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 站点相关统计数据
 * @author HJH
 *
 */
@Entity
@Table(name="site_statistics")
public class Statistics implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7743940155056040545L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Statistics.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Statistics.class.getSimpleName();
	}
	
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 站点ID
	 */
	private Integer siteId;
	
	/**
	 * 页面访问数PV
	 */
	@Column(columnDefinition="int default 0")
	private Integer pageView = 0;

	
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

	public Integer getPageView() {
		return pageView;
	}

	public void setPageView(Integer pageView) {
		this.pageView = pageView;
	}
	
	

}
