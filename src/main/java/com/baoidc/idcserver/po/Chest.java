package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;
/*
 * 机柜
 */
public class Chest implements Serializable {
	private int chestId;
	private String chestNumber;//编码
	private String chestType;//规格
	private String createTime;
	private String hostRoom;//机房
	private int hostRoomId;//机房配置参数id
	private String upPort;//上联端口
	private String interchanger;//交换机
	private String manageIp;// 管理IP
	private String ddos;
	private String bandWidth;
	private String power;
	private String remark;
	private int useStatus;
	private User user;
	private List<InstanceIp> ipList;//ip集合
	private DeviceInstance deviceInstance;//对应的实例
	private List<ChestIpRange > chestIpRangeList;//ip范围
	private List<Gateway> gatewayList;//网关
	private List<Subnetmask> subnetmaskList; //子网掩码
	
	public List<Gateway> getGatewayList() {
		return gatewayList;
	}
	public void setGatewayList(List<Gateway> gatewayList) {
		this.gatewayList = gatewayList;
	}
	public List<Subnetmask> getSubnetmaskList() {
		return subnetmaskList;
	}
	public void setSubnetmaskList(List<Subnetmask> subnetmaskList) {
		this.subnetmaskList = subnetmaskList;
	}
	public int getHostRoomId() {
		return hostRoomId;
	}
	public void setHostRoomId(int hostRoomId) {
		this.hostRoomId = hostRoomId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUpPort() {
		return upPort;
	}
	public void setUpPort(String upPort) {
		this.upPort = upPort;
	}
	public String getInterchanger() {
		return interchanger;
	}
	public void setInterchanger(String interchanger) {
		this.interchanger = interchanger;
	}
	public String getManageIp() {
		return manageIp;
	}
	public void setManageIp(String manageIp) {
		this.manageIp = manageIp;
	}
	public List<ChestIpRange> getChestIpRangeList() {
		return chestIpRangeList;
	}
	public void setChestIpRangeList(List<ChestIpRange> chestIpRangeList) {
		this.chestIpRangeList = chestIpRangeList;
	}
	public List<InstanceIp> getIpList() {
		return ipList;
	}
	public void setIpList(List<InstanceIp> ipList) {
		this.ipList = ipList;
	}
	public DeviceInstance getDeviceInstance() {
		return deviceInstance;
	}
	public void setDeviceInstance(DeviceInstance deviceInstance) {
		this.deviceInstance = deviceInstance;
	}
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public String getChestNumber() {
		return chestNumber;
	}
	public void setChestNumber(String chestNumber) {
		this.chestNumber = chestNumber;
	}
	public String getChestType() {
		return chestType;
	}
	public void setChestType(String chestType) {
		this.chestType = chestType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getHostRoom() {
		return hostRoom;
	}
	public void setHostRoom(String hostRoom) {
		this.hostRoom = hostRoom;
	}
	public String getDdos() {
		return ddos;
	}
	public void setDdos(String ddos) {
		this.ddos = ddos;
	}
	public String getBandWidth() {
		return bandWidth;
	}
	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public int getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
