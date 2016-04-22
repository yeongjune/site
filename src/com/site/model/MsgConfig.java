package com.site.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="site_msg_config")
public class MsgConfig {
	
	public static final String tableName;
    public static final String modelName;
    static{
        Class c = MsgConfig.class;
        tableName = ((Table)c.getAnnotation(Table.class)).name();
        modelName = c.getSimpleName();
    }
	
	@Id
	@GeneratedValue
	private Integer id;
	/**
	 * 购买短信的条数
	 */
	private Integer msgCount;
	
	/**
	 * 最后修改时间
	 */
	private Date modifyDate;
	/**
	 * 站点id
	 */
	private Integer siteId;
	
	/**
	 * 短信默认发送信息的内容。如： 【番禺流管办】 您有一条网上信访需要处理。
	 */
	private String msgContent;
	
	/**
	 * 默认接收信息的手机号码，多个用逗号隔开(13725126273,13725126274) 
	 */
	private String phone;

	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
		
	}

	public Integer getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(Integer msgCount) {
		this.msgCount = msgCount;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
}
