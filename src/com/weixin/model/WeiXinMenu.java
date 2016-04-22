package com.weixin.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dzf on 2015/11/12.
 */
@Entity
@Table(name="weixin_menu")
public class WeiXinMenu implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<WeiXinMenu> clazz = WeiXinMenu.class;
        tableName = clazz.getAnnotation(Table.class).name();
        modelName = clazz.getSimpleName();
    }

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 网站id
     */
    private Integer siteId;

    /**
     * 父级id
     */
    private Integer pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     * 外部链接 (跳转)
     * 网站栏目 (跳转)
     * 网站拉取 (拉取)
     * 微信拉取 (拉取)
     * 素材拉取 (拉取)
     * 跳转图文 (跳转)
     */
    private String type;

    /**
     * 关联id
     * 当类型为 网站栏目 微信拉取 素材拉取 跳转图文 时有效
     * 网站栏目、微信拉取、栏目拉取：栏目id
     * 素材拉取、跳转图文：素材id 即media_id
     */
    private Integer relationId;

    /**
     * 外部链接的url
     */
    private String url;

    /**
     * 微信拉取 的数量
     */
    private Integer number;

    @Column(columnDefinition = "int default 0")
    private int sort = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
