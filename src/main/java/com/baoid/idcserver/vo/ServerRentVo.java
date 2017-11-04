package com.baoid.idcserver.vo;

import java.util.List;

import com.baoidc.idcserver.po.DeviceServer;
import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.ServerRoom;

public class ServerRentVo {
	private List<DeviceServer> deviceServerList;
	private List<DeviceSourceParam> sourceParamList;
	private List<DeviceSourceParam> ramList;
	private List<DeviceSourceParam> diskList;
	private List<ServerRoom> serverRoomList;
	
	
	public List<DeviceSourceParam> getSourceParamList() {
		return sourceParamList;
	}
	public void setSourceParamList(List<DeviceSourceParam> sourceParamList) {
		this.sourceParamList = sourceParamList;
	}
	public List<DeviceSourceParam> getRamList() {
		return ramList;
	}
	public void setRamList(List<DeviceSourceParam> ramList) {
		this.ramList = ramList;
	}
	public List<DeviceSourceParam> getDiskList() {
		return diskList;
	}
	public void setDiskList(List<DeviceSourceParam> diskList) {
		this.diskList = diskList;
	}
	public List<DeviceServer> getDeviceServerList() {
		return deviceServerList;
	}
	public void setDeviceServerList(List<DeviceServer> deviceServerList) {
		this.deviceServerList = deviceServerList;
	}
	public List<ServerRoom> getServerRoomList() {
		return serverRoomList;
	}
	public void setServerRoomList(List<ServerRoom> serverRoomList) {
		this.serverRoomList = serverRoomList;
	}
	
	
	
}
