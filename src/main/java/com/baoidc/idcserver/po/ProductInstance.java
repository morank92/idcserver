package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

public class ProductInstance implements Serializable {
	
	private String room;//机房
	private String model;//服务器型号
	private String size;//机柜规格
	private String height;//服务器托管的规格
	private String ram;//内存
	private String disk;//硬盘
	private String ddos;//ddos防护
	private String chestddos;//机柜的ddos防护
	private String width;//带宽
	private String ttl;//线路
	private String power;//电源
	private String ipcount;//ip数量
	private String count;//产品数量
	private String duration;//时长
	
	
	public String getChestddos() {
		return chestddos;
	}
	public void setChestddos(String chestddos) {
		this.chestddos = chestddos;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getIpcount() {
		return ipcount;
	}
	public void setIpcount(String ipcount) {
		this.ipcount = ipcount;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getRam() {
		return ram;
	}
	public void setRam(String ram) {
		this.ram = ram;
	}
	public String getDisk() {
		return disk;
	}
	public void setDisk(String disk) {
		this.disk = disk;
	}
	public String getDdos() {
		return ddos;
	}
	public void setDdos(String ddos) {
		this.ddos = ddos;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getTtl() {
		return ttl;
	}
	public void setTtl(String ttl) {
		this.ttl = ttl;
	}
}
