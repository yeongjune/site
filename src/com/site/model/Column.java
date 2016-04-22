package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 栏目
 * @author Administrator
 *
 */
@Entity
@Table(name="site_column")
public class Column implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = Column.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Column.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 所属网站id
	 */
	private Integer siteId;
	/**
	 * 栏目名称
	 */
	private String name;
	/**
	 * 栏目类型；单页栏目、新闻栏目、外部栏目、图片栏目、下载栏目
	 */
	private String type = "新闻栏目";
	/**
	 * 栏目地址，外部栏目需要填写；其他类型栏目访问栏目模板页地址 + 栏目id
	 */
	private String url;
	/**
	 * 父级栏目id
	 */
	private Integer pid = 0;
	/**
	 * 排序
	 */
	private Integer sort = 0;
	
	/**
	 * 层级
	 */
	private Integer level = 0;
	
	/**
	 * 英文别名
	 */
	private String alias;
	/**
	 * 是否在导航显示，0：否；1：是
	 */
	private Integer navigation;
	
	/**
	 * 针对栏目需要使用图片情况，用样式代替，（暂时用src）
	 * 栏目图片
	 */
	private String className;
	
	/**
	 * 设置本栏目可选择进行投票的article数目
	 */
	private Integer voteNum = 0;
	
	/**
	 * 栏目每页显示新闻数，默认20
	 */
	private Integer pageSize = 20;
	
	/**
	 * 栏目描述
	 */
	private String description;

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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Integer getNavigation() {
		return navigation;
	}
	public void setNavigation(Integer navigation) {
		this.navigation = navigation;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getVoteNum() {
		return voteNum;
	}
	public void setVoteNum(Integer voteNum) {
		this.voteNum = voteNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
