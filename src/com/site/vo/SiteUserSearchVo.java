package com.site.vo;

/**
 * 查询站点用户的参数Vo
 * @author lfq
 * @2014-5-7
 */
public class SiteUserSearchVo {
	
	/**
	 * 用户昵称，模糊查询
	 */
	private String name;
	
	/**
	 * 用户帐号，模糊查询
	 */
	private String acount;
	
	/**
	 * 关键字：模糊匹配 用户帐号、用户昵称、邮箱
	 */
	private String keyword;
	
	/**
	 * 用户状态：0待审核，-1禁用，1启用，-2审核不通过
	 */
	private Integer status;
	
	/**
	 * 用户帐号，完全匹配查询
	 */
	private String allAcount;
	
	/**
	 * 站点ID
	 */
	private Integer siteId;
	
	/**
	 * 起始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 部门
	 */
	private String department;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcount() {
		return acount;
	}

	public void setAcount(String acount) {
		this.acount = acount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAllAcount() {
		return allAcount;
	}

	public void setAllAcount(String allAcount) {
		this.allAcount = allAcount;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	
}
