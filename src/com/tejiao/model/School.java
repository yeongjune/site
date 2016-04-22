package com.tejiao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 申报学校
 * Created by dzf on 15-12-28.
 */
@Entity
@Table(name="tejiao_school")
public class School implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<?> clazz = School.class;
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
     * 学校名称
     */
    private String name;

    /**
     * 区县代号
     */
    private String area;

    /**
     * 学校代号
     */
    private String code;

    /**
     * 所属镇街
     */
    private String town;

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

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
