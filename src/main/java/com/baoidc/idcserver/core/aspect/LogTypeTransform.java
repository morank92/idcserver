package com.baoidc.idcserver.core.aspect;

public class LogTypeTransform {
	public static String transform(String str){
		String newStr = "";
		switch (str) {
		case "DEVICERENT":
			newStr = "设备租用";
			break;
		case "DEVICERENT-CREATE":
			newStr = "服务器租用/创建";
			break;
		case "DEVICERENT-UP":
			newStr = "设备租用/升级";
			break;
		case "DEVICERENT-SNMPCONF":
			newStr = "设备租用/SNMP";
			break;
		case "DEVICEDEPOSIT":
			newStr = "设备托管";
			break;
		case "DEVICEDEPOSIT-SNMPCONF":
			newStr = "设备托管/SNMP";
			break;
		case "DEVICEDEPOSIT-CREATE":
			newStr = "设备托管/创建";
			break;
		case "DEVICEDEPOSIT-UP":
			newStr = "设备托管/升级";
			break;
		case "CHESTRENT":
			newStr = "机柜租用";
			break;
		case "RACKRENT-CREATE":
			newStr = "机柜租用/创建";
			break;
		case "RACKRENT-RENEW":
			newStr = "机柜租用/续费";
			break;
		case "RACKRENT-UP":
			newStr = "机柜租用/升级";
			break;
		case "RACKRENT-IN-UP":
			newStr = "机柜租用/设备/升级";
			break;
		case "XUFEI-DEAL":
			newStr = "处理续费的订单";
			break;
		case "UP-DEAL":
			newStr = "处理升级的订单";
			break;
		case "CREATE-DEAL":
			newStr = "新建实例";
			break;
		case "MATERIALMANAGE":
			newStr = "资料管理";
			break;
		case "ACCOUNTMANAGE":
			newStr = "账户管理";
			break;
		case "ORDERMANAGE":
			newStr = "订单管理";
			break;
		case "WORKORDERMANAGE":
			newStr = "工单管理";
			break;
		case "RECORDMANAGE":
			newStr = "备案管理";
			break;
		case "USERREPORT":
			newStr = "用户报表";
			break;
		case "USERMANAGE":
			newStr = "用户管理";
			break;
		case "ASSETMANAGE":
			newStr = "资产管理";
			break;
		case "ASSETRECYCLE":
			newStr = "资产回收";
			break;
		case "FINANCEANALYZE":
			newStr = "财务分析";
			break;
		case "CONSUMEANALYZE":
			newStr = "消费分析";
			break;
		case "ENCASHMENTMANAGE":
			newStr = "提现管理";
			break;
		case "PARAMCONFIG":
			newStr = "参数配置";
			break;
		case "PRODUCTCONFIG":
			newStr = "产品配置";
			break;
		case "ARTICLECONTENT":
			newStr = "文章内容";
			break;
		case "ROLEMANAGE":
			newStr = "角色管理";
			break;
		case "SYSTEMUSER":
			newStr = "系统用户";
			break;
		case "SYSTEMMENU":
			newStr = "菜单管理";
			break;	
		case "LOGIN":
			newStr = "登陆";
			break;	
		case "LOGOUT":
			newStr = "注销";
			break;
		case "SYSOPERATION":
			newStr = "系统后台操作";
			break;	
		case "USERREPORTCONF":
			newStr = "告警配置";
			break;	
		default:
			break;
		}
		return newStr;
	}
}
