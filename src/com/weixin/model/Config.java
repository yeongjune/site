package com.weixin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by dzf on 2015/11/13.
 */
@Entity
@Table(name="weixin_config")
public class Config implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<Config> clazz = Config.class;
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
     * 微信公众号字段
     */
    private String appID;

    /**
     * 微信公众号字段
     */
    private String appsecret;

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

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }
}
