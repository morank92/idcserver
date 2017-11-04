package com.baoidc.idcserver.core;

/**
 * 订单状态枚举类
 * @author Administrator
 *
 */
public enum OrderStatus {
	
	UNPAY(0),PAIED(1);
	
	private int statusVal;
	
	OrderStatus(int statusVal){
		this.statusVal = statusVal;
	}

	public int getStatusVal() {
		return statusVal;
	}

	public void setStatusVal(int statusVal) {
		this.statusVal = statusVal;
	}

}
