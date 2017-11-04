package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

import com.baoid.idcserver.vo.ServerPort;
/*
 * 设备   例如  服务器
 */
public class Servers implements Serializable {
	private int serverId;
	private String orderId;
	private String deviceType;
	private String serverNumber;
	private String serverDetail;//设备型号
	private String createTime;
	private String memorySize;
	private String diskSize;
	private int useStatus;
	private String inHostRoom;//机房
	private int hostRoomId;//机房配置参数id
	private String bandWidth;//带宽
	private String showMemory;//页面显示内存,xx*1 +  xx*1
	private String showDisk;//页面显示硬盘, xx*1 + xx*1
	private String ddos;
	private List<ServerPort> portList;
	private String manageCardIp; //管理卡IP
	private String manageCardPort;//管理卡端口
	private String remark;  //备注
	private User user; //租用人
	private DeviceInstance deviceInstance;
	private List<InstanceIp> ipList;
	private int ownUserId;//资产所属者
	private User ownUser; //服务器所属人
	private Chest chest;
	
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
	public List<ServerPort> getPortList() {
		return portList;
	}
	public void setPortList(List<ServerPort> portList) {
		this.portList = portList;
	}
	public String getManageCardIp() {
		return manageCardIp;
	}
	public void setManageCardIp(String manageCardIp) {
		this.manageCardIp = manageCardIp;
	}
	public String getManageCardPort() {
		return manageCardPort;
	}
	public void setManageCardPort(String manageCardPort) {
		this.manageCardPort = manageCardPort;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getShowMemory() {
		return showMemory;
	}
	public void setShowMemory(String showMemory) {
		this.showMemory = showMemory;
	}
	public String getShowDisk() {
		return showDisk;
	}
	public void setShowDisk(String showDisk) {
		this.showDisk = showDisk;
	}
	public Chest getChest() {
		return chest;
	}
	public void setChest(Chest chest) {
		this.chest = chest;
	}
	public int getOwnUserId() {
		return ownUserId;
	}
	public void setOwnUserId(int ownUserId) {
		this.ownUserId = ownUserId;
	}
	
	public User getOwnUser() {
		return ownUser;
	}
	public void setOwnUser(User ownUser) {
		this.ownUser = ownUser;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public DeviceInstance getDeviceInstance() {
		return deviceInstance;
	}
	public void setDeviceInstance(DeviceInstance deviceInstance) {
		this.deviceInstance = deviceInstance;
	}
	public List<InstanceIp> getIpList() {
		return ipList;
	}
	public void setIpList(List<InstanceIp> ipList) {
		this.ipList = ipList;
	}
	public String getInHostRoom() {
		return inHostRoom;
	}
	public void setInHostRoom(String inHostRoom) {
		this.inHostRoom = inHostRoom;
	}
	public String getBandWidth() {
		return bandWidth;
	}
	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}
	public String getDdos() {
		return ddos;
	}
	public void setDdos(String ddos) {
		this.ddos = ddos;
	}
	public String getServerNumber() {
		return serverNumber;
	}
	public void setServerNumber(String serverNumber) {
		this.serverNumber = serverNumber;
	}
	public String getServerDetail() {
		return serverDetail;
	}
	public void setServerDetail(String serverDetail) {
		this.serverDetail = serverDetail;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMemorySize() {
		return memorySize;
	}
	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}
	public String getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}
	public int getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	
	
	
}
