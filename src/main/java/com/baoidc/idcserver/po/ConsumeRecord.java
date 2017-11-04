package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConsumeRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8601788163218854833L;
	
	private int recordId;
	private int userId;
	private int accountId;
	private String consumeNo;
	private String orderNo;
	private String orderType;
	private Double consumeAmt;
	private String consumeTime;
	private String productName;
	private int instanceType;
	
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getConsumeNo() {
		return consumeNo;
	}
	public void setConsumeNo(String consumeNo) {
		this.consumeNo = consumeNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Double getConsumeAmt() {
		return consumeAmt;
	}
	public void setConsumeAmt(Double consumeAmt) {
		this.consumeAmt = consumeAmt;
	}
	public String getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}
}
