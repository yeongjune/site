package com.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 留言实体
 * 
 * @author ah
 *
 */
@Entity
@Table(name="site_message")
public class Message implements Serializable{
	
	private static final long serialVersionUID = -2458371222091691440L;
	public static final String modelName;
	public static final String tableName;
	
	static{
		Table t = Message.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Message.class.getSimpleName();
	}
	
	/**
	 * id
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 站点id
	 */
	private Integer siteId;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 电话
	 */
	private String phone;
	
	/**
	 * 留言内容
	 */
	private String content;
	
	/**
	 * 联系地址
	 */
	private String address;
	
	/**
	 * 留言类型,1为匿名,0为不匿名
	 */
	private Integer type = 0;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 回复
	 */
	private String reply;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
