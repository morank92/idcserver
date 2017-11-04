package com.baoidc.idcserver.po;

import java.io.Serializable;

public class Touclick implements Serializable{
	private String token;
	private String checkAddress;
	private String sid;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCheckAddress() {
		return checkAddress;
	}
	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	
}
