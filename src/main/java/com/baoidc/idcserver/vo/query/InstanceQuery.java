package com.baoidc.idcserver.vo.query;

public class InstanceQuery {
	
	private int userId;//用户id
	private int serviceId;//服务类型id
	private int status;//实例状态
	private String deviceName; //设备类型名称
	private String ipStr;//ip地址
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getIpStr() {
		return ipStr;
	}
	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}
	
	

}
