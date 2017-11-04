package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChargeRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2065426673631597330L;
	
	private int recordId;
	private int userId;
	private int accountId;
	private String chargeNo;
	private String chargeCreateTime;
	private Double chargeAmt;
	private int chargeType;
	private int chargeStatus;
	private String chargeFinishTime;
	
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
	public String getChargeNo() {
		return chargeNo;
	}
	public void setChargeNo(String chargeNo) {
		this.chargeNo = chargeNo;
	}
	public Double getChargeAmt() {
		return chargeAmt;
	}
	public void setChargeAmt(Double chargeAmt) {
		this.chargeAmt = chargeAmt;
	}
	public int getChargeType() {
		return chargeType;
	}
	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(int chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public String getChargeCreateTime() {
		return chargeCreateTime;
	}
	public void setChargeCreateTime(String chargeCreateTime) {
		this.chargeCreateTime = chargeCreateTime;
	}
	public String getChargeFinishTime() {
		return chargeFinishTime;
	}
	public void setChargeFinishTime(String chargeFinishTime) {
		this.chargeFinishTime = chargeFinishTime;
	}

}
