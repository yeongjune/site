package com.tejiao.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dzf on 15-12-29.
 */
@Entity
@Table(name="tejiao_user_type")
public class UserType implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<?> clazz = UserType.class;
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
     * 用户id
     */
    private Integer userId;

    /**
     * 是否为区审核人，否则为镇街审核
     */
    @Column(columnDefinition = "int default 0")
    private int isAdmin = 0;

    /**
     * 审核镇街
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

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
