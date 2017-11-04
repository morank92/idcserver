package com.baoidc.idcserver.po;

import java.io.Serializable;

public class EncashmentRecord implements Serializable {
	
	private int id;
	private int userId;
	private User user;
	private String encashmentNo;
	private String encashmentCreateTime;
	private double encashmentAmt;
	private String encashmentAccount;
	private String encashmentStatus;
	private String encashmentFinishTime;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getEncashmentNo() {
		return encashmentNo;
	}
	public void setEncashmentNo(String encashmentNo) {
		this.encashmentNo = encashmentNo;
	}
	public String getEncashmentCreateTime() {
		return encashmentCreateTime;
	}
	public void setEncashmentCreateTime(String encashmentCreateTime) {
		this.encashmentCreateTime = encashmentCreateTime;
	}
	public double getEncashmentAmt() {
		return encashmentAmt;
	}
	public void setEncashmentAmt(double encashmentAmt) {
		this.encashmentAmt = encashmentAmt;
	}
	public String getEncashmentAccount() {
		return encashmentAccount;
	}
	public void setEncashmentAccount(String encashmentAccount) {
		this.encashmentAccount = encashmentAccount;
	}
	
	
	public String getEncashmentStatus() {
		return encashmentStatus;
	}
	public void setEncashmentStatus(String encashmentStatus) {
		this.encashmentStatus = encashmentStatus;
	}
	public String getEncashmentFinishTime() {
		return encashmentFinishTime;
	}
	public void setEncashmentFinishTime(String encashmentFinishTime) {
		this.encashmentFinishTime = encashmentFinishTime;
	}
	
	
}
