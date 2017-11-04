package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
/*
 * 用户联系人
 */
@XmlRootElement
public class UserContacts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4261589919353965392L;
	
	
	private int id;
	private int userId;
	private String realName;
	private String fixedlinkPhone;
	private String mobilePhone;
	private String contactEmail;
	private String qqNum;
	
	
	public String getQqNum() {
		return qqNum;
	}
	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getFixedlinkPhone() {
		return fixedlinkPhone;
	}
	public void setFixedlinkPhone(String fixedlinkPhone) {
		this.fixedlinkPhone = fixedlinkPhone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

}
