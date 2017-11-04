package com.baoidc.idcserver.po;

import java.io.Serializable;

public class Remark implements Serializable {

	private int id;
	private String orderId;
	private String content;//内容，备注信息
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
}
