package com.apply.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报名信息表
 * @author lfq
 * @time 2015-3-23
 *
 */
@Entity
@Table(name="apply_info")
public class ApplyInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = ApplyInfo.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = ApplyInfo.class.getSimpleName();
	}

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 实体名称
	 */
	private String entityName;
	
	/**
	 * 站点id
	 */
	private Integer siteId;
	
	/**
	 * 报名编号，同一个站点siteId下不能相同，用于同一个站点siteId下有多个报名:如01,02,03
	 */
	private String applyNo;
	
	/**
	 * 报名开始时间
	 */
	private Date startTime;
	
	/**
	 * 报名结束时间
	 */
	private Date endTime;
	
	/**
	 * 报名状态：0未开启报名，1已开启报名
	 */
	private Integer state;
	
	/**
	 * 报名的标题，如：2015年集贤小学一年级入学报名表
	 */
	private String title;
	
	/**
	 * 是否已删除:0为未删除，1为已删除
	 */
	private Integer isDelete=0;
	
	/**
	 * 报名说明，该备注会显示在报名界面的头部
	 */
	private String remark;

	/**
	 * 考试类型
	 * 1 初中
	 * 0 小学
	 */
	private Integer examType;
	
	/**
	 * 面试须知
	 */
	private String interviewNote;
	
	/**
	 * 是否开启打印
	 * 0 否
	 * 1 是
	 */
	private Integer isPrint = 0;
	
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

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getExamType() {
		return examType;
	}

	public void setExamType(Integer examType) {
		this.examType = examType;
	}

	public String getInterviewNote() {
		return interviewNote;
	}

	public void setInterviewNote(String interviewNote) {
		this.interviewNote = interviewNote;
	}

	public Integer getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(Integer isPrint) {
		this.isPrint = isPrint;
	}

}
