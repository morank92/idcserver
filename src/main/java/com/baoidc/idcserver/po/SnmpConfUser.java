package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

public class SnmpConfUser implements Serializable {
	private int id;
	private int userId;
	private int instanceId;
	private int version;
	private String secName;
	private String passWord;
	private String authPro;//加密的算法
	private String authKey;//加密的参数，秘钥
	private String secModel;//加密的模式
	private String snmpIp;
	private String comWord;
	private String encrypt;//此配置信息放入redis中的标识
	private String systemVersion;//系统版本
	
	
	public String getSecModel() {
		return secModel;
	}
	public void setSecModel(String secModel) {
		this.secModel = secModel;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public String getSecName() {
		return secName;
	}
	public void setSecName(String secName) {
		this.secName = secName;
	}
	public String getAuthPro() {
		return authPro;
	}
	public void setAuthPro(String authPro) {
		this.authPro = authPro;
	}
	public String getSnmpIp() {
		return snmpIp;
	}
	public void setSnmpIp(String snmpIp) {
		this.snmpIp = snmpIp;
	}
	public String getComWord() {
		return comWord;
	}
	public void setComWord(String comWord) {
		this.comWord = comWord;
	}
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
}
