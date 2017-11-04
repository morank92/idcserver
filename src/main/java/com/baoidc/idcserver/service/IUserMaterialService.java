package com.baoidc.idcserver.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.CustomerManager;
import com.baoidc.idcserver.po.SysManageUser;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserContacts;

public interface IUserMaterialService {
		//新增联系人
		public void addUserContact(UserContacts userContacts);
		
		//修改联系人
		public void updateUserContact(UserContacts userContacts);
		
		//删除联系人
		public void deleteUserContact(Integer[] userContactsIds);
		
		//查询所有联系人
		public List<UserContacts> getUserContacts(Integer userId);
		
		//查询客户经理
		public SysManageUser getCustomerManager(Integer userId);
		
		//查询用户详细信息
		public User getUserByUserId(Integer userId) throws Exception;
		
		//修改用户基本信息
		public void updateUser(User user) throws Exception;
		
		//修改密码
		public void updateUserPassword(@Param("userId")Integer userId,@Param("password")String password);
		
		//修改手机号码
		public void updateUserPhone(@Param("userId")Integer userId,@Param("phone")String phone);
		
		//修改QQ号码
		public void updateUserQQNum(@Param("userId")Integer userId,@Param("qqNum")String qqNum);

		//修改支付宝账号
		public void updateUserzfbao(Integer userId, String zfbao);
		
		//查询所有的客户经理
		public List<SysManageUser> getAllCustomerManager();

		//给用户分配客户经理
		public void allotManage(Integer userId, int manageId);
		
}
