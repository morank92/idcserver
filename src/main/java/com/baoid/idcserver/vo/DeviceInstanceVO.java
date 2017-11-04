package com.baoid.idcserver.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.InstanceParam;
import com.baoidc.idcserver.po.ProductInstance;
import com.baoidc.idcserver.po.User;

@XmlRootElement
public class DeviceInstanceVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4402312094517677759L;
	
	private int id;
	private int serviceId;//服务id。如：设备租用、机柜租用、设备托管
	private String instanceNum;
	private int status;
	private int instanceType;//实例类型：如：设备租用里面有其他类型的租用，服务器租用、关联productId
	private String deviceTypeName; //设备类型名称
	private User user;
	private String startTime;
	private String endTime;
	private double price;
	private String productName;//设备类型名称
	private List<InstanceParam> instanceParamList;//数据库存储的实例属性值，抽象的参数
	private ProductInstance productInstance;//页面展示的产品实例，经过反射将抽象属性值转化为了明确的对象
	private List<InstanceIp> ipList;//实例的ip集
	
	
	public String getInstanceNum() {
		return instanceNum;
	}
	public void setInstanceNum(String instanceNum) {
		this.instanceNum = instanceNum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public ProductInstance getProductInstance() {
		return productInstance;
	}
	public void setProductInstance(ProductInstance productInstance) {
		this.productInstance = productInstance;
	}
	public List<InstanceIp> getIpList() {
		return ipList;
	}
	public void setIpList(List<InstanceIp> ipList) {
		this.ipList = ipList;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	
}
