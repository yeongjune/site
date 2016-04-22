package com.tejiao.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 家长表，需要注册
 * Created by dzf on 15-12-28.
 */
@Entity
@Table(name="tejiao_parent")
public class Parent implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<?> clazz = Parent.class;
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
     * 账号
     */
    private String account;

    /**
     * 密码 md5
     */
    private String password;

    /**
     * 名称
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否已停用
     */
    @Column(columnDefinition = "int default 0")
    private int isStop = 0;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsStop() {
        return isStop;
    }

    public void setIsStop(int isStop) {
        this.isStop = isStop;
    }
}
