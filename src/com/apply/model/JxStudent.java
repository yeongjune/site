package com.apply.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 集贤新生入学报名表
 * @author lfq
 * @time 2015-3-20
 *
 */
@Entity
@Table(name="jx_student")
public class JxStudent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String tableName;
	public static final String modelName;
	
	static{
		Table t = JxStudent.class.getAnnotation(Table.class);
		tableName = t.name();
		modelName = JxStudent.class.getSimpleName();
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
	 * 曾用名
	 */
	private String usedName;
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 出生日期
	 */
	private Date birthday;
	
	/**
	 * 身份证号
	 */
	private String IDCard;
	
	/**
	 * 民族
	 */
	private String nationality;
	
	/**
	 * 所属籍贯省
	 */
	private String nativePlaceProvince;
	
	/**
	 * 所属籍贯市
	 */
	private String nativePlaceCity;
	
	/**
	 * 健康状况
	 */
	private String healthyCondition;
	
	/**
	 * 个人特长
	 */
	@Column(length=1000)
	private String rewardHobby;
	
	/**
	 * 家庭住址
	 */
	private String homeAddress;
	
	/**
	 * 户口所在地
	 */
	private String domicile;

	
	
	/**
	 * 第一联系人-姓名
	 */
	private String fullName1;
	/**
	 * 第一联系人
	 */
	private String relationship1;
	/**
	 * 第一联系人-工作单位
	 */
	private String unit1;

	/**
	 * 第一联系人-单位电话
	 */
	private String telephone1;
	/**
	 * 第二联系人-姓名
	 */
	private String fullName2;
	/**
	 * 第二联系人
	 */
	private String relationship2;
	/**
	 * 第二联系人-工作单位
	 */
	private String unit2;
	/**
	 * 第二联系人-单位电话
	 */
	private String telephone2;
	
	/**
	 * 头像图片地址Id
	 */
	private String headPicUrl;
	
	
	/**
	 * 地段生：是 或 否
	 */
	private String dds;
	
	/**
	 * 广州户籍非地段生：是 或 否
	 */
	private String gzhjfdds;
	
	/**
	 * 业主生：是 或 否
	 */
	private String yzs;
	
	/**
	 * 非广州户籍学生：是 或 否
	 */
	private String fgzhjxs;
	
	/**
	 * 了解渠道
	 */
	private String ljqd;
	
	/**
	 * 体检表：是 或 否
	 */
	private String tjb;
	
	/**
	 * 预防接种证：是 或 否
	 */
	private String yfjzzh;
	
	/**
	 * 计生证：是 或 否
	 */
	private String jszh;

	/**
	 * 户口本：是 或 否
	 */
	private String hkb;
	
	/**
	 * 就业证明：是 或 否
	 */
	private String jyzm;
	
	/**
	 * 房产证或购房协议：是 或 否
	 */
	private String fczhhgfxy;
	
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

	public String getUsedName() {
		return usedName;
	}

	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
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

	public String getHealthyCondition() {
		return healthyCondition;
	}

	public void setHealthyCondition(String healthyCondition) {
		this.healthyCondition = healthyCondition;
	}

	public String getRewardHobby() {
		return rewardHobby;
	}

	public void setRewardHobby(String rewardHobby) {
		this.rewardHobby = rewardHobby;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
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

	public String getDds() {
		return dds;
	}

	public void setDds(String dds) {
		this.dds = dds;
	}

	public String getGzhjfdds() {
		return gzhjfdds;
	}

	public void setGzhjfdds(String gzhjfdds) {
		this.gzhjfdds = gzhjfdds;
	}

	public String getYzs() {
		return yzs;
	}

	public void setYzs(String yzs) {
		this.yzs = yzs;
	}

	

	public String getFgzhjxs() {
		return fgzhjxs;
	}

	public void setFgzhjxs(String fgzhjxs) {
		this.fgzhjxs = fgzhjxs;
	}

	public String getLjqd() {
		return ljqd;
	}

	public void setLjqd(String ljqd) {
		this.ljqd = ljqd;
	}

	public String getTjb() {
		return tjb;
	}

	public void setTjb(String tjb) {
		this.tjb = tjb;
	}

	public String getYfjzzh() {
		return yfjzzh;
	}

	public void setYfjzzh(String yfjzzh) {
		this.yfjzzh = yfjzzh;
	}

	public String getJszh() {
		return jszh;
	}

	public void setJszh(String jszh) {
		this.jszh = jszh;
	}

	public String getHkb() {
		return hkb;
	}

	public void setHkb(String hkb) {
		this.hkb = hkb;
	}

	public String getJyzm() {
		return jyzm;
	}

	public void setJyzm(String jyzm) {
		this.jyzm = jyzm;
	}

	public String getFczhhgfxy() {
		return fczhhgfxy;
	}

	public void setFczhhgfxy(String fczhhgfxy) {
		this.fczhhgfxy = fczhhgfxy;
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
