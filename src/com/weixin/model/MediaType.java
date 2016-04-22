package com.weixin.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 素材类型
 * Created by dzf on 2015/11/13.
 */
@Entity
@Table(name="weixin_media_type")
public class MediaType implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<?> clazz = Media.class;
        tableName = clazz.getAnnotation(Table.class).name();
        modelName = clazz.getSimpleName();
    }

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 父级id，暂未使用
     */
    @Column(columnDefinition = "int default 0")
    private Integer pid = 0;

    /**
     * 网站id
     */
    private Integer siteId;

    /**
     * 分类名称
     */
    private String name;

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
}
