package com.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 站点用户实体
 * @author lfq
 * @2014-5-7
 */
@Entity
@Table(name="site_user")
public class SiteUser implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table t = SiteUser.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = SiteUser.class.getSimpleName();
	}
	
	/**
	 * 标识ID
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 登录帐号
	 */
	private String acount;
	
	/**
	 * 用户昵称
	 */
	private String name;
	
	/**
	 * 用户身份
	 */
	private String identity;
	
	/**
	 * 登录密码：md5加密
	 */
	private String password;
	
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 注册时间
	 */
	private Date createTime;
	
	/**
	 * 用户状态：-2审核不通过，-1禁用,0待审核，1启用
	 */
	private Integer status;
	
	/**
	 * 所在站点ID
	 */
	private Integer siteId;
	
	/**
	 * 所属部门或者班级
	 */
	private String department;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAcount() {
		return acount;
	}

	public void setAcount(String acount) {
		this.acount = acount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
}
