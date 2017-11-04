package com.baoidc.idcserver.core;

import java.util.HashMap;
import java.util.Map;

public class OrderTypeTransfer {
	
	private final static Map<String, String> orderTypeMap = new HashMap<String, String>();
	
	static{
		orderTypeMap.put("1", "服务器租用");
		orderTypeMap.put("2", "服务器托管");
		orderTypeMap.put("3", "机柜租用");
		orderTypeMap.put("4", "服务升级");
		orderTypeMap.put("5", "服务续费");
		orderTypeMap.put("6", "服务器DDOS升级");
		orderTypeMap.put("7", "机柜DDOS升级");
	}
	
	public static String getOrderTypeText(String orderType){
		return orderTypeMap.get(orderType);
	}

}
