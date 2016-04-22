package com.apply.vo;


public class StudentSearchVo {
	
	/**
	 * 学生Id，多个用逗号隔开
	 */
	private String ids;
	
	/**
	 * 学生姓名，模糊查询
	 */
	private String name;
	
	/**
	 * 学生报名时间：始
	 */
	private String startTime;
	
	/**
	 * 学生报名时间：止
	 */
	private String endTime;
	
	/**
	 * 状态
	 */
	private Integer status;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 站点siteId
	 */
	private Integer siteId;
	
	/**
	 * 是否参加面试；0：否、1：是
	 */
	private Integer interview;
	
	/**
	 * 是否录取：0否，1是 
	 */
	private Integer admit;

	/**
	 * 毕业学校
	 */
	private String graduate;
	
	/**
	 * 最小总分
	 */
	private Double minTotal;
	
	/**
	 * 最大总分
	 */
	private Double maxTotal;
	
	/**
	 * 户籍关键字(不分省、市、区、域)
	 */
	private String domicile;
	
	/**
	 * 面试日期时间，完全匹配 格式:yyyy-MM-dd HH:mm
	 */
	private String interiewDate;
	
	/**
	 * 面试年份
	 */
	private String examYear;
	
	/**
	 * 面试时间是否能为空（不能为空，表示查询时为空的不查询出来）
	 */
	private boolean canInterviewDateNull = true;
	
	/**
	 * 是否已经设置考室
	 * 0	不限
	 * 1	已设置
	 * 2	未设置
	 */
	private Integer isSetRoomNo;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}


	public Integer getInterview() {
		return interview;
	}

	public void setInterview(Integer interview) {
		this.interview = interview;
	}

	public String getGraduate() {
		return graduate;
	}

	public void setGraduate(String graduate) {
		this.graduate = graduate;
	}

	public Double getMinTotal() {
		return minTotal;
	}

	public void setMinTotal(Double minTotal) {
		this.minTotal = minTotal;
	}

	public Double getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Double maxTotal) {
		this.maxTotal = maxTotal;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public Integer getAdmit() {
		return admit;
	}

	public void setAdmit(Integer admit) {
		this.admit = admit;
	}

	public String getInteriewDate() {
		return interiewDate;
	}

	public void setInteriewDate(String interiewDate) {
		this.interiewDate = interiewDate;
	}

	public String getExamYear() {
		return examYear;
	}

	public void setExamYear(String examYear) {
		this.examYear = examYear;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public boolean isCanInterviewDateNull() {
		return canInterviewDateNull;
	}

	public void setCanInterviewDateNull(boolean canInterviewDateNull) {
		this.canInterviewDateNull = canInterviewDateNull;
	}

	public Integer getIsSetRoomNo() {
		return isSetRoomNo;
	}

	public void setIsSetRoomNo(Integer isSetRoomNo) {
		this.isSetRoomNo = isSetRoomNo;
	}

}
