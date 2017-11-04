package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

	private int id;
	private String orderId;
	private int status;
	private String orderType;
	private int userId;
	private List<UserContacts> userContactsList;//联系人信息
	private String createTime;//创建时间
	private String payTime;//支付时间
	private String startTime;//开始时间--实例创建的时间
	private String endTime;//结束时间--实例到期的时间
	private int instanceId;
	private double price;
	private double finalPrice;//管理员的议价
	private double sourcePrice;//该订单为升级的时候存在的实例源标配价格：即一个月对应的升级价格，后面该订单处理的时候需要将此参数加进实例的源标配价格
	private String productName;
	private int serviceId;//服务类型
	private String userName;
	private List<OrderParam> orderParamList;
	private String encrypt;//作为保证前后取出来的数据是同一对象
	private DeviceInstance deviceInstance;//关联的实例
	private List<InstanceIp> ipList;//关联的ip集合
	private int serverId;//机柜实例下的服务器id，由页面提交参数而来；给服务器升级的时候所需要提交，且会增加到该订单中的oderparam中去
	//private Remark remark;//订单创建时的备注--对象
	private String remark;//备注
	private String queryIpstr;//ip查询的参数
	private String queryEndtime;//ip查询的结束时间
	
	private int customerManagerId;//客户经理的id
	
	
	public int getCustomerManagerId() {
		return customerManagerId;
	}
	public void setCustomerManagerId(int customerManagerId) {
		this.customerManagerId = customerManagerId;
	}
	public double getSourcePrice() {
		return sourcePrice;
	}
	public void setSourcePrice(double sourcePrice) {
		this.sourcePrice = sourcePrice;
	}
	public String getQueryEndtime() {
		return queryEndtime;
	}
	public void setQueryEndtime(String queryEndtime) {
		this.queryEndtime = queryEndtime;
	}
	public String getQueryIpstr() {
		return queryIpstr;
	}
	public void setQueryIpstr(String queryIpstr) {
		this.queryIpstr = queryIpstr;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
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
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public List<UserContacts> getUserContactsList() {
		return userContactsList;
	}
	public void setUserContactsList(List<UserContacts> userContactsList) {
		this.userContactsList = userContactsList;
	}
	public double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<OrderParam> getOrderParamList() {
		return orderParamList;
	}
	public void setOrderParamList(List<OrderParam> orderParamList) {
		this.orderParamList = orderParamList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
