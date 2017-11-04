package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4794079182142650565L;
	
	private int accountId;
	private int userId;
	private String createTime;
	private Double chargeTotal;//充值总额
	private Double consumeTotal;//消费总额
	private Double accountBalance;//余额
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Double getChargeTotal() {
		return chargeTotal;
	}
	public void setChargeTotal(Double chargeTotal) {
		this.chargeTotal = chargeTotal;
	}
	public Double getConsumeTotal() {
		return consumeTotal;
	}
	public void setConsumeTotal(Double consumeTotal) {
		this.consumeTotal = consumeTotal;
	}
	public Double getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}
}
