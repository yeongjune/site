package com.apply.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 学生
 * @author cyf
 *
 */
@Entity
@Table(name="apply_student")
public class Student implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = Student.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = Student.class.getSimpleName();
	}

	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 站点id
	 */
	private Integer siteId;
	/**
	 * 帐号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 准考证
	 */
	private String testCard;
	/**
	 * 证件类型
	 */
	private String certificateType;
	/**
	 * 证件号码
	 */
	private String certificate;
	/**
	 * 小学学籍号
	 */
	private String enrollmentNumbers;
	/**
	 * 民族
	 */
	private String nationality;
	/**
	 * 籍贯
	 */
	private String nativePlace;
	
	/**
	 * 户籍所在省
	 */
	private String domiciProvince;
	
	/**
	 * 户籍所在地-市
	 */
	private String domicilCity;
	/**
	 * 户籍所在地-区
	 */
	private String domicileArea;
	/**
	 * 户籍所在地-派出所地址
	 */
	private String domicile;
	/**
	 * 家庭住址
	 */
	private String homeAddress;
	
	/**
	 * 毕业学校-省
	 */
	private String graduateProvince;
	/**
	 * 毕业学校-市
	 */
	private String graduateCity;
	/**
	 * 毕业学校-区
	 */
	private String graduateArea;
	/**
	 * 毕业学校
	 */
	private String graduate;
	/**
	 * 出生日期
	 */
	private Date birthday;
	/**
	 * 联系手机
	 */
	private String phoneNumber;
	/**
	 * 获奖及个人特长
	 */
	@Column(columnDefinition = "text")
	private String rewardHobby;
	/**
	 * 语文
	 */
	private Double chinese;
	/**
	 * 数学
	 */
	private Double maths;
	/**
	 * 英文
	 */
	private Double english;
	/**
	 * 总分
	 */
	private Double total;
	/**
	 * 联系人1
	 */
	private String fullName1;
	/**
	 * 关系：父亲、母亲、其他监护人
	 */
	private String relationship1;
	/**
	 * 所在单位
	 */
	private String unit1;
	/**
	 * 职务
	 */
	private String position1;
	/**
	 * 联系电话
	 */
	private String telephone1;
	/**
	 * 联系人2
	 */
	private String fullName2;
	private String relationship2;
	private String unit2;
	private String position2;
	private String telephone2;
	/**
	 * 面试日期时间，格式yyyy-MM-dd HH:mm-HH:mm
	 */
	private String interviewDate;
	
	/**
	 * 是否参加面试；0：否、1：是
	 */
	private Integer interview=0;
	/**
	 * 面试成绩
	 */
	private Double interviewScore;
	/**
	 * 是否录取：-1未公布,0否，1是
	 */
	private Integer admit=-1;
	/**
	 * 报名状态：0待审核;1审核通过；2审核不通过；3审核回退
	 */
	private Integer status=0;
	/**
	 * 创建时间
	 */
	private Date createTime = new Date();
	/**
	 * 修改时间
	 */
	private Date updateTime;
	
	/**
	 * 头像图片地址Id
	 */
	private String headPicUrl;
	
	/**
	 *  审核备注
	 */
	private String checkRemark;
	
	/**
	 * 参加考试的批次
	 */
	private String batch;

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
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getEnrollmentNumbers() {
		return enrollmentNumbers;
	}
	public void setEnrollmentNumbers(String enrollmentNumbers) {
		this.enrollmentNumbers = enrollmentNumbers;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getDomicileArea() {
		return domicileArea;
	}
	public void setDomicileArea(String domicileArea) {
		this.domicileArea = domicileArea;
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
	public String getGraduateCity() {
		return graduateCity;
	}
	public void setGraduateCity(String graduateCity) {
		this.graduateCity = graduateCity;
	}
	public String getGraduateArea() {
		return graduateArea;
	}
	public void setGraduateArea(String graduateArea) {
		this.graduateArea = graduateArea;
	}
	public String getGraduate() {
		return graduate;
	}
	public void setGraduate(String graduate) {
		this.graduate = graduate;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getRewardHobby() {
		return rewardHobby;
	}
	public void setRewardHobby(String rewardHobby) {
		this.rewardHobby = rewardHobby;
	}
	public Double getChinese() {
		return chinese;
	}
	public void setChinese(Double chinese) {
		this.chinese = chinese;
	}
	public Double getMaths() {
		return maths;
	}
	public void setMaths(Double maths) {
		this.maths = maths;
	}
	public Double getEnglish() {
		return english;
	}
	public void setEnglish(Double english) {
		this.english = english;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getFullName1() {
		return fullName1;
	}
	public void setFullName1(String fullName1) {
		this.fullName1 = fullName1;
	}
	public String getRelationship1() {
		return relationship1;
	}
	public void setRelationship1(String relationship1) {
		this.relationship1 = relationship1;
	}
	public String getUnit1() {
		return unit1;
	}
	public void setUnit1(String unit1) {
		this.unit1 = unit1;
	}
	public String getPosition1() {
		return position1;
	}
	public void setPosition1(String position1) {
		this.position1 = position1;
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
	public String getRelationship2() {
		return relationship2;
	}
	public void setRelationship2(String relationship2) {
		this.relationship2 = relationship2;
	}
	public String getUnit2() {
		return unit2;
	}
	public void setUnit2(String unit2) {
		this.unit2 = unit2;
	}
	public String getPosition2() {
		return position2;
	}
	public void setPosition2(String position2) {
		this.position2 = position2;
	}
	public String getTelephone2() {
		return telephone2;
	}
	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}
	public String getInterviewDate() {
		return interviewDate;
	}
	public void setInterviewDate(String interviewDate) {
		this.interviewDate = interviewDate;
	}
	public Integer getInterview() {
		return interview;
	}
	public void setInterview(Integer interview) {
		this.interview = interview;
	}
	public Double getInterviewScore() {
		return interviewScore;
	}
	public void setInterviewScore(Double interviewScore) {
		this.interviewScore = interviewScore;
	}
	public Integer getAdmit() {
		return admit;
	}
	public void setAdmit(Integer admit) {
		this.admit = admit;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDomicilCity() {
		return domicilCity;
	}
	public void setDomicilCity(String domicilCity) {
		this.domicilCity = domicilCity;
	}
	public String getHeadPicUrl() {
		return headPicUrl;
	}
	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}
	public String getDomiciProvince() {
		return domiciProvince;
	}
	public void setDomiciProvince(String domiciProvince) {
		this.domiciProvince = domiciProvince;
	}
	public String getGraduateProvince() {
		return graduateProvince;
	}
	public void setGraduateProvince(String graduateProvince) {
		this.graduateProvince = graduateProvince;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTestCard() {
		return testCard;
	}
	public void setTestCard(String testCard) {
		this.testCard = testCard;
	}
	public String getCheckRemark() {
		return checkRemark;
	}
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	
}
