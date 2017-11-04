package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

public class ServerRoom implements Serializable {

	private int id;
	private String name;
	private String area;
	private List<DeviceServer> deviceServerList;
	
	
	public List<DeviceServer> getDeviceServerList() {
		return deviceServerList;
	}
	public void setDeviceServerList(List<DeviceServer> deviceServerList) {
		this.deviceServerList = deviceServerList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	
	
}
