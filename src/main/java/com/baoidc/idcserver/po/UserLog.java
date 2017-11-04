package com.baoidc.idcserver.po;
/*
 * 操作日志
 */
public class UserLog {
	
	private int logId;
	private String logType;
	private String method;
	private String requestIp;
	private int userId;
	private int adminId;
	private String logUser;//日志来源    user   还是  admin
	private String operation;
	private String doTime;
	private User user;
	private SysManageUser admin;
	
	
	public int getAdminId() {
		return adminId;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	public SysManageUser getAdmin() {
		return admin;
	}
	public void setAdmin(SysManageUser admin) {
		this.admin = admin;
	}
	public String getLogUser() {
		return logUser;
	}
	public void setLogUser(String logUser) {
		this.logUser = logUser;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getDoTime() {
		return doTime;
	}
	public void setDoTime(String doTime) {
		this.doTime = doTime;
	}
}
