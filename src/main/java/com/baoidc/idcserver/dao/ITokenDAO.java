package com.baoidc.idcserver.dao;

import com.baoidc.idcserver.po.Token;

public interface ITokenDAO {
	
	//根据用户名获取用户token
	public Token getTokenByUserName(String userName);
	
	public void refreshToken(Token token);
	
	public void addNewToken(Token token);
	
	public void deleteToken(String userName);

}
