package com.baoid.idcserver.vo;

import java.io.Serializable;

/*
 * 消费分析辅助类
 */
public class ConsumeTypeAnalyze implements Serializable{
	private String months;
	private double consumeTotal;
	private int instanceType;
	
	
	public int getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}
	public String getMonths() {
		return months;
	}
	public void setMonths(String months) {
		this.months = months;
	}
	public double getConsumeTotal() {
		return consumeTotal;
	}
	public void setConsumeTotal(double consumeTotal) {
		this.consumeTotal = consumeTotal;
	}
	
	
}
