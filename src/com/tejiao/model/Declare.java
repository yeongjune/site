package com.tejiao.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 申报表
 * Created by dzf on 15-12-28.
 */
@Entity
@Table(name="tejiao_declare")
public class Declare implements Serializable{

    public static final String tableName;
    public static final String modelName;

    static {
        Class<?> clazz = Declare.class;
        tableName = clazz.getAnnotation(Table.class).name();
        modelName = clazz.getSimpleName();
    }

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 网站id
     */
    private Integer siteId;

    /**
     * 家长用户id
     */
    private Integer parentId;

    /**
     * 申报标题
     */
    private String title;

    /**
     * 学籍号
     */
    private String registry;

    /**
     * 申报学校id
     */
    private Integer schoolId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生性别
     * 1 男
     */
    @Column(columnDefinition = "int default 0")
    private int gender = 0;

    /**
     * 出生日期
     */
    private Date birth;

    /**
     * 身份证号码
     */
    private String idcard;

    /**
     * 户籍地址
     */
    private String census;

    /**
     * 入学时间
     */
    private Date learnTime;

    /**
     * 当前时间的年级
     */
    @Column(columnDefinition = "int default 0")
    private int grade;

    /**
     * 年级（申报时候的年级）
     */
    @Column(columnDefinition = "int default 0")
    private int sourceGrade;

    /* 残疾证信息 **************/
    /**
     * 是否已领残疾证
     *
     * 0 ：未领残疾证
     * 1 :已领残疾证
     */
    @Column(columnDefinition = "int default 0")
    private int existence = 0;

    /* 未领取********/

    /**
     * 鉴定时间
     */
    private Date appraiseTime;

    /**
     * 鉴定结果
     *
     <option value="1">智力残疾-A</option>
     <option value="2">视力残疾-B</option>
     <option value="3">肢体残疾-C</option>
     <option value="4">听力言语残疾-D</option>
     <option value="5">综合（多重）残疾-E</option>
     <option value="6">其他残疾-F</option>
     <option value="7">脑瘫-G</option>
     <option value="8">自闭症-H</option>
     */
    @Column(columnDefinition = "int default 0")
    private int appraiseResult = 0;

    /**
     * 检测机构
     */
    private String discoverOrgan;

    /* 已领取***********/

    /**
     * 残疾类型
     *
     <option value="1">智力残疾-A</option>
     <option value="2">视力残疾-B</option>
     <option value="3">肢体残疾-C</option>
     <option value="4">听力言语残疾-D</option>
     <option value="5">综合（多重）残疾-E</option>
     <option value="6">其他残疾-F</option>
     <option value="7">脑瘫-G</option>
     <option value="8">自闭症-H</option>
     */
    @Column(columnDefinition = "int default 0")
    private int deformityType = 0;

    /**
     * 残疾证号
     */
    private String deformityNumber;

    /**
     * 发证机关
     */
    private String proofOffice;

    /* 附件图片 **********************/

    /**
     * 申请表
     */
    private String imgTable;

    /**
     * 班主任情况说明
     */
    private String imgSituation;

    /**
     * 家长申请书
     */
    private String imgApplication;

    /**
     * 残疾证
     */
    private String imgCertificate;

    /**
     * 评估量表
     */
    private String imgAssessment;

    /**
     * 诊断书
     */
    private String imgDiagnosis;

    /* 监护人信息 ************/

    /**
     * 监护人姓名
     */
    private String guardian;

    /**
     * 与监护人关系
     */
    private String relation;

    /**
     * 固话
     */
    private String telephone;

    /**
     * 手机号码,联系电话
     */
    private String phone;

    /**
     * 电子邮件, (联系地址)
     */
    private String email;

    /* 审核信息 *******************/

    /**
     * 镇街审核情况
     * 0：未审核
     * 1：审核通过
     * 2：审核不通过
     */
    @Column(columnDefinition = "int default 0")
    private int townCheckState = 0;

    /**
     * 区审核情况
     * 0：未审核
     * 1：审核通过
     * 2：审核不通过
     */
    @Column(columnDefinition = "int default 0")
    private int districtCheckState = 0;

    /**
     * 审核通过后分配的code
     */
    private String code;

    /**
     * 创建时间
     */
    private Date createTime = new Date();
    
