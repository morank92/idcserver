package com.baoidc.idcserver.po;

import java.io.Serializable;

public class Disk implements Serializable{
	private int diskId;
	private String diskType;
	private String diskSize;
	private int diskUsable;
	private String inHostRoom;
	
	
	public String getInHostRoom() {
		return inHostRoom;
	}
	public void setInHostRoom(String inHostRoom) {
		this.inHostRoom = inHostRoom;
	}
	public int getDiskId() {
		return diskId;
	}
	public void setDiskId(int diskId) {
		this.diskId = diskId;
	}
	public String getDiskType() {
		return diskType;
	}
	public void setDiskType(String diskType) {
		this.diskType = diskType;
	}
	public String getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}
	public int getDiskUsable() {
		return diskUsable;
	}
	public void setDiskUsable(int diskUsable) {
		this.diskUsable = diskUsable;
	}
	
}
