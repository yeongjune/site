package com.apply.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 民航子弟初中新生入学报名表
 * @author lfq
 * @time 2015-3-16
 *
 */
@Entity
@Table(name="dz_student")
public class DzStudent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = DzStudent.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = DzStudent.class.getSimpleName();
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
	 * 身份证号
	 */
	private String IDCard;
	
	/**
	 * 学籍号
	 */
	private String inSchoolNo;
	
	public String getInSchoolNo() {
		return inSchoolNo;
	}

	public void setInSchoolNo(String inSchoolNo) {
		this.inSchoolNo = inSchoolNo;
	}

	/**
	 * 所属籍贯省
	 */
	private String nativePlaceProvince;
	
	/**
	 * 所属籍贯市
	 */
	private String nativePlaceCity;
	
	/**
	 * 户籍所在省
	 */
	private String domiciProvince;
	
	/**
	 * 户籍所在-市
	 */
	private String domicilCity;
	/**
	 * 户籍所在-区/县
	 */
	private String domicileArea;
	/**
	 * 是否农业户口:0否，1是
	 */
	private Integer IsPeasant;
	/**
	 * 家庭住址
	 */
	private String homeAddress;
	
	/**
	 * 家庭电话
	 */
	private String homePhone;
	
	/**
	 * 毕业学校
	 */
	private String graduate;
	
	/**
	 * 曾任何种职位
	 */
	private String position;
	/**
	 * 出生日期
	 */
	private Date birthday;
	/**
	 * 父亲联系手机
	 */
	private String fatherPhone;
	/**
	 * 母亲联系手机
	 */
	private String matherPhone;
	/**
	 * 获奖及个人特长
	 */
	@Column(length=2000)
	private String rewardHobby;
	
	/**
	 * 家庭主要成员1-姓名
	 */
	private String fullName1;
	/**
	 * 家庭主要成员1-关系
	 */
	private String relationship1;
	/**
	 * 家庭主要成员1-工作单位
	 */
	private String unit1;

	/**
	 * 家庭主要成员1-联系电话
	 */
	private String telephone1;
	/**
	 * 家庭主要成员2-姓名
	 */
	private String fullName2;
	/**
	 * 家庭主要成员2-关系
	 */
	private String relationship2;
	/**
	 * 家庭主要成员2-工作单位
	 */
	private String unit2;
	/**
	 * 家庭主要成员2-联系电话
	 */
	private String telephone2;
	/**
	 * 头像图片地址Id
	 */
	private String headPicUrl;
	
	
	/**
	 * 公司名
	 */
	private String companyName;

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

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getNativePlaceProvince() {
		return nativePlaceProvince;
	}

	public void setNativePlaceProvince(String nativePlaceProvince) {
		this.nativePlaceProvince = nativePlaceProvince;
	}

	public String getNativePlaceCity() {
		return nativePlaceCity;
	}

	public void setNativePlaceCity(String nativePlaceCity) {
		this.nativePlaceCity = nativePlaceCity;
	}

	public String getDomiciProvince() {
		return domiciProvince;
	}

	public void setDomiciProvince(String domiciProvince) {
		this.domiciProvince = domiciProvince;
	}

	public String getDomicilCity() {
		return domicilCity;
	}

	public void setDomicilCity(String domicilCity) {
		this.domicilCity = domicilCity;
	}

	public String getDomicileArea() {
		return domicileArea;
	}

	public void setDomicileArea(String domicileArea) {
		this.domicileArea = domicileArea;
	}

	public Integer getIsPeasant() {
		return IsPeasant;
	}

	public void setIsPeasant(Integer isPeasant) {
		IsPeasant = isPeasant;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getGraduate() {
		return graduate;
	}

	public void setGraduate(String graduate) {
		this.graduate = graduate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getFatherPhone() {
		return fatherPhone;
	}

	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}

	public String getMatherPhone() {
		return matherPhone;
	}

	public void setMatherPhone(String matherPhone) {
		this.matherPhone = matherPhone;
	}

	public String getRewardHobby() {
		return rewardHobby;
	}

	public void setRewardHobby(String rewardHobby) {
		this.rewardHobby = rewardHobby;
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

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	
}
