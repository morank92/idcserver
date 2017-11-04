package com.baoidc.idcserver.po;

import java.io.Serializable;
/*
 * 用户配置  告警联系人   目前  type两种  note短信   email邮箱
 */
public class ReportConf implements Serializable{
	private int userId;
	private int id;
	private String username;
	private String type;
	private String value;
	private int userType;
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
