package com.site.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 记录一人一票的投票记录
 * @author HJH
 *
 */
@Entity
@Table(name="site_article_clickcount")
public class ArticleClickCount implements Serializable{

	
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = ArticleClickCount.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = ArticleClickCount.class.getSimpleName();
	}
	
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 文章ID
	 */
	private Integer articleId;
	
	/**
	 * 用户ID
	 */
	private Integer userId;
	
	/**
	 * 栏目ID
	 */
	private Integer columnId;
	/**
	 * 手机号码
	 */
	private String mobilePhone;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}
	
	
}
