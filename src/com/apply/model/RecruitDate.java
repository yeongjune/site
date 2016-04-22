package com.apply.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报名日期实体
 * @author lfq
 * @2014-5-10
 */
@Entity
@Table(name="apply_recruitDate")
public class RecruitDate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = RecruitDate.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = RecruitDate.class.getSimpleName();
	}
	/**
	 * 主键Id
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 报名开始日期
	 */
	private Date startDate;
	
	/**
	 * 报名截止日期
	 */
	private Date endDate;
	
	/**
	 * 答应准考证日期
	 */
	private Date printStartDate;
	
	/**
	 * 打印准考证截止日期
	 */
	private Date printEndDate;
	
	/**
	 * 站点siteId
	 */
	private Integer siteId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getPrintStartDate() {
		return printStartDate;
	}

	public void setPrintStartDate(Date printStartDate) {
		this.printStartDate = printStartDate;
	}

	public Date getPrintEndDate() {
		return printEndDate;
	}

	public void setPrintEndDate(Date printEndDate) {
		this.printEndDate = printEndDate;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
}
