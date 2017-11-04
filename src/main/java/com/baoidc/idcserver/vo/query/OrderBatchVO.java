package com.baoidc.idcserver.vo.query;

import java.util.List;

import com.baoidc.idcserver.po.Order;

public class OrderBatchVO {
	
	private List<Order> orderList;//为 批量支付

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	
	
}
