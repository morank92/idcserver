package com.baoidc.idcserver.dao;

import java.util.List;

import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.OrderParam;
import com.baoidc.idcserver.po.ServerRentOrder;
import com.baoidc.idcserver.po.ServerRoom;
import com.baoidc.idcserver.po.SnmpConfUser;
import com.baoidc.idcserver.vo.query.OrderQueryVO;


public interface ISnmpDAO {

	//获取用户针对于实例的snmp配置信息
	SnmpConfUser getSnmpConf4User(SnmpConfUser snmpconf);

	//创建snmp配置信息
	int setSnmpMessage(SnmpConfUser snmpConf);

	//更新snmp配置信息
	void updateSnmpConf(SnmpConfUser snmpConf);

	


}
