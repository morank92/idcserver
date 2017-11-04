package com.baoidc.idcserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserContacts;

public interface IUserDAO {
	
	//新增用户
	public void addNewUser(User user);
	
	//根据用户Id获取用户信息
	public User getUserInfoById(int id);
	
	//根据用户Id获取用户的联系人信息
	public List<UserContacts> getUserContactsById(int id);
	
	//获取用户列表
	public List<User> getAllUser(@Param("qqNum") String qqNum,@Param("userId")int userId); //可通过qq号码查询
	
	//修改用户信息
	public void updateUserInfo(User user);
	
	//根据邮箱地址获取用户信息，用户登录验证
	public User getUserInfoByEmail(String email);

	//判断邮箱是否存在
	public int checkEmail(String email);

}
