package com.apply.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 长兴中学借读生报名表
 * @author lfq
 * @time 2015-3-24
 *
 */
@Entity
@Table(name="cx_student")
public class CxStudent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = CxStudent.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = CxStudent.class.getSimpleName();
	}

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 站点id
	 */
	private Integer siteId;
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 出生日期
	 */
	private Date birthday;
	
	/**
	 * 毕业小学
	 */
	private String graduation;
	
	/**
	 * 曾任职位
	 */
	private String position;
	
	/**
	 * 户口所在地
	 */
	private String domicile;
	
	/**
	 * 家庭住址
	 */
	private String homeAddress;
	
	/**
	 * 父亲-姓名
	 */
	private String fullName1;

	/**
	 * 父亲-工作单位
	 */
	private String unit1;

	/**
	 * 父亲-联系方式
	 */
	private String telephone1;
	/**
	 * 母亲-姓名
	 */
	private String fullName2;

	/**
	 * 母亲-工作单位
	 */
	private String unit2;
	/**
	 * 母亲-联系方式
	 */
	private String telephone2;
	
	/**
	 * 有何特长
	 */
	@Column(length=1000)
	private String hobby;
	
	/**
	 * 区以上获奖情况
	 */
	@Column(length=1000)
	private String rewardInArea;
	
	/**
	 * 校内获奖情况
	 */
	@Column(length=1000)
	private String rewardInSchool;
	
	/**
	 * 五年级第一学期期末成绩-语文
	 */
	private Double yuwen1;
	/**
	 * 五年级第一学期期末成绩-数学
	 */
	private Double shuxue1;
	/**
	 * 五年级第一学期期末成绩-英语
	 */
	private Double yingyu1;
	/**
	 * 五年级第一学期期末成绩-年级排名
	 */
	private Integer paiming1;
	
	/**
	 * 五年级第二学期期末成绩-语文
	 */
	private Double yuwen2;
	/**
	 * 五年级第二学期期末成绩-数学
	 */
	private Double shuxue2;
	/**
	 * 五年级第二学期期末成绩-英语
	 */
	private Double yingyu2;
	/**
	 * 五年级第二学期期末成绩-年级排名
	 */
	private Integer paiming2;
	
	/**
	 * 六年级第一学期期末成绩-语文
	 */
	private Double yuwen3;
	/**
	 * 六年级第一学期期末成绩-数学
	 */
	private Double shuxue3;
	/**
	 * 六年级第一学期期末成绩-英语
	 */
	private Double yingyu3;
	/**
	 * 六年级第一学期期末成绩-年级排名
	 */
	private Integer paiming3;
	
	/**
	 * 六年级第二学期期末成绩-语文
	 */
	private Double yuwen4;
	/**
	 * 六年级第二学期期末成绩-数学
	 */
	private Double shuxue4;
	/**
	 * 六年级第二学期期末成绩-英语
	 */
	private Double yingyu4;
	/**
	 * 六年级第二学期期末成绩-年级排名
	 */
	private Integer paiming4;
	
	/**
	 * 头像图片地址
	 */
	private String headPicUrl;

	/**
	 * 创建时间
	 */
	private Date createTime = new Date();
	
	/**
	 * 是否已删除：0未删除，1已删除
	 * (用作标识是否已删除)
	 */
	private Integer isDelete=0;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGraduation() {
		return graduation;
	}

	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getFullName1() {
		return fullName1;
	}

	public void setFullName1(String fullName1) {
		this.fullName1 = fullName1;
	}

	public String getUnit1() {
		return unit1;
	}

	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getFullName2() {
		return fullName2;
	}

	public void setFullName2(String fullName2) {
		this.fullName2 = fullName2;
	}

	public String getUnit2() {
		return unit2;
	}

	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getRewardInArea() {
		return rewardInArea;
	}

	public void setRewardInArea(String rewardInArea) {
		this.rewardInArea = rewardInArea;
	}

	public String getRewardInSchool() {
		return rewardInSchool;
	}

	public void setRewardInSchool(String rewardInSchool) {
		this.rewardInSchool = rewardInSchool;
	}

	public Double getYuwen1() {
		return yuwen1;
	}

	public void setYuwen1(Double yuwen1) {
		this.yuwen1 = yuwen1;
	}

	public Double getShuxue1() {
		return shuxue1;
	}

	public void setShuxue1(Double shuxue1) {
		this.shuxue1 = shuxue1;
	}

	public Double getYingyu1() {
		return yingyu1;
	}

	public void setYingyu1(Double yingyu1) {
		this.yingyu1 = yingyu1;
	}

	public Integer getPaiming1() {
		return paiming1;
	}

	public void setPaiming1(Integer paiming1) {
		this.paiming1 = paiming1;
	}

	public Double getYuwen2() {
		return yuwen2;
	}

	public void setYuwen2(Double yuwen2) {
		this.yuwen2 = yuwen2;
	}

	public Double getShuxue2() {
		return shuxue2;
	}

	public void setShuxue2(Double shuxue2) {
		this.shuxue2 = shuxue2;
	}

	public Double getYingyu2() {
		return yingyu2;
	}

	public void setYingyu2(Double yingyu2) {
		this.yingyu2 = yingyu2;
	}

	public Integer getPaiming2() {
		return paiming2;
	}

	public void setPaiming2(Integer paiming2) {
		this.paiming2 = paiming2;
	}

	public Double getYuwen3() {
		return yuwen3;
	}

	public void setYuwen3(Double yuwen3) {
		this.yuwen3 = yuwen3;
	}

	public Double getShuxue3() {
		return shuxue3;
	}

	public void setShuxue3(Double shuxue3) {
		this.shuxue3 = shuxue3;
	}

	public Double getYingyu3() {
		return yingyu3;
	}

	public void setYingyu3(Double yingyu3) {
		this.yingyu3 = yingyu3;
	}

	public Integer getPaiming3() {
		return paiming3;
	}

	public void setPaiming3(Integer paiming3) {
		this.paiming3 = paiming3;
	}

	public Double getYuwen4() {
		return yuwen4;
	}

	public void setYuwen4(Double yuwen4) {
		this.yuwen4 = yuwen4;
	}

	public Double getShuxue4() {
		return shuxue4;
	}

	public void setShuxue4(Double shuxue4) {
		this.shuxue4 = shuxue4;
	}

	public Double getYingyu4() {
		return yingyu4;
	}

	public void setYingyu4(Double yingyu4) {
		this.yingyu4 = yingyu4;
	}

	public Integer getPaiming4() {
		return paiming4;
	}

	public void setPaiming4(Integer paiming4) {
		this.paiming4 = paiming4;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
