package com.baoidc.idcserver.core;

public class ServerException extends RuntimeException {
	
	private String code;
	private String message;
	
	public ServerException(String code,String message){
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 根据枚举类型构造异常类
	 * @param errorCode
	 */
	public ServerException(ErrorCode errorCode){
		this.code = errorCode.getCode();
		this.message = errorCode.getMsg();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
