package com.baoidc.idcserver.vo.query;

import java.io.Serializable;

public class RenewVO implements Serializable {
	
	private int userId;//用户
	private int orderType;//订单类型
	private String durationEncrypt;//时长的key
	private String[] instanceEncrypt;//实例的key
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getDurationEncrypt() {
		return durationEncrypt;
	}
	public void setDurationEncrypt(String durationEncrypt) {
		this.durationEncrypt = durationEncrypt;
	}
	public String[] getInstanceEncrypt() {
		return instanceEncrypt;
	}
	public void setInstanceEncrypt(String[] instanceEncrypt) {
		this.instanceEncrypt = instanceEncrypt;
	}
}
