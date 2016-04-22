package com.apply.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 考试 实体
 * 用于保存每场考试的信息
 * @author lfq
 * @2014-5-10
 */
/**
 *
 * @author lfq
 * @2014-5-10
 */
@Entity
@Table(name="apply_exam")
public class Exam   implements Serializable{
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Exam.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Exam.class.getSimpleName();
	}
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 考试名
	 */
	private String examName;
	
	/**
	 * 考区，丢弃
	 */
	private String examArea;
	
	/**
	 * 考场数量
	 */
	private Integer roomCount=0;
	
	/**
	 * 每个试室座位数:默认为30
	 */
	private Integer everyRoomSeatCount;
	
	/**
	 * 考试日期
	 */
	private Date examDate;
	
	/**
	 * 考试时间
	 */
	private String examTime;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 站点ID
	 */
	private Integer siteId;

	/**
	 * 考试类型
	 * 0	小学入学考试
	 * 1	中学入学考试
	 */
	private Integer examType = 0;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getExamArea() {
		return examArea;
	}

	public void setExamArea(String examArea) {
		this.examArea = examArea;
	}

	public Integer getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getExamTime() {
		return examTime;
	}

	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}

	public Integer getEveryRoomSeatCount() {
		return everyRoomSeatCount;
	}

	public void setEveryRoomSeatCount(Integer everyRoomSeatCount) {
		this.everyRoomSeatCount = everyRoomSeatCount;
	}

	public Integer getExamType() {
		return examType;
	}

	public void setExamType(Integer examType) {
		this.examType = examType;
	}
	
}
