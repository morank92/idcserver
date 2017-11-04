package com.baoidc.idcserver.po;

public class ChestIpRange {
	private int id;
	private String startIp;
	private String endIp;
	private int chestId;
	private String ipBusi;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStartIp() {
		return startIp;
	}
	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}
	public String getEndIp() {
		return endIp;
	}
	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public String getIpBusi() {
		return ipBusi;
	}
	public void setIpBusi(String ipBusi) {
		this.ipBusi = ipBusi;
	}
	
	
}
