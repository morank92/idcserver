package com.baoidc.idcserver.service.impl;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.Snmp2cQuery;
import com.baoidc.idcserver.dao.ISnmpDAO;
import com.baoidc.idcserver.po.SnmpConfUser;
import com.baoidc.idcserver.po.ipmiUser;
import com.baoidc.idcserver.service.ISnmpService;
import com.baoidc.idcserver.vo.query.SnmpResult;
import com.veraxsystems.vxipmi.api.async.ConnectionHandle;
import com.veraxsystems.vxipmi.api.sync.IpmiConnector;
import com.veraxsystems.vxipmi.coding.commands.IpmiVersion;
import com.veraxsystems.vxipmi.coding.commands.PrivilegeLevel;
import com.veraxsystems.vxipmi.coding.commands.chassis.ChassisControl;
import com.veraxsystems.vxipmi.coding.commands.chassis.ChassisControlResponseData;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatus;
import com.veraxsystems.vxipmi.coding.commands.chassis.GetChassisStatusResponseData;
import com.veraxsystems.vxipmi.coding.commands.chassis.PowerCommand;
import com.veraxsystems.vxipmi.coding.commands.session.SetSessionPrivilegeLevel;
import com.veraxsystems.vxipmi.coding.protocol.AuthenticationType;
import com.veraxsystems.vxipmi.coding.security.CipherSuite;
@Service
public class SnmpServiceImpl implements ISnmpService {
	
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ISnmpDAO snmpDao;
	
