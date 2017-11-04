package com.baoidc.idcserver.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7379423792982570961L;
	
	private String resp_code;
	private String resp_msg;
	
	public ErrorEntity(){}
	
	public ErrorEntity(String resp_code,String resp_msg){
		this.resp_code = resp_code;
		this.resp_msg = resp_msg;
	}

	public String getResp_code() {
		return resp_code;
	}

	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}
	
}
