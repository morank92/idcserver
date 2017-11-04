package com.baoid.idcserver.vo;

import java.io.Serializable;

import com.baoidc.idcserver.po.User;

public class ConsumeAnalyze implements Serializable {
	private int recordId;
	private String consumeNo;//消费编号编号
	private String orderNo;//订单编号编号
	private double consumeAmt;//消费金额
	private String consumeTime;//消费时间
	private String productName;//消费描述
	private User user;
	
	
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getConsumeNo() {
		return consumeNo;
	}
	public void setConsumeNo(String consumeNo) {
		this.consumeNo = consumeNo;
	}
	public double getConsumeAmt() {
		return consumeAmt;
	}
	public void setConsumeAmt(double consumeAmt) {
		this.consumeAmt = consumeAmt;
	}
	public String getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
	
	
}
