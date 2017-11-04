package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Operation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 524071633820838078L;
	
	private int id;
	private String optName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOptName() {
		return optName;
	}
	public void setOptName(String optName) {
		this.optName = optName;
	}
	
}
