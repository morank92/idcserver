package com.baoidc.idcserver.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import com.baoidc.idcserver.po.SnmpConfUser;
import com.baoidc.idcserver.vo.query.SnmpData;
import com.baoidc.idcserver.vo.query.SnmpResult;

public class Snmp2cQuery {
	//获取cpu使用率  
	public double collectCPU(SnmpConfUser snmpUser) {
		
		double cpuUsed = 0;//cpu使用率
		String ipStr = snmpUser.getSnmpIp();
		String comStr = snmpUser.getComWord();//访问的关键字
		String addr = "udp:"+ipStr+"/161";
		TransportMapping transport = null ;  
		Snmp snmp = null ;  
		CommunityTarget target;  
		String[] oids = {"1.3.6.1.2.1.25.3.3.1.2"};  
		try {  
			transport = new DefaultUdpTransportMapping();  
			snmp = new Snmp(transport);//创建snmp  
			snmp.listen();//监听消息  
			target = new CommunityTarget();  
			target.setCommunity(new OctetString(comStr));  
			target.setRetries(2);  
			target.setAddress(GenericAddress.parse(addr));  
			target.setTimeout(2000);  
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
		for (int i = 0; i < oids.length; i++){
			columns[i] = new OID(oids[i]); 
		}
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
			cpuUsed = new BigDecimal((float)percentage/list.size()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			System.out.println("version2的CPU利用率为："+percentage/list.size()+"%");
			}  
		} catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			try {  
				if(transport!=null){  
					transport.close();
				}
				if(snmp!=null)  {
					snmp.close();  
				}
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		}  
		
		return cpuUsed;
		
	}
	
