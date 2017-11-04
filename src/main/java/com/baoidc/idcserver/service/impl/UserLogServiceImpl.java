package com.baoidc.idcserver.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.dao.IUserLogDAO;
import com.baoidc.idcserver.po.UserLog;
import com.baoidc.idcserver.service.IUserLogService;

@Service
public class UserLogServiceImpl implements IUserLogService {
	
	@Autowired
	private IUserLogDAO userLogDAO;

	public void addUserLog(UserLog userLog) {
		userLogDAO.addUserLog(userLog);
	}
	
	//查询用户操作日志
	public List<UserLog> queryUserOptLog(int userId,int adminId, String startTime, String endTime){
		if(startTime.equals("")){
			//获取7天前的时间
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.DAY_OF_MONTH, -7);
			Date date = gc.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			startTime = sdf.format(date);
		}
		return userLogDAO.queryUserOptLog(userId,adminId,startTime,endTime);
	}

}
