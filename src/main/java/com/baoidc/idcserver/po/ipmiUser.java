package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

public class ipmiUser implements Serializable {
	private int userId;
	private String userName;
	private String ipStr;
	private String password;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpStr() {
		return ipStr;
	}
	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
