package com.baoidc.idcserver.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;



import com.baoidc.idcserver.core.AES256Cipher;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.IdcServerUtils;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.ServerException;
import com.baoidc.idcserver.dao.IUserAccountDAO;
import com.baoidc.idcserver.dao.IUserDAO;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.service.ITokenService;
import com.baoidc.idcserver.service.IUserService;

@Service("userService")
public class UseServiceImpl implements IUserService {
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IUserAccountDAO userAccountDAO;
	
	@Resource
	private ITokenService tokenService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	public int addNewUser(User user,int userType) {
		int userId = 0;
		if(user != null && userType == 1){
			switch (userType) {
			case 1: //新增普通用户
				user.setPassword(encryptPassword(user.getPassword()));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String registerTime = sdf.format(new Date());
				user.setRegisterTime(registerTime);
				user.setUserRole(1); //新增角色
				userDAO.addNewUser(user);
				userId = user.getId();
				if(userId != 0){//成功创建用户之后创建账户
					UserAccount userAccount = new UserAccount();
					userAccount.setUserId(userId);
					userAccount.setCreateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
					userAccount.setChargeTotal(0.00); //初始充值金额为0
					userAccount.setConsumeTotal(0.00); //初始消费金额为0
					userAccountDAO.addNewUserAccount(userAccount);
				}
				break;
			default:
				break;
			}
		}
		return userId;
	}

	public User getUserInfoById(int id) {
		return userDAO.getUserInfoById(id);
	}

	public List<User> getAllUser(String qqNum,int userId) {
		return userDAO.getAllUser(qqNum,userId);
	}

	public void updateUserInfo(User user) {
		if(user != null){
			String password = user.getPassword();
			if(password != null && StringUtils.isNotBlank(password)){
				user.setPassword(encryptPassword(password));
			}
			userDAO.updateUserInfo(user);
		}
	}
	
/*	public ResponseMessage doLogin(String email,String inPassword) throws ServerException {
		ResponseMessage resMessage = null;
		if(email != null){
			User user = userDAO.getUserInfoByEmail(email);
			if(user == null){
				resMessage = new ResponseMessage(ErrorCode.NOT_USER); //用户不存在
			}else{
				String password = user.getPassword();
				if(inPassword != null && StringUtils.isNotBlank(inPassword)){
					if(password.equals(encryptPassword(inPassword))){
						resMessage = new ResponseMessage(ErrorCode.USER_LOGIN_SUCCESS);
						//将用户Id和token信息传送到前端
						StringBuilder sBuilder = new StringBuilder();
						sBuilder.append(user.getId()).append("|");
						if(tokenFlag == 0){//重新生成token
							String tokenCode = tokenService.addNewToken(user);
							sBuilder.append(tokenCode);
							resMessage.setData(sBuilder.toString());
						}else{
							if(oldToken != null){
								String tokenCode = tokenService.refreshToken(oldToken);
								sBuilder.append(tokenCode);
								resMessage.setData(sBuilder.toString());
							}
						}
						String tokenCode = IdcServerUtils.createTokenCode(user.getEmail());
						redisUtil.set(user.getEmail(), tokenCode, 1800);
						resMessage.setData(tokenCode);
					}else{
						resMessage = new ResponseMessage(ErrorCode.USER_PASSWORD_WRONG);//密码错误
					}
				}
			}
		}
		return resMessage;
	}*/
	//登陆
	public ResponseMessage doLogin(String email,String inPassword,HttpServletRequest request) throws ServerException {
		ResponseMessage resMessage = null;
		if(email != null){
			User user = userDAO.getUserInfoByEmail(email);
			if(user == null){
				//throw new ServerException(ErrorCode.NOT_USER); //用户不存在
				resMessage = new ResponseMessage(ErrorCode.NOT_USER);
			}else{
				String password = user.getPassword();
				if(inPassword != null && StringUtils.isNotBlank(inPassword)){
					if(password.equals(encryptPassword(inPassword))){
						//throw new ServerException(ErrorCode.USER_LOGIN_SUCCESS); //验证通过
						resMessage = new ResponseMessage(ErrorCode.USER_LOGIN_SUCCESS);
						request.setAttribute("USER-ID", user.getId());
						StringBuilder sBuilder = new StringBuilder();
						sBuilder.append(user.getId()).append("|").append(user.getEmail()).append("|");
						String tokenCode = IdcServerUtils.createTokenCode(user.getEmail(),user.getId(),0);
						sBuilder.append(tokenCode);
						redisUtil.set(user.getEmail(), tokenCode, 1800);
						resMessage.setData(sBuilder.toString());
					}else{
						//throw new ServerException(ErrorCode.USER_PASSWORD_WRONG); //密码错误
						resMessage = new ResponseMessage(ErrorCode.USER_PASSWORD_WRONG);
					}
				}
			}
		}
		return resMessage;
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

	public int checkEmail(String email) {
		return userDAO.checkEmail(email);
	}

	//用户退出
	public void logout(String userEmail) {
		Jedis jedis = redisUtil.getJedis();
		Long del = jedis.del(userEmail);
		jedis.close();
	}

}
