package com.baoidc.idcserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.UserLog;


public interface IUserLogDAO {
	
	public void addUserLog(UserLog userLog);

	//查询用户操作日志
	public List<UserLog> queryUserOptLog(@Param("userId")int userId,@Param("adminId")int adminId,@Param("startTime") String startTime,@Param("endTime") String endTime);

}
