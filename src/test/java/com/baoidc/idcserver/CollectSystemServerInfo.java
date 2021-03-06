package com.baoidc.idcserver;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class CollectSystemServerInfo {

	//获取cpu使用率  
	@Test
	public void collectCPU() {  
		TransportMapping transport = null ;  
		Snmp snmp = null ;  
		CommunityTarget target;  
		String[] oids = {"1.3.6.1.2.1.25.3.3.1.2"};  
		try {  
		transport = new DefaultUdpTransportMapping();  
		snmp = new Snmp(transport);//创建snmp  
		snmp.listen();//监听消息  
		target = new CommunityTarget();  
		target.setCommunity(new OctetString("public"));  
		target.setRetries(2);  
		target.setAddress(GenericAddress.parse("udp:192.168.11.15/161"));  
		target.setTimeout(8000);  
		target.setVersion(SnmpConstants.version2c);  
		TableUtils tableUtils = new TableUtils(snmp, new PDUFactory() {  
					public PDU createPDU(Target arg0) {  
					PDU request = new PDU();  
					request.setType(PDU.GET);  
					return request;  
				}

					public PDU createPDU(MessageProcessingModel arg0) {
						// TODO Auto-generated method stub
						return null;
					}  
			});  
		OID[] columns = new OID[oids.length];  
		for (int i = 0; i < oids.length; i++) 
		columns[i] = new OID(oids[i]); 
		List<TableEvent> list = tableUtils.getTable(target, columns, null, null);  
		if(list.size()==1 && list.get(0).getColumns()==null){  
		System.out.println(" null");  
		}else{  
		int percentage = 0;  
		for(TableEvent event : list){  
		VariableBinding[] values = event.getColumns();  
		if(values != null)   
		percentage += Integer.parseInt(values[0].getVariable().toString());  
		}  
		System.out.println("CPU利用率为："+percentage/list.size()+"%");
		}  
		} catch(Exception e){  
		e.printStackTrace();  
		}finally{  
		try {  
		if(transport!=null)  
		transport.close();  
		if(snmp!=null)  
		snmp.close();  
		} catch (IOException e) {  
		e.printStackTrace();  
		}  
		}  
	}
	
	//获取内存相关信息  
	@Test
	public void collectMemory() {
	TransportMapping transport = null ;  
	Snmp snmp = null ;  
	CommunityTarget target;  
	String[] oids = {"1.3.6.1.2.1.25.2.3.1.2",  //type 存储单元类型  
				     "1.3.6.1.2.1.25.2.3.1.3",  //descr  
				     "1.3.6.1.2.1.25.2.3.1.4",  //unit 存储单元大小  
				     "1.3.6.1.2.1.25.2.3.1.5",  //size 总存储单元数  
				     "1.3.6.1.2.1.25.2.3.1.6"}; //used 使用存储单元数;  
	String PHYSICAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.2";//物理存储  
	String VIRTUAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.3"; //虚拟存储  
	String pan_memory = "1.3.6.1.2.1.25.2.1.4";//盘？
	try {
	transport = new DefaultUdpTransportMapping();  
	snmp = new Snmp(transport);//创建snmp  
	snmp.listen();//监听消息  
	target = new CommunityTarget();  
	target.setCommunity(new OctetString("public"));  
	target.setRetries(2);  
	target.setAddress(GenericAddress.parse("udp:192.168.11.15/161"));  
	target.setTimeout(8000);  
	target.setVersion(SnmpConstants.version2c);  
	TableUtils tableUtils = new TableUtils(snmp, new PDUFactory() {  
	public PDU createPDU(Target arg0) {  
	PDU request = new PDU();  
	request.setType(PDU.GET);  
	return request;  
	}

	public PDU createPDU(MessageProcessingModel arg0) {
		// TODO Auto-generated method stub
		return null;
	}  
	});  
	OID[] columns = new OID[oids.length];  
	for (int i = 0; i < oids.length; i++)  
	columns[i] = new OID(oids[i]);  
	
	List<TableEvent> list = tableUtils.getTable(target, columns, null, null);  
	if(list.size()==1 && list.get(0).getColumns()==null){  
	System.out.println(" null");  
	}else{  
	for(TableEvent event : list){  
	VariableBinding[] values = event.getColumns();  
	if(values == null) continue;  
	int unit = Integer.parseInt(values[2].getVariable().toString());//unit 存储单元大小  
	int totalSize = Integer.parseInt(values[3].getVariable().toString());//size 总存储单元数  
	int usedSize = Integer.parseInt(values[4].getVariable().toString());//used  使用存储单元数  
	String oid = values[0].getVariable().toString();  
	if (PHYSICAL_MEMORY_OID.equals(oid)){  
	System.out.println("PHYSICAL_MEMORY----->物理内存大小："+(long)totalSize * unit/(1024*1024*1024)+"G   内存使用率为："+(long)usedSize*100/totalSize+"%");  
	}else if (VIRTUAL_MEMORY_OID.equals(oid)) {  
		System.out.println("VIRTUAL_MEMORY----->虚拟内存大小："+(long)totalSize * unit/(1024*1024*1024)+"G   内存使用率为："+(long)usedSize*100/totalSize+"%");  
	} else if(pan_memory.equals(oid)){
		System.out.println("pan--->>> "+(long)totalSize * unit/(1024*1024*1024)+"G   /????为："+(long)usedSize*100/totalSize+"%");
	}
	}  
	}  
	} catch(Exception e){  
	e.printStackTrace();  
	}finally{  
	try {  
	if(transport!=null)  
	transport.close();  
	if(snmp!=null)  
	snmp.close();  
	} catch (IOException e) {  
	e.printStackTrace();  
	}  
	}  
	}  
	

}