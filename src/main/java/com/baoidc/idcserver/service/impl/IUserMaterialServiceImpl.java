package com.baoidc.idcserver.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.core.AES256Cipher;
import com.baoidc.idcserver.dao.ISystemDAO;
import com.baoidc.idcserver.dao.IUserMaterialDao;
import com.baoidc.idcserver.po.CustomerManager;
import com.baoidc.idcserver.po.SysManageUser;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserContacts;
import com.baoidc.idcserver.service.IUserMaterialService;
@Service
public class IUserMaterialServiceImpl implements IUserMaterialService {

	@Autowired
	private IUserMaterialDao userMaterialDao;
	
	@Autowired
	private ISystemDAO systemDao;
	
	public void addUserContact(UserContacts userContacts) {
		userMaterialDao.addUserContact(userContacts);
	}

	public void updateUserContact(UserContacts userContacts) {
		userMaterialDao.updateUserContact(userContacts);
	}

	public void deleteUserContact(Integer[] userContactsIds) {
		for (Integer id : userContactsIds) {
			userMaterialDao.deleteUserContact(id);
		}
	}

	public List<UserContacts> getUserContacts(Integer userId) {
		return userMaterialDao.getUserContacts(userId);
	}

	public SysManageUser getCustomerManager(Integer userId) {
		
		return systemDao.getCustomerManageByUserId(userId);
	}

	public User getUserByUserId(Integer userId)throws Exception{
			User user = userMaterialDao.getUserByUserId(userId);
			AES256Cipher  aes = new AES256Cipher();
			if(user.getAuthNum()!=null){
				user.setAuthNum(aes.decrypt(user.getAuthNum(), "lanysec", ""));
			}
			return user;
	}

	public void updateUser(User user)throws Exception {
			if(user.getAuthName()!=null){
				AES256Cipher  aes = new AES256Cipher();
				user.setAuthNum(aes.encrypt(user.getAuthNum(), "lanysec", ""));
			}
			userMaterialDao.updateUser(user);
	}

	public void updateUserPassword(Integer userId,String password) {
		password = encryptPassword(password);
		userMaterialDao.updateUserPassword(userId, password);
	}
	
	
	
	/**
	 * 密码加密
	 * @param password
	 * @return
	 */
	private String encryptPassword(String password){
		String encryptedPass = "";
		if(password != null && StringUtils.isNotBlank(password)){
			try{
				AES256Cipher  aes = new AES256Cipher();
				encryptedPass = aes.encrypt(password, "lanysec", "");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return encryptedPass;
	}

	public void updateUserPhone(Integer userId,
			String phone) {
		userMaterialDao.updateUserPhone(userId, phone);
	}

	public void updateUserQQNum(Integer userId,String qqNum) {
		userMaterialDao.updateUserQQNum(userId, qqNum);
	}

	public void updateUserzfbao(Integer userId, String zfbao) {
		userMaterialDao.updateUserzfbao(userId,zfbao);	
	}
	
	//查询所有的客户经理
	public List<SysManageUser> getAllCustomerManager(){
		return systemDao.getAllCustomerManager();
	}
	//给用户分配客户经理
	public void allotManage(Integer userId, int manageId){
		userMaterialDao.allotManage(userId,manageId);
	}

}