	//获取内存相关信息  
	public SnmpResult collectMemory(SnmpConfUser snmpUser) {
		
		SnmpResult result = new SnmpResult();
		
		List<SnmpData> diskList = new ArrayList<SnmpData>();//硬盘可能有多块
		SnmpData ramData = new SnmpData();//作为内存数据对象
		
		
		String ipStr = snmpUser.getSnmpIp();
		String comStr = snmpUser.getComWord();
		String addr = "udp:"+ipStr+"/161";
		TransportMapping transport = null ;  
		Snmp snmp = null ;  
		CommunityTarget target;  
		String[] oids = {"1.3.6.1.2.1.25.2.3.1.2",  //type 存储单元类型  
					     "1.3.6.1.2.1.25.2.3.1.3",  //descr  
					     "1.3.6.1.2.1.25.2.3.1.4",  //unit 存储单元大小  簇的大小？
					     "1.3.6.1.2.1.25.2.3.1.5",  //size 总存储单元数  簇的数目
					     "1.3.6.1.2.1.25.2.3.1.6"}; //used 使用存储单元数;  使用多少，跟总容量相除就是占用率
		String PHYSICAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.2";//物理存储  
		String VIRTUAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.3"; //虚拟存储  
		String pan_memory = "1.3.6.1.2.1.25.2.1.4";//盘？
		try {
			transport = new DefaultUdpTransportMapping();  
			snmp = new Snmp(transport);//创建snmp  
			snmp.listen();//监听消息  
			target = new CommunityTarget();  
			target.setCommunity(new OctetString(comStr));  
			target.setRetries(2);  
			target.setAddress(GenericAddress.parse(addr));  
			target.setTimeout(2000);  
			target.setVersion(SnmpConstants.version2c);
			TableUtils tableUtils = new TableUtils(snmp, new PDUFactory() {  
				public PDU createPDU(Target arg0) {  
					PDU request = new PDU();  
					request.setType(PDU.GET);  
					return request;  
				}

				public PDU createPDU(MessageProcessingModel arg0) {
					return null;
				}  
			});  
	OID[] columns = new OID[oids.length];  
	for (int i = 0; i < oids.length; i++) { 
		columns[i] = new OID(oids[i]);  
	}
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
				
				System.out.println("PHYSICAL_MEMORY----->物理内存大小version2的："+(long)totalSize * unit/(1024*1024*1024)+"G   内存使用率为："+(long)usedSize*100/totalSize+"%");
				System.out.println("PHYSICAL_MEMORY----->物理内存大小version26666666666的："+(long)usedSize/totalSize);
				
				float ramUsed = (float)usedSize*100/totalSize;//使用率
				
				float ram = (float)totalSize * unit/(1024*1024*1024);//总大小

				//数据转换
				double ramcount = new BigDecimal(ram).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double ramUsage = new BigDecimal(ramUsed).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				double ramUse = new BigDecimal((float)ramcount*(ramUsage/100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double ramfree = new BigDecimal(ramcount - ramUse).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				ramData.setUsage(ramUsage);
				ramData.setTotal(ramcount);
				ramData.setRemain(ramfree);
				ramData.setUsed(ramUse);
				
			}else if (VIRTUAL_MEMORY_OID.equals(oid)) {  
				
				System.out.println("VIRTUAL_MEMORY----->虚拟内存大小version2的："+(long)totalSize * unit/(1024*1024*1024)+"G   内存使用率为："+(long)usedSize*100/totalSize+"%");
				
				
			} else if(pan_memory.equals(oid)){
				
				SnmpData snmpData = new SnmpData();
				System.out.println("pan--->>> version2的"+(long)totalSize * unit/(1024*1024*1024)+"G   /????为："+(long)usedSize*100/totalSize+"%");
				float diskUsed = (float)usedSize*100/totalSize;//使用率
				float disk = (float)totalSize * unit/(1024*1024*1024);//总大小
				
				//数据转换
				double diskcount = new BigDecimal(disk).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double diskUsage = new BigDecimal(diskUsed).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double diskUse = new BigDecimal((float)diskcount*(diskUsage/100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				double diskfree = new BigDecimal(diskcount - diskUse).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				snmpData.setRemain(diskfree);
				snmpData.setUsed(diskUse);
				snmpData.setUsage(diskUsage);
				snmpData.setTotal(diskcount);
				
				diskList.add(snmpData);
			}
		} 
		
	}  
	} catch(Exception e){  
		e.printStackTrace();  
	}finally{  
		try {  
			if(transport!=null) { 
				transport.close(); 
			}
			if(snmp!=null)  {
				snmp.close(); 
			}
		} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		
		result.setRamData(ramData);
		result.setDiskData(diskList);
		
		return result;
	}  
	
	
	//版本2获取target对象
	public Target getTarget(SnmpConfUser snmpUser){
		CommunityTarget target;
		
		String ipStr = snmpUser.getSnmpIp();
		String comStr = snmpUser.getComWord();
		String addr = "udp:"+ipStr+"/161";
		
		target = new CommunityTarget();  
		target.setCommunity(new OctetString(comStr));  
		target.setRetries(2);  
		target.setAddress(GenericAddress.parse(addr));  
		target.setTimeout(2000);  
		target.setVersion(SnmpConstants.version2c);
		return target;
	}
	

	//linux收集内存
	public SnmpResult collectMemory4Linux(SnmpConfUser snmpUser) {
		String addr = "udp:"+snmpUser.getSnmpIp()+"/161";
		String comm = snmpUser.getComWord();
		SnmpResult result = new SnmpResult();
		try {  
            Address targetAddress = GenericAddress.parse(addr);  
            TransportMapping transport = new DefaultUdpTransportMapping();  
            Snmp snmp = new Snmp(transport);  
            transport.listen();//监听  
              
            CommunityTarget target = new CommunityTarget();  
            target.setCommunity(new OctetString(comm));//设置共同体名  
            target.setAddress(targetAddress);//设置目标Agent地址  
            target.setRetries(2);//重试次数  
            target.setTimeout(5000);//超时设置  
            target.setVersion(1);//版本  
              
            String[] oids = {"1.3.6.1.4.1.2021.4.6.0",  //内存空闲大小
						     "1.3.6.1.4.1.2021.4.5.0",  //内存总大小
						     
						     "1.3.6.1.4.1.2021.11.11.0",  //cpu空闲使用率
						     //"1.3.6.1.4.1.2021.11.10.0",  //cpu
						     
						     "1.3.6.1.4.1.2021.9.1.6.1",  //disk  total
						     "1.3.6.1.4.1.2021.9.1.7.1",  //disk   keyong 
						     //"1.3.6.1.4.1.2021.9.1.8.1",  //disk   使用了的
						     //"1.3.6.1.4.1.2021.9.1.9.1",  //disk   使用率
        					}; 
            
            PDU request = new PDU();  
            request.setType(PDU.GET);//操作类型GET  
            
            for(int i=0;i<oids.length;i++){
            	request.add(new VariableBinding(new OID(oids[i])));
            }
            
            System.out.println("Request UDP:" + request);  
              
            ResponseEvent respEvt = snmp.send(request, target);  
              
            //读取得到的绑定变量
            long[] vals = new long[oids.length];
            if (respEvt != null && respEvt.getResponse()!=null) { 
                Vector <VariableBinding> revBindings = (Vector<VariableBinding>) respEvt.getResponse().getVariableBindings();
                for (int i=0; i<revBindings.size();i++) {
                    VariableBinding vbs = revBindings.elementAt(i);
                    long val = Integer.parseInt(vbs.getVariable().toString());
                    vals[i] = val;//获取其值
                    System.out.println(vbs.getOid()+":"+vbs.getVariable());  
                }  
            }
            
            List<SnmpData> diskDatas = new ArrayList<SnmpData>();//硬盘可能有多块
            SnmpData diskData = new SnmpData();
            SnmpData ramData = new SnmpData();
            
            float ramf = (float)vals[0]/(1024*1024);
            float ramtotal = (float)vals[1]/(1024*1024);
            float cpu = 100- (float)vals[2];//cpu使用率
            float disktotal = (float)vals[3]/(1024*1024);
            float diskf = (float)vals[4]/(1024*1024);
            
            //内存数据转换
            double ramfree = new BigDecimal(ramf).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double ramcount = new BigDecimal(ramtotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double ramUsed = new BigDecimal(ramcount - ramfree).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double ramUsage = new BigDecimal((ramUsed/ramcount)*100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            
            //硬盘数据转换
            double diskcount = new BigDecimal(disktotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double diskfree = new BigDecimal(diskf).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double diskused = new BigDecimal(diskcount - diskfree).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double diskUsage = new BigDecimal((diskused/diskcount)*100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            
            //cpu
            double cpu2 = new BigDecimal(cpu).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            
            //硬盘的数据
            diskData.setTotal(diskcount);
            diskData.setRemain(diskfree);
            diskData.setUsed(diskused);
            diskData.setUsage(diskUsage);//使用率
            //内存的数据
            ramData.setTotal(ramcount);
            ramData.setRemain(ramfree);
            ramData.setUsed(ramUsed);
            ramData.setUsage(ramUsage);//使用率
            
            diskDatas.add(diskData);
            result.setCpu(cpu2);
            result.setRamData(ramData);
            result.setDiskData(diskDatas);
  
        }  
        catch (IOException e){  
            e.printStackTrace();  
        } 
		return result;
	}

	
	

}