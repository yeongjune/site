package com.site.vo;

/**
 * 文章查询Vo
 * @author lifaqiang
 *
 * 2014-3-14
 */
public class ArticleSearchVo {
	
	/**
	 * 站点ID
	 */
	private Integer siteId;
	
	/**
	 * 栏目ID
	 */
	private Integer columnId;
	
	/**
	 * 栏目ID，多个用逗号隔开，同时查询多个栏目的文章列表
	 */
	private String columnIds;
	
	/**
	 * 查询是否包含子节点的
	 */
	private boolean includeSub;
	/**
	 * 文章标题，模糊查询
	 */
	private String title;
	
	/**
	 * 文章内容，模糊查询
	 */
	private String content;
	
	/**
	 * 从第几条开始取记录 
	 * 没值或值小于等于1的则从第一条开始
	 */
	private Integer start;
	
	/**
	 * 查询列表时返回的记录数的限制
	 * 不传或传小于1的数返回所有
	 */
	private Integer limit;
	
	/**
	 * 排序规则
	 * 默认 0 最新的
	 * 	  1 最旧的
	 * 	  2 点击数
	 *    3 最后修改
	 */
	private Integer sortType = 0;
	
	/**
	 * 推荐位ID
	 */
	private Integer recommendId;

	/**
	 * 获取有图片的内容
	 */
	private boolean image = false;
	
	public boolean isIncludeSub() {
		return includeSub;
	}

	public void setIncludeSub(boolean includeSub) {
		this.includeSub = includeSub;
	}

	/**
	 * 关键字
	 */
	private String keyWord;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	public String getColumnIds() {
		return columnIds;
	}

	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}

	public Integer getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Integer recommendId) {
		this.recommendId = recommendId;
	}

	public boolean isImage() {
		return image;
	}

	public void setImage(boolean image) {
		this.image = image;
	}
}
