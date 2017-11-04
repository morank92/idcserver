package com.baoidc.idcserver.po;

import java.io.Serializable;

public class Subnetmask implements Serializable {
	private int chestId;
	private String subnetmaskValue;
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public String getSubnetmaskValue() {
		return subnetmaskValue;
	}
	public void setSubnetmaskValue(String subnetmaskValue) {
		this.subnetmaskValue = subnetmaskValue;
	}
	
	
}
