package com.baoidc.idcserver.service;

import java.util.List;

import com.baoidc.idcserver.po.UserLog;


public interface IUserLogService {
	
	public void addUserLog(UserLog userLog);

	//查询用户操作日志
	public List<UserLog> queryUserOptLog(int userId,int adminId,String startTime, String endTime);

}
