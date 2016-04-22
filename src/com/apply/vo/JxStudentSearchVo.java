package com.apply.vo;

/**
 * 集贤小学报名学生查询Vo
 * @author lfq
 * @time 2015-3-20
 *
 */
public class JxStudentSearchVo {
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
	 * 地段生：是/否
	 */
	private String dds;
	/**
	 * 业主生：是/否
	 */
	private String yzs;
	/**
	 * 广州户籍非地段生：是/否
	 */
	private String gzhjfdds;
	/**
	 * 非广州户籍学生：是/否
	 */
	private String fgzhjxs;
	
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

	public String getDds() {
		return dds;
	}

	public void setDds(String dds) {
		this.dds = dds;
	}

	public String getYzs() {
		return yzs;
	}

	public void setYzs(String yzs) {
		this.yzs = yzs;
	}

	public String getGzhjfdds() {
		return gzhjfdds;
	}

	public void setGzhjfdds(String gzhjfdds) {
		this.gzhjfdds = gzhjfdds;
	}

	public String getFgzhjxs() {
		return fgzhjxs;
	}

	public void setFgzhjxs(String fgzhjxs) {
		this.fgzhjxs = fgzhjxs;
	}

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}
	
	
	
}
