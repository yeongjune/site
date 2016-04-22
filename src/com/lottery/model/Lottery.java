package com.lottery.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lottery")
public class Lottery implements Serializable{

	private static final long serialVersionUID = 4007084283457731860L;
	public static final String tableName;
	public static final String modelName;
	static{
		Table table = Lottery.class.getAnnotation(Table.class);
		tableName = table.name();
		modelName = Lottery.class.getSimpleName();
	}
	
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 需要摇出的人数
	 */
	private Integer number;

	/**
	 * 候补人数
	 */
	private Integer waitNum;
	
	/**
	 * 摇号次数
	 */
	private Integer times;
	
	/**
	 * 该摇号创建时间
	 */
	private Date createTime = new Date();
	
	/**
	 * 摇号说明
	 */
	private String remark;
	
	/**
	 * 摇号进行步奏
	 * 1 第一步
	 * 2 第二步
	 * 3 第三步 完成
	 */
	private Integer step = 0;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Integer getWaitNum() {
		return waitNum;
	}

	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}
	
}
