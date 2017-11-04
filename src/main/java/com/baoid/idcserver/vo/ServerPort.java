package com.baoid.idcserver.vo;

import java.io.Serializable;

public class ServerPort implements Serializable{
	private int serverId;
	private int chestId;
	private String portNum;
	
	
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public String getPortNum() {
		return portNum;
	}
	public void setPortNum(String portNum) {
		this.portNum = portNum;
	}
	
	
	
}
