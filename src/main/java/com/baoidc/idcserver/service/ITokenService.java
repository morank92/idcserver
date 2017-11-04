package com.baoidc.idcserver.service;

import com.baoidc.idcserver.po.Token;
import com.baoidc.idcserver.po.User;

public interface ITokenService {
	
	public Token getTokenByUserName(String userName);
	
	public String refreshToken(Token token);
	
	public String addNewToken(User user);
	
	public void deleteToken(String userName);
	
}
