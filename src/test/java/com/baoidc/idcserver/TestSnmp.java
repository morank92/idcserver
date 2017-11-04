package com.baoidc.idcserver;


import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.junit.runner.RunWith;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baoidc.idcserver.dao.AssetManageDAO;
import com.baoidc.idcserver.po.Servers;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
public class TestSnmp {
	
	@Autowired
	private AssetManageDAO assetManageDAO;
	
	@org.junit.Test
	public void testYuanSnmp(){
		//Snmp的三个版本号
        //int ver = SnmpConstants.version3;
        int ver = SnmpConstants.version2c;
        //int ver1 = SnmpConstants.version1;
        CollectSystemInfo manager = new CollectSystemInfo(ver);
         // 构造报文
        PDU pdu = new PDU();//version2
        //ScopedPDU pdu = new ScopedPDU();//version3
         //PDU pdu = new ScopedPDU();
        // 设置要获取的对象ID，这个OID代表远程计算机的名称
         OID oids = new OID("1.3.6.1.4.1.2021.4.5.0");
       pdu.add(new VariableBinding(oids));
        // 设置报文类型
        pdu.setType(PDU.GET);
        //((ScopedPDU) pdu).setContextName(new OctetString("priv"));
        try {
            // 发送消息 其中最后一个是想要发送的目标地址
            manager.sendMessage(false, true, pdu, "udp:192.168.11.15/161");//192.168.1.229 Linux服务器
            //manager.sendMessage(false, true, pdu, "udp:192.168.1.233/161");//192.168.1.233 WinServer2008服务器
        } catch (IOException e) {
            e.printStackTrace();
        }
     }
	
	@org.junit.Test
	public void testCpu(){
		//String addr = "192.168.7.111";
		String addr = "192.168.11.15";
		PDU pdu = new PDU();
		VariableBinding vb = new VariableBinding();
		vb.setOid(new OID("1.3.6.1.4.1.2021.10.1.3.1"));
		pdu.addOID(vb);
		CollectSystemInfo sys = new CollectSystemInfo(2);
		sys.collectInfo(addr, new JSONArray());
		try {
			sys.sendMessage(false, false, pdu, addr+"/161");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	@org.junit.Test
	public void testxxxxx(){
		Servers sv = new Servers();
		sv.setOwnUserId(23);
		
		List<Servers> ns = assetManageDAO.getMatchServers(sv);
		if(ns != null && ns.size() == 0){
			System.out.println("if不为空：  "+ns.size());
		}
		
		System.out.println(ns.size());
		
	}
	
	
}
