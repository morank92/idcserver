package com.baoidc.idcserver.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.IdcServerUtils;
import com.baoidc.idcserver.dao.ITokenDAO;
import com.baoidc.idcserver.po.Token;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.service.ITokenService;

@Service("tokenService")
public class TokenServiceImpl implements ITokenService {
	
	@Autowired
	private ITokenDAO tokenDAO;

	public Token getTokenByUserName(String userName) {
		return tokenDAO.getTokenByUserName(userName);
	}

	//刷新token
	public String refreshToken(Token token) {
		String newTokenCode = IdcServerUtils.createTokenCode(token.getUserName(),1,1);
		token.setTokenCode(newTokenCode);
		token.setCreateTime(DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss"));
		tokenDAO.refreshToken(token);
		return newTokenCode;
	}

	//生成新的token存放到数据库
	public String addNewToken(User user) {
		String tokenCode = "";
		if(user !=  null){
			String email = user.getEmail();
			tokenCode = IdcServerUtils.createTokenCode(email,1,1);
			Token token = new Token();
			token.setTokenCode(tokenCode);
			token.setCreateTime(DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss"));
			token.setUserName(email);
			tokenDAO.addNewToken(token);
		}
		return tokenCode;
	}

	public void deleteToken(String userName) {
		tokenDAO.deleteToken(userName);
	}
	

}
