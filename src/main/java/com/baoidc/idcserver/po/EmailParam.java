package com.baoidc.idcserver.po;

import java.io.Serializable;

/*
 * 这个类是管理员  配置邮件告警  配置邮箱的参数
 */
public class EmailParam implements Serializable{
	
    private int id;
    private int isSsl;
    private String port;
    private String smtp;
    private String emailAccount;
    private String emailPassword;
    private String sendName;
    private String receiveEmail;
    
    
	public int getIsSsl() {
		return isSsl;
	}
	public void setIsSsl(int isSsl) {
		this.isSsl = isSsl;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSmtp() {
		return smtp;
	}
	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmailAccount() {
		return emailAccount;
	}
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}
	public String getEmailPassword() {
		return emailPassword;
	}
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getReceiveEmail() {
		return receiveEmail;
	}
	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
    
    
    
}
