package com.baoidc.idcserver.po;

public class Token {
	
	private int tokenId;
	private String tokenCode;
	private String userName;
	private String createTime;
	
	public int getTokenId() {
		return tokenId;
	}
	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}
	public String getTokenCode() {
		return tokenCode;
	}
	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
