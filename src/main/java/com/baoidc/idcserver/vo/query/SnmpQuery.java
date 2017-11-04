package com.baoidc.idcserver.vo.query;

import java.util.List;

public class SnmpQuery {
	private int userId;
	private int version;
	private String userName;
	private String passWord;
	private String key;
	private String ipStr;
	private String comStr;
	private String encrypt;//此配置信息放入redis中的标识
	
	
	public String getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getComStr() {
		return comStr;
	}
	public void setComStr(String comStr) {
		this.comStr = comStr;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getIpStr() {
		return ipStr;
	}
	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
}
