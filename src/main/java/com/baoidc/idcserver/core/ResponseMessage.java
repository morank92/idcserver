package com.baoidc.idcserver.core;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseMessage implements Serializable{
	
	
	private String flag;
	private Object obj;
	
	public ResponseMessage(){}
	
	public ResponseMessage(ErrorCode errorCode){
		this.code = errorCode.getCode();
		this.msg = errorCode.getMsg();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4299590525235302L;
	
	private String code;
	private String msg;
	private Object data;
	private String businessDesc;
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getBusinessDesc() {
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}

}
