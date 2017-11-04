package com.baoidc.idcserver.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class IdcServerUtils {
	
	public static String encryptPassword(String password){
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
	
	public static String createTokenCode(String userName,int userId,int logType){
		//根据userName和当前时间（yyyyMMddhhmiss)生成一个md5的token
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(userName);
		strBuilder.append("_");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		strBuilder.append(sdf.format(new Date()));
		strBuilder.append("_");
		strBuilder.append(userId);
		if(logType == 0){//用户登录
			strBuilder.append("_");
			strBuilder.append("user");
		}else{ //管理员登录
			strBuilder.append("_");
			strBuilder.append("admin");
		}
		return encryptPassword(strBuilder.toString());
	}
	
	public static String decryptPassword(String encryStr){
		String decryptPass = "";
		if(encryStr != null && StringUtils.isNotBlank(encryStr)){
			try{
				AES256Cipher  aes = new AES256Cipher();
				decryptPass = aes.decrypt(encryStr, "lanysec", "");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return decryptPass;
	}

}
