package com.apply.vo;

/**
 * 长兴中学借读生查询Vo
 * @author lfq
 * @time 2015-3-24
 *
 */
public class CxStudentSearchVo {
	/**
	 * 站点id
	 */
	private Integer siteId;
	/**
	 * 页码大小
	 */
	private Integer pageSize;
	/**
	 * 当前页
	 */
	private  Integer currentPage;
	/**
	 * 姓名/身份证 模糊匹配
	 */
	private String keyword;
	/**
	 * 报名年份格式yyyy
	 */
	private String year;
	/**
	 * 性别：男/女
	 */
	private String gender;
	
	/**
	 * 学生id
	 */
	private Integer [] ids;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}
}
