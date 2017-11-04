package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

public class ServerRent implements Serializable {
	
	private String room;
	private String model;
	private String ram;
	private String disk;
	private String ddos;
	private String width;
	private String ttlNum;
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
	public String getTtlNum() {
		return ttlNum;
	}
	public void setTtlNum(String ttlNum) {
		this.ttlNum = ttlNum;
	}
	
	
	
}
