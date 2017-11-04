package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

public class AutoRenewConf implements Serializable {

	private int id;
	private int instanceId;//设备id
	private String duration;//时长
	private User user;//该实例的用户
	private int status;//状态：默认为0，已经为此实例下单，且订单未处理时 的状态为 1，当处理后恢复到0
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	
	
	
}
