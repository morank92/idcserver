package com.baoidc.idcserver.core;

/**
 * 资源属性标签名枚举类
 * @author Administrator
 *
 */
public enum SourceTag {
	
	SNMP_SEC("baoidc.snmpMessage.security"),
	GETINSTANCE_SEC("baoidc.deviceInstance.security"),
	COLUMN_INDEX("baoidc.visibleColumn.security"),
	NEWORDER_SEC("baoidc.newOrder.security"),
	SUBMITORDER_SEC("baoidc.submitOrder.security"),
	TTL("ttl"),RAM("ram"),DISK("disk"),DDOS("ddos"),WIDTH("width"),
	POWER("power"),IPCOUNT("ipcount"),COUNT("count"),MODEL("model"),
	CHEST_DDOS("chestddos"),
	SERVER_ID("serverid"),
	SERVER_ID_NAME("服务器ID"),
	SIZE("size"),DURATION("duration"),ROOM("room");
	
	private String tagVal;
	
	SourceTag(String tagVal){
		this.tagVal = tagVal;
	}

	public String getTagVal() {
		return tagVal;
	}

	public void setTagVal(String tagVal) {
		this.tagVal = tagVal;
	}

}
