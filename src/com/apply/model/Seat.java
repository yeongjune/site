package com.apply.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 座位实体
 * 用于保存学生每次考试的作为安排信息
 * @author lfq
 * @2014-5-10
 */
@Entity
@Table(name="apply_seat")
public class Seat  implements Serializable {
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Seat.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Seat.class.getSimpleName();
	}
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 考试Id，来自apply_exam表的id
	 */
	private Integer examId;

	/**
	 * 考室
	 */
	private String roomNo;
	
	/**
	 * 候考室
	 */
	private String watingRoomNo;
	
	/**
	 * 座位号
	 */
	private Integer seatNo;
	
	/**
	 * 考生ID，来自表apply_student的id
	 */
	private Integer studentId;
	
	/**
	 * 站点ID
	 */
	private Integer siteId;

	/**
	 * 考试类型
	 * 0	小学入学考试
	 * 1	初中入学考试
	 */
	private Integer examType;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getWatingRoomNo() {
		return watingRoomNo;
	}

	public void setWatingRoomNo(String watingRoomNo) {
		this.watingRoomNo = watingRoomNo;
	}

	public Integer getExamType() {
		return examType;
	}

	public void setExamType(Integer examType) {
		this.examType = examType;
	}

	public Integer getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(Integer seatNo) {
		this.seatNo = seatNo;
	}
	
}
