package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SysApi implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4537325033062957257L;
	
	private int id;
	private String apiName;
	private String apiAddr;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getApiAddr() {
		return apiAddr;
	}
	public void setApiAddr(String apiAddr) {
		this.apiAddr = apiAddr;
	}

}
