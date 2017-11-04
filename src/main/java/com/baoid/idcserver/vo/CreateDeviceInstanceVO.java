package com.baoid.idcserver.vo;

import java.util.List;

import com.baoidc.idcserver.po.ServerNum;

public class CreateDeviceInstanceVO {
	
	private String orderId;
	private List<ServerNum> numbers;//实例编号
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public List<ServerNum> getNumbers() {
		return numbers;
	}
	public void setNumbers(List<ServerNum> numbers) {
		this.numbers = numbers;
	}

}
