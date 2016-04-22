package com.site.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 新闻跟推荐位中间关系表
 * @author lostself
 *
 */
@Entity
@Table(name = "site_article_recommend")
public class ArticleRecommend implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String modelName;
	public static final String tableName;
	static{
		Table t = ArticleRecommend.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = ArticleRecommend.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(nullable = true)
	private Integer articleId;
	
	@Column(nullable = true)
	private Integer recommendId;

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

	public Integer getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Integer recommendId) {
		this.recommendId = recommendId;
	}
}
