package com.lottery.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 学生
 * @author ah
 *
 */
@Entity(name="LotteryStudent")
@Table(name="lottery_student")
public class Student implements Serializable{

	private static final long serialVersionUID = 7710554350850907392L;
	public static final String tableName;
	public static final String modelName;
	
	/**
	 * 选中状态
	 */
	public static final Integer SELECTED = 1;
	/**
	 * 候补状态
	 */
	public static final Integer WAITING = 2;
	/**
	 * 未选中状态
	 */
	public static final Integer UNSELECT = 0;
	/**
	 * 性别：男
	 */
	public static final String MALE = "男";
	/**
	 * 性别：女
	 */
	public static final String FEMALE = "女";
	
	static{
		Table table = Student.class.getAnnotation(Table.class);
		tableName = table.name();
		modelName = Student.class.getSimpleName();
	}
	
	@Id
	@GeneratedValue
	private Integer id;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 出生年月日
	 */
	private Date birthday;
	
	/**
	 * 身份证号
	 */
	private String IDCard;

	/**
	 * 摇号状态
	 * 0	未被摇中
	 * 1	已被摇中
	 */
	private Integer status = 0;

	/**
	 * 摇号id  所属那次摇号
	 */
	private Integer lotteryId;
	
	/**
	 * 毕业学校
	 */
	private String school;
	
	/**
	 * 联系电话(父亲)
	 */
	private String phone;

	/**
	 * 联系电话(母亲)
	 */
	private String mPhone;
	
	/**
	 * 全国学籍号
	 */
	private String stuCode;
	
	/**
	 * 小升初编号
	 */
	private String stuNo;
	
	/**
	 * 随机号码
	 */
	private Integer randomNum;

	/**
	 * 序号
	 */
	private Integer serialNum;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(Integer lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public String getStuNo() {
		return stuNo;
	}

	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}

	public Integer getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(Integer randomNum) {
		this.randomNum = randomNum;
	}

	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

}
