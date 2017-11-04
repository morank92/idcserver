package com.baoidc.idcserver.po;

import java.io.Serializable;

/*
 * 机柜上的网关
 */
public class Gateway implements Serializable {
	private int chestId;
	private String gatewayValue;
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public String getGatewayValue() {
		return gatewayValue;
	}
	public void setGatewayValue(String gatewayValue) {
		this.gatewayValue = gatewayValue;
	}
	
	 
}