    /**
     * 最后修改时间
     */
    private Date lastModifyTime = new Date();

    /**
     * 是否申请 免计算考试分数
     * 0：未申请
     * 1：申请
     * 2：申请通过
     * 3：申请不通过
     */
    @Column(columnDefinition = "int default 0")
    private Integer freeCount = 0;

    /**
     * 申请时间
     */
    private Date freeCountTime;

    /**
     * 是否申请 职中升学面试
     * 0：未申请
     * 1：申请
     * 2：申请通过
     * 3：申请不通过
     */
    @Column(columnDefinition = "int default 0")
    private Integer applyAudition = 0;

    /**
     * 申请时间
     */
    private Date applyAuditionTime;

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getCensus() {
        return census;
    }

    public void setCensus(String census) {
        this.census = census;
    }

    public Date getLearnTime() {
        return learnTime;
    }

    public void setLearnTime(Date learnTime) {
        this.learnTime = learnTime;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getExistence() {
        return existence;
    }

    public void setExistence(int existence) {
        this.existence = existence;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTownCheckState() {
        return townCheckState;
    }

    public void setTownCheckState(int townCheckState) {
        this.townCheckState = townCheckState;
    }

    public int getDistrictCheckState() {
        return districtCheckState;
    }

    public void setDistrictCheckState(int districtCheckState) {
        this.districtCheckState = districtCheckState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAppraiseTime() {
        return appraiseTime;
    }

    public void setAppraiseTime(Date appraiseTime) {
        this.appraiseTime = appraiseTime;
    }

    public String getDiscoverOrgan() {
        return discoverOrgan;
    }

    public void setDiscoverOrgan(String discoverOrgan) {
        this.discoverOrgan = discoverOrgan;
    }

    public String getDeformityNumber() {
        return deformityNumber;
    }

    public void setDeformityNumber(String deformityNumber) {
        this.deformityNumber = deformityNumber;
    }

    public int getDeformityType() {
        return deformityType;
    }

    public void setDeformityType(int deformityType) {
        this.deformityType = deformityType;
    }

    public int getAppraiseResult() {
        return appraiseResult;
    }

    public void setAppraiseResult(int appraiseResult) {
        this.appraiseResult = appraiseResult;
    }

    public String getImgTable() {
        return imgTable;
    }

    public void setImgTable(String imgTable) {
        this.imgTable = imgTable;
    }

    public String getImgSituation() {
        return imgSituation;
    }

    public void setImgSituation(String imgSituation) {
        this.imgSituation = imgSituation;
    }

    public String getImgApplication() {
        return imgApplication;
    }

    public void setImgApplication(String imgApplication) {
        this.imgApplication = imgApplication;
    }

    public String getImgCertificate() {
        return imgCertificate;
    }

    public void setImgCertificate(String imgCertificate) {
        this.imgCertificate = imgCertificate;
    }

    public String getImgAssessment() {
        return imgAssessment;
    }

    public void setImgAssessment(String imgAssessment) {
        this.imgAssessment = imgAssessment;
    }

    public String getImgDiagnosis() {
        return imgDiagnosis;
    }

    public void setImgDiagnosis(String imgDiagnosis) {
        this.imgDiagnosis = imgDiagnosis;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProofOffice() {
        return proofOffice;
    }

    public void setProofOffice(String proofOffice) {
        this.proofOffice = proofOffice;
    }

	
	public int getSourceGrade() {
		return sourceGrade;
	}

	public void setSourceGrade(int sourceGrade) {
		this.sourceGrade = sourceGrade;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

    public Integer getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(Integer freeCount) {
        this.freeCount = freeCount;
    }

    public Date getFreeCountTime() {
        return freeCountTime;
    }

    public void setFreeCountTime(Date freeCountTime) {
        this.freeCountTime = freeCountTime;
    }

    public Integer getApplyAudition() {
        return applyAudition;
    }

    public void setApplyAudition(Integer applyAudition) {
        this.applyAudition = applyAudition;
    }

    public Date getApplyAuditionTime() {
        return applyAuditionTime;
    }

    public void setApplyAuditionTime(Date applyAuditionTime) {
        this.applyAuditionTime = applyAuditionTime;
    }
}
