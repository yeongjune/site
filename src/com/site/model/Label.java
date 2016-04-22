	package com.site.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 标签，定义数据查询规则
 * @author Administrator
 *
 */
@Entity
@Table(name="site_label")
public class Label implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Label.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Label.class.getSimpleName();
	}
	
	public interface Item{
		// 栏目标签
		public final static String Column = "Column";
		// 推荐位标签
		public final static String Recommend = "Recommend";
		// 列表标签
		public final static String List = "List";
		// 位置标签
		public final static String Location = "Location";
		// 文章标签
		public final static String Article = "Article";
		// 菜单标签
		public final static String Menu = "Menu";
		// 单网页标签
		public final static String Single = "Single";
		// 友情链接
		public final static String Friendlylink = "Friendlylink";
		// 联系方式
		public final static String Contcat ="Contact";
        /** 留言板 **/
        public final static String Message = "Message";
	}
	
	@Id
	private String id;
	
	/**
	 * 标签名称
	 */
	private String name;
	
	/**
	 * 
	 * 自定义 0
	 * 系统内置标签 1
	 */
	private Integer state = 0;
	
	/**
	 * 标签说明
	 */
	@Column(columnDefinition = "text")
	private String info;
	
	/**
	 * 排序
	 */
	private Integer sort = 0;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
