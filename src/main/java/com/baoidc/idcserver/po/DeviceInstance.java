package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

//设备实例：包含数据库存储的类型，和页面展示对应的类型，以及公共的字段
public class DeviceInstance implements Serializable {

	private int instanceId;
	private int serviceId;//服务id。如：设备租用、机柜租用、设备托管
	private int deviceId;//关联的设备id
	private int chestId;//关联的机柜id
	private String instanceNum;//设备编号
	private int status;
	private int instanceType;//实例类型：如：设备租用里面有其他类型的租用，服务器租用、关联productId
	private String deviceName; //设备类型名称
	private int userId;
	private String startTime;
	private String endTime;
	private double price;//总价
	private double sourcePrice;//创建本实例时候的源价格，作为后面的升级需要维护的价格，且续费也是依赖于此价格计算
	private String productName;//订单产品类型名称，来源于订单、产品
	//private String deviceName;//设备类型名称：来源于资产
	private List<InstanceParam> instanceParamList;//数据库存储的实例属性值，抽象的参数
	private ProductInstance productInstance;//页面展示的产品实例，经过反射将抽象属性值转化为了明确的对象
	private List<InstanceIp> ipList;//实例的ip集
	private String orderId;//每个实例由哪一个订单生成的，作为订单页面关联的实例里面的ip
	private String room;//生成实例时候产生，作为首页根据该机房查询用
	private String encrypt;//redis存储标识
	private int autoRenewStatus;//自动续费的状态，0为未开启，1为已开启
	private User user; //实例用户
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getAutoRenewStatus() {
		return autoRenewStatus;
	}
	public void setAutoRenewStatus(int autoRenewStatus) {
		this.autoRenewStatus = autoRenewStatus;
	}
	public double getSourcePrice() {
		return sourcePrice;
	}
	public void setSourcePrice(double sourcePrice) {
		this.sourcePrice = sourcePrice;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getChestId() {
		return chestId;
	}
	public void setChestId(int chestId) {
		this.chestId = chestId;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public String getInstanceNum() {
		return instanceNum;
	}
	public void setInstanceNum(String instanceNum) {
		this.instanceNum = instanceNum;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public String getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public List<InstanceIp> getIpList() {
		return ipList;
	}
	public void setIpList(List<InstanceIp> ipList) {
		this.ipList = ipList;
	}
	public ProductInstance getProductInstance() {
		return productInstance;
	}
	public void setProductInstance(ProductInstance productInstance) {
		this.productInstance = productInstance;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<InstanceParam> getInstanceParamList() {
		return instanceParamList;
	}
	public void setInstanceParamList(List<InstanceParam> instanceParamList) {
		this.instanceParamList = instanceParamList;
	}
	
}
