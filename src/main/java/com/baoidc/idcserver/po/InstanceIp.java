package com.baoidc.idcserver.po;

import java.io.Serializable;

public class InstanceIp implements Serializable {
	private int ipId;
	private int instanceId;//所属设备实例id
	private String ipStr;//ip
	private String ipBusi;//运营商
	private int chestId;
	private int ipStatus;
	
	
	
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public int getIpId() {
		return ipId;
	}
	public void setIpId(int ipId) {
		this.ipId = ipId;
	}
	
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public int getIpStatus() {
		return ipStatus;
	}
	public void setIpStatus(int ipStatus) {
		this.ipStatus = ipStatus;
	}
	public String getIpStr() {
		return ipStr;
	}
	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}
	public String getIpBusi() {
		return ipBusi;
	}
	public void setIpBusi(String ipBusi) {
		this.ipBusi = ipBusi;
	}
	
	
}
