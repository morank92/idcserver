package com.baoid.idcserver.vo;

import java.io.Serializable;
/*
 * 财务分析辅助类
 */
public class FinanceAnalyze implements Serializable {
	private String months;
	private double chargeAmt;
	private double encashmentAmt;
	private double estimateIncome;
	
	
	
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	public double getChargeAmt() {
		return chargeAmt;
	}
	public void setChargeAmt(double chargeAmt) {
		this.chargeAmt = chargeAmt;
	}
	public double getEncashmentAmt() {
		return encashmentAmt;
	}
	public void setEncashmentAmt(double encashmentAmt) {
		this.encashmentAmt = encashmentAmt;
	}
	public double getEstimateIncome() {
		return this.chargeAmt-this.encashmentAmt;
	}
	public void setEstimateIncome(double estimateIncome) {
		this.estimateIncome = estimateIncome;
	}
	
	
}
