package com.tejiao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by dzf on 16-1-22.
 */
@Entity
@Table(name="tejiao_attachment")
public class Attachment implements Serializable {

    public static final String tableName;
    public static final String modelName;

    static {
        Class<?> clazz = Attachment.class;
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
     * 申报id
     */
    private Integer declareId;

    /**
     * 文件名
     */
    private String name;

    /***
     * 后缀名
     */
    private String ext;

    /**
     * 相对于根目录 例如: upload/username/user/excel.xsl
     */
    private String path;

    /**
     * 上传时间
     */
    private Date date = new Date();

    /**
     * 备注
     */
    private String remark;

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

    public Integer getDeclareId() {
        return declareId;
    }

    public void setDeclareId(Integer declareId) {
        this.declareId = declareId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
