package com.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 存放图片栏目的图片 
 * @author lostself
 */
@Entity
@Table(name = "site_image")
public class Image implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String modelName;
	public static final String tableName;
	
	static{
		Table t = Image.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Image.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 新闻ID
	 */
	private Integer articleId;
	
	/**
	 * 图片新闻临时ID,上传完图片时就会插入一条图片信息
	 */	
	private String tempId;
	
	/**
	 * 图片访问相对路径
	 * 譬如 /img/2013/2/id/aaa.jpgs
	 */
	@Column(nullable = false)
	private String path;
	
	/**
	 * 大小
	 */
	private Long size;
	
	/**
	 * 日期
	 */
	private Date createTime = new Date();
	
	/**
	 * 文件名，不包含路径
	 */
	private String fileName;
	
	/**
	 * 分类：1、图片类型的新闻图片文件； 2、新闻的缩略图文件； 3、新闻的附近文件，4、其他文件 ；
	 */
	private Integer type;
	
	/**
	 * 图片连接到的地址
	 */
	private String href;
	
	/**
	 * 图片描述
	 */
	private String description;

    /**
     * 星级数
     */
    private Integer stars;

    /**
     * 点赞数
     */
    @Column(columnDefinition = "int default 0")
    private int smile = 0;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public int getSmile() {
        return smile;
    }

    public void setSmile(int smile) {
        this.smile = smile;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
}
