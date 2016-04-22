package com.site.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 新闻的附加信息定义
 * Created by dzf on 2015/10/15.
 */
@Entity
@Table(name="site_article_extra")
public class ArticleExtra implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String tableName;
    public static final String modelName;
    static{
        Table t = ArticleExtra.class.getAnnotation(Table.class);
        tableName = t.name();
        modelName = ArticleExtra.class.getSimpleName();
    }

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 网站id
     */
    private Integer siteId;

    /**
     * 标题
     */
    private String title;

    /**
     * 字段key名
     */
    private String field;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
