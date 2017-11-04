package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 42353760250052278L;
	
	private int id;
	private String userName;
	private String email;
	private String password;
	private String phoneNum;
	private String registerTime;
	private int identityType;
	private String realName;
	private String companyAddr;
	private String officePhone;
	private String zipCode;
	private String faxNo;
	private String industryApp;
	private String primaryBusi;
	private String networkAddr;
	private int customerManagerId;
	private String qqNum;
	private String zfbao;
	private int updateFlag;
	private int userRole;
	private String authType;
	private String authPicture;
	private String authPictureFront;
	private String authPictureBack;
	private String authName;
	private String authNum;
	
	
	
	
	
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public String getAuthPicture() {
		return authPicture;
	}
	public void setAuthPicture(String authPicture) {
		this.authPicture = authPicture;
	}
	public String getAuthPictureFront() {
		return authPictureFront;
	}
	public void setAuthPictureFront(String authPictureFront) {
		this.authPictureFront = authPictureFront;
	}
	public String getAuthPictureBack() {
		return authPictureBack;
	}
	public void setAuthPictureBack(String authPictureBack) {
		this.authPictureBack = authPictureBack;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getAuthNum() {
		return authNum;
	}
	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}
	public int getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}
	public String getZfbao() {
		return zfbao;
	}
	public void setZfbao(String zfbao) {
		this.zfbao = zfbao;
	}
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	public int getIdentityType() {
		return identityType;
	}
	public void setIdentityType(int identityType) {
		this.identityType = identityType;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	public String getOfficePhone() {
		return officePhone;
	}
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public String getIndustryApp() {
		return industryApp;
	}
	public void setIndustryApp(String industryApp) {
		this.industryApp = industryApp;
	}
	public String getPrimaryBusi() {
		return primaryBusi;
	}
	public void setPrimaryBusi(String primaryBusi) {
		this.primaryBusi = primaryBusi;
	}
	public String getNetworkAddr() {
		return networkAddr;
	}
	public void setNetworkAddr(String networkAddr) {
		this.networkAddr = networkAddr;
	}
	public int getCustomerManagerId() {
		return customerManagerId;
	}
	public void setCustomerManagerId(int customerManagerId) {
		this.customerManagerId = customerManagerId;
	}
	public int getUserRole() {
		return userRole;
	}
	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

}
