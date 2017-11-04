package com.baoidc.idcserver.po;

import java.io.Serializable;

public class MemoryBank implements Serializable {
	private int memoryId;//某一类的id
	private String memoryType;
	private int memoryUsable;
	private String inHostRoom;
	
	
	
	public String getInHostRoom() {
		return inHostRoom;
	}
	public void setInHostRoom(String inHostRoom) {
		this.inHostRoom = inHostRoom;
	}
	public int getMemoryId() {
		return memoryId;
	}
	public void setMemoryId(int memoryId) {
		this.memoryId = memoryId;
	}
	public String getMemoryType() {
		return memoryType;
	}
	public void setMemoryType(String memoryType) {
		this.memoryType = memoryType;
	}
	public int getMemoryUsable() {
		return memoryUsable;
	}
	public void setMemoryUsable(int memoryUsable) {
		this.memoryUsable = memoryUsable;
	}
	
	
	
}
