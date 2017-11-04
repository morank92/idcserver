package com.baoidc.idcserver.po;

public class SystemTrans {
	
	private int id;
	private String type;
	private String srcValue;
	private String tagValue;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSrcValue() {
		return srcValue;
	}
	public void setSrcValue(String srcValue) {
		this.srcValue = srcValue;
	}
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

}
