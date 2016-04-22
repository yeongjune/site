package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

/**
 * 数据源
 * @author Administrator
 *
 */
@Entity
@Table(name="site_data")
public class Data implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Data.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Data.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 数据源名称
	 */
	private String name;
	/**
	 * 标签，查询规则
	 */
	private String labelId;
	/**
	 * 查询记录数量
	 */
	private Integer size = 10;
	
	/**
	 * 批量栏目ID
	 * 默认：0 整站
	 * 		-1 传参
	 */
	private String columnIds = "0";
	
	/**
	 * 所属推荐位
	 */
	private Integer recommendId = 0;
	
	/**
	 * 网站id
	 */
	private Integer siteId = 0;
	
	/**
	 * 是否显示分页,默认不显示分页
	 * 如果为1 则页面取值结构为 pageList 结构
	 * 如果为0 则页面取值List<Map>结构
	 */
	private Integer displayPage = 0;
	
	/**
	 * 排序规则
	 * 默认 0 最新的
	 * 	  1 最旧的
	 * 	  2 点击数
	 *    3 最后修改
	 */
	private Integer sortType = 0;
	
	/**
	 * 显示新闻内容，会屏过滤掉html元素
	 * 默认 0 不显示
	 * 		显示字数
	 */
	private Integer displayContentSize = 0;
	
	/**
	 * 返回的数据是否为json数据格式:0为否，1为是
	 */
	private Integer isJsonData = 0;
	
	/**
	 * 是否为分栏目查询（如果分栏目将为每个栏目分别查询出指定的size数量，否则为查询所有选择了的栏目）
	 */
	private Integer everyColumn=0;

	/**
	 * 是否只获取有图片的数据
	 */
	@Column(columnDefinition = "int default 0")
	private Integer isImages = 0;

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
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getDisplayPage() {
		return displayPage;
	}
	public void setDisplayPage(Integer displayPage) {
		this.displayPage = displayPage;
	}
	public Integer getSortType() {
		return sortType;
	}
	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}
	public Integer getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(Integer recommendId) {
		this.recommendId = recommendId;
	}
	public String getColumnIds() {
		return columnIds;
	}
	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}
	public Integer getDisplayContentSize() {
		return displayContentSize;
	}
	public void setDisplayContentSize(Integer displayContentSize) {
		this.displayContentSize = displayContentSize;
	}
	public Integer getIsJsonData() {
		return isJsonData;
	}
	public void setIsJsonData(Integer isJsonData) {
		this.isJsonData = isJsonData;
	}
	public Integer getEveryColumn() {
		return everyColumn;
	}
	public void setEveryColumn(Integer everyColumn) {
		this.everyColumn = everyColumn;
	}

	public Integer getIsImages() {
		return isImages;
	}

	public void setIsImages(Integer isImages) {
		this.isImages = isImages;
	}
}
