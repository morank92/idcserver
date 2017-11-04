package com.baoidc.idcserver.service;


import com.baoidc.idcserver.po.SnmpConfUser;
import com.baoidc.idcserver.po.ipmiUser;
import com.baoidc.idcserver.vo.query.SnmpQuery;
import com.baoidc.idcserver.vo.query.SnmpResult;

public interface ISnmpService {
	
	//创建snmp配置
	public SnmpConfUser setSnmpMessage(SnmpConfUser snmpConf);

	//查询
	public SnmpResult getSnmpResultBySnmpConfUser(SnmpConfUser snmpConf);

	//获取配置信息snmp
	public SnmpConfUser getSnmpConf(SnmpConfUser snmpConf);

	//更新配置信息
	public void updateSnmpConf(SnmpConfUser snmpConf);

	//开关机
	public void toConfirmIpmi(ipmiUser ipuser);

	//开机
	public boolean toStartByIpmi(ipmiUser ipuser) throws Exception;

	//关机
	public boolean toEndByIpmi(ipmiUser ipuser) throws Exception;
	
	
}