	@Autowired
	private Snmp2cQuery snmp2cQuery;
	
	
	//snmp查询
	public SnmpResult getSnmpResultBySnmpConfUser(SnmpConfUser snmpconf) {
		
		SnmpResult result = new SnmpResult();
		//查询当前用户实例下的配置snmp
		SnmpConfUser snmpUser = snmpDao.getSnmpConf4User(snmpconf);
		if(snmpUser != null){
			int version = snmpUser.getVersion();
			if(version == SnmpConstants.version2c){
				if("Linux".equals(snmpUser.getSystemVersion())){//为linux操作系统
					
					result = snmp2cQuery.collectMemory4Linux(snmpUser);
					
				}else{
					result = snmp2cQuery.collectMemory(snmpUser);
					double cpu = snmp2cQuery.collectCPU(snmpUser);//使用率、
					result.setCpu(cpu);
				}
				
			}else if(version == SnmpConstants.version3){//为版本3
				Snmp snmp;
				
				String secName = snmpUser.getSecName();
				String pword = snmpUser.getPassWord();
				String auth = snmpUser.getAuthPro();//加密方式
				String ip = snmpUser.getSnmpIp()+"/161";//ip
				
				try {
					snmp = new Snmp(new DefaultUdpTransportMapping());
					USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);  
			        SecurityModels.getInstance().addSecurityModel(usm);  
			        snmp.listen();
			        
			     // Add User  
			        UsmUser user = new UsmUser(  
			                new OctetString(secName),  
			                AuthMD5.ID, new OctetString(pword),  
			                PrivDES.ID, new OctetString(pword));  
			        //If the specified SNMP engine id is specified, this user can only be used with the specified engine ID  
			        //So if it's not correct, will get an error that can't find a user from the user table.  
			        //snmp.getUSM().addUser(new OctetString("nmsAdmin"), new OctetString("0002651100"), user);  
			        snmp.getUSM().addUser(new OctetString(secName), user);  
			          
			        UserTarget target = new UserTarget();  
			        target.setVersion(SnmpConstants.version3);  
			        target.setAddress(new UdpAddress(ip));  
			        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);  
			        target.setSecurityName(new OctetString(secName));  
			        target.setTimeout(3000);    //3s  
			        target.setRetries(0);  
			                  
			        OctetString contextEngineId = new OctetString("0002651100[02]");  
			        sendRequest(snmp, createGetPdu(contextEngineId), target);  
			        snmpWalk(snmp, target, contextEngineId);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		        
			}else{
				System.out.println("snmp版本未匹配到！");
			}
		}
		return result;
	} 
	
	//版本3的创建pdu
	private static PDU createGetPdu(OctetString contextEngineId) {  
        ScopedPDU pdu = new ScopedPDU();  
        pdu.setType(PDU.GET);  
        pdu.setContextEngineID(contextEngineId);    //if not set, will be SNMP engine id  
        //pdu.setContextName(contextName);  //must be same as SNMP agent  
          
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.3.0"))); //sysUpTime  
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5.0"))); //sysName  
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5")));   //expect an no_such_instance error  
        return pdu;  
    }  
      
	//版本3的发送请求
    private static void sendRequest(Snmp snmp, PDU pdu, UserTarget target)  
    throws IOException {  
        ResponseEvent responseEvent = snmp.send(pdu, target);  
        PDU response = responseEvent.getResponse();  
          
        if (response == null) {  
            System.out.println("TimeOut...");  
        } else {  
            if (response.getErrorStatus() == PDU.noError) {  
                Vector<? extends VariableBinding> vbs = response.getVariableBindings();  
                for (VariableBinding vb : vbs) {  
                    System.out.println(vb + " ," + vb.getVariable().getSyntaxString());  
                }  
            } else {  
                System.out.println("Error:" + response.getErrorStatusText());  
            }  
        }  
    }  
      
  //版本3的运行获取数据
    private static void snmpWalk(Snmp snmp, UserTarget target, OctetString contextEngineId) {  
        TableUtils utils = new TableUtils(snmp,  
                new MyDefaultPDUFactory(PDU.GETNEXT, //GETNEXT or GETBULK)  
                                        contextEngineId));  
        utils.setMaxNumRowsPerPDU(5);   //only for GETBULK, set max-repetitions, default is 10  
        OID[] columnOids = new OID[] {  
                new OID("1.3.6.1.2.1.1.9.1.2"), //sysORID  
                new OID("1.3.6.1.2.1.1.9.1.3")  //sysORDescr  
        };  
        // If not null, all returned rows have an index in a range (lowerBoundIndex, upperBoundIndex]  
        List<TableEvent> l = utils.getTable(target, columnOids, new OID("3"), new OID("10"));  
        for (TableEvent e : l) {  
            System.out.println(e);  
        }  
    }  
      
    //版本3的默认pdu
    private static class MyDefaultPDUFactory extends DefaultPDUFactory {  
        private OctetString contextEngineId = null;  
          
        public MyDefaultPDUFactory(int pduType, OctetString contextEngineId) {  
            super(pduType);  
            this.contextEngineId = contextEngineId;  
        }  
  
        @Override  
        public PDU createPDU(Target target) {  
            PDU pdu = super.createPDU(target);  
            if (target.getVersion() == SnmpConstants.version3) {  
                ((ScopedPDU)pdu).setContextEngineID(contextEngineId);  
            }  
            return pdu;  
        }         
    }

    //创建snmp配置--用户
    public SnmpConfUser setSnmpMessage(SnmpConfUser snmpConf) {
    	int id = snmpDao.setSnmpMessage(snmpConf);
    	snmpConf.setId(id);
		return snmpConf;
	}

    //获取 snmp配置
	public SnmpConfUser getSnmpConf(SnmpConfUser snmpConf) {
		return snmpDao.getSnmpConf4User(snmpConf);
	}

	public void updateSnmpConf(SnmpConfUser snmpConf) {
		snmpDao.updateSnmpConf(snmpConf);
		
	}

	@Override
	public void toConfirmIpmi(ipmiUser ipuser) {
		// TODO Auto-generated method stub
		
	}

	//开机
	public boolean toStartByIpmi(ipmiUser ipuser) throws Exception {
		
		IpmiConnector connector = null;
		boolean flag = false;
		
		String ipStr = ipuser.getIpStr();
		String userName = ipuser.getUserName();
		String password = ipuser.getPassword();

		connector = new IpmiConnector(3000);
		System.out.println("Connector created");

		ConnectionHandle handle = connector.createConnection(InetAddress.getByName(ipStr));//连接ip
		System.out.println("Connection created");

		CipherSuite cs = connector.getAvailableCipherSuites(handle).get(2);//重新尝试次数
		System.out.println("Cipher suite picked");

		connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);//级别
		System.out.println("Channel authentication capabilities receivied");

		connector.openSession(handle, userName, password, null);//用户名密码
		System.out.println("Session open");

		// Send some message and read the response
		GetChassisStatusResponseData rd = (GetChassisStatusResponseData) connector.sendMessage(handle,
		        new GetChassisStatus(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus));

		System.out.println("Received answer");
		System.out.println("System power state is " + (rd.isPowerOn() ? "up" : "down"));

		// Set session privilege level to administrator, as ChassisControl command requires this level
		connector.sendMessage(handle, new SetSessionPrivilegeLevel(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
		        PrivilegeLevel.Administrator));

		ChassisControl chassisControl = null;
		
		//开关机选择
		if (!rd.isPowerOn()) {//目前处于关机状态，可以开机
		    chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus, PowerCommand.PowerUp);
		    flag = true;
		} else {//已经是开机状态
			flag = false;
		    /*chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
		            PowerCommand.PowerDown);*/
		}

		ChassisControlResponseData data = (ChassisControlResponseData) connector.sendMessage(handle, chassisControl);

		// Close the session
		connector.closeSession(handle);
        System.out.println("Session closed");

        // Close connection manager and release the listener port.
        connector.tearDown();
        System.out.println("Connection manager closed");
        return flag;
	}

	@Override
	public boolean toEndByIpmi(ipmiUser ipuser)throws Exception {
		IpmiConnector connector = null;
		boolean flag = false;
		
		String ipStr = ipuser.getIpStr();
		String userName = ipuser.getUserName();
		String password = ipuser.getPassword();

		connector = new IpmiConnector(6000);
		System.out.println("Connector created");

		ConnectionHandle handle = connector.createConnection(InetAddress.getByName(ipStr));//连接ip
		System.out.println("Connection created");

		CipherSuite cs = connector.getAvailableCipherSuites(handle).get(3);
		System.out.println("Cipher suite picked");

		connector.getChannelAuthenticationCapabilities(handle, cs, PrivilegeLevel.Administrator);//级别
		System.out.println("Channel authentication capabilities receivied");

		connector.openSession(handle, userName, password, null);//用户名密码
		System.out.println("Session open");

		// Send some message and read the response
		GetChassisStatusResponseData rd = (GetChassisStatusResponseData) connector.sendMessage(handle,
		        new GetChassisStatus(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus));

		System.out.println("Received answer");
		System.out.println("System power state is " + (rd.isPowerOn() ? "up" : "down"));

		// Set session privilege level to administrator, as ChassisControl command requires this level
		connector.sendMessage(handle, new SetSessionPrivilegeLevel(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
		        PrivilegeLevel.Administrator));

		ChassisControl chassisControl = null;
		
		//开关机选择
		if (!rd.isPowerOn()) {//目前处于关机状态
		    //chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus, PowerCommand.PowerUp);
		    flag = false;
		} else {//目前处于开机状态，可以关机
		    chassisControl = new ChassisControl(IpmiVersion.V20, cs, AuthenticationType.RMCPPlus,
		            PowerCommand.PowerDown);
		    flag = true;
		}

		ChassisControlResponseData data = (ChassisControlResponseData) connector.sendMessage(handle, chassisControl);

		// Close the session
		connector.closeSession(handle);
        System.out.println("Session closed");

        // Close connection manager and release the listener port.
        connector.tearDown();
        System.out.println("Connection manager closed");
        return flag;
	}

}
