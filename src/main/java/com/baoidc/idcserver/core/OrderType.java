package com.baoidc.idcserver.core;

/**
 * 订单类型枚举类
 * @author Administrator
 *
 */
public enum OrderType {
	
	SERVER_RENT(1),SERVER_TRUST(2),RACK_RENT(3),SERVER_UP(4);
	
	private int orderType;
	
	OrderType(int orderType){
		this.orderType = orderType;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}


}
