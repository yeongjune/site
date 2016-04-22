package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 联系方式
 * @author lfq
 *
 */
@Entity
@Table(name="site_contact")
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = Contact.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Contact.class.getSimpleName();
	}
	@Id
	@GeneratedValue
	private Integer id;
	
	//显示标题
	private String title;
	
	//联系方式:二维码、QQ、阿里旺旺、Skype、MSN、Email
	private String type;
	
	//联系值，如果为二维码是则为图片地址
	private String typeValue;
	
	//排序
	private Integer orderSort;
	
	//站点ID
	private Integer siteId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}
	public Integer getOrderSort() {
		return orderSort;
	}
	public void setOrderSort(Integer orderSort) {
		this.orderSort = orderSort;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
}
