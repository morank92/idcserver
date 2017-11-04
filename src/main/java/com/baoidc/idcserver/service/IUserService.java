package com.baoidc.idcserver.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.ServerException;
import com.baoidc.idcserver.po.User;

public interface IUserService {
	
	public int addNewUser(User user,int userType);
	
	public User getUserInfoById(int id);
	
	public List<User> getAllUser(String qqNum,int userId);
	
	public void updateUserInfo(User user);
	
	public ResponseMessage doLogin(String email,String inPassword, HttpServletRequest request) throws ServerException;
	//判断邮箱是否被注册
	public int checkEmail(String email);

	//用户退出
	public void logout(String userEmail);

}
