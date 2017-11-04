package com.baoidc.idcserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class CollectSystemInfo {

	private Snmp snmp = null;
	private int m_version;
	private int m_try = 2;
	private int m_timeout = 4000;
	private Boolean m_syn = false;
	private String m_Community = "public";

	public CollectSystemInfo(int version) {
		try {
			this.m_version = version;
			TransportMapping transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			if (version == SnmpConstants.version3) {
				// 设置安全模式
				USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
				SecurityModels.getInstance().addSecurityModel(usm);
			}
			// 开始监听消息
			snmp.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// snmp4j遇到中文直接转成16进制字符串
	public static String getChinese(String octetString) {
		try {
			String[] temps = octetString.split(":");
			byte[] bs = new byte[temps.length];
			for (int i = 0; i < temps.length; i++)
				bs[i] = (byte) Integer.parseInt(temps[i], 16);
			return new String(bs, "GB2312");
		} catch (Exception e) {
			return "";
		}
	}

	private int CPUInfo(TableUtils tableUtils, Target target, JSONArray arrayinfo) {
		// 获取CPU信息
		OID[] columnOids = new OID[] { new OID("1.3.6.1.2.1.25.3.3.1.2") };
		// If not null, all returned rows have an index in a range
		// (lowerBoundIndex, upperBoundIndex]
		// lowerBoundIndex,upperBoundIndex都为null时返回所有的叶子节点。
		// 必须具体到某个OID,,否则返回的结果不会在(lowerBoundIndex, upperBoundIndex)之间
		@SuppressWarnings("unchecked")
		List<TableEvent> eventlist = tableUtils.getTable(target, columnOids, null, null); //
		if (eventlist.size() == 1 && eventlist.get(0).getColumns() == null) {
			JSONObject outjson = new JSONObject();
			outjson.put("status", Integer.toString(eventlist.get(0).getStatus()));
			outjson.put("msg", eventlist.get(0).getErrorMessage());
			arrayinfo.put(outjson);
			return 1;
		} else {
			JSONArray detailarray = new JSONArray();
			int percentage = 0;
			String curuse;
			JSONObject outjson = new JSONObject();
			outjson.put("status", "0");
			for (TableEvent event : eventlist) {
				VariableBinding[] values = event.getColumns();
				if (values != null) {
					curuse = values[0].getVariable().toString();
					detailarray.put(curuse);
					percentage += Integer.parseInt(curuse);
				}
			}
			outjson.put("detail", detailarray);
			outjson.put("name", "CPU");
			outjson.put("percent", Integer.toString(percentage / eventlist.size()));
			arrayinfo.put(outjson);
		}
		return 0;
	}

	private int StorageInfo(TableUtils tableUtils, Target target, JSONArray arrayinfo) {
		// 获取存储信息
		String PHYSICAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.2";// 物理存储
		String VIRTUAL_MEMORY_OID = "1.3.6.1.2.1.25.2.1.3"; // 虚拟存储
		String DISK_OID = "1.3.6.1.2.1.25.2.1.4";

		OID[] columnOids = new OID[] {
				// type 存储单元类型
				new OID("1.3.6.1.2.1.25.2.3.1.2"),
				// descr
				new OID("1.3.6.1.2.1.25.2.3.1.3"),
				// unit 存储单元大小
				new OID("1.3.6.1.2.1.25.2.3.1.4"),
				// size 总存储单元数
				new OID("1.3.6.1.2.1.25.2.3.1.5"),
				// used 使用存储单元数;
				new OID("1.3.6.1.2.1.25.2.3.1.6") };
		// If not null, all returned rows have an index in a range
		// (lowerBoundIndex, upperBoundIndex]
		// lowerBoundIndex,upperBoundIndex都为null时返回所有的叶子节点。
		// 必须具体到某个OID,,否则返回的结果不会在(lowerBoundIndex, upperBoundIndex)之间
		@SuppressWarnings("unchecked")
		List<TableEvent> eventlist = tableUtils.getTable(target, columnOids, null, null);
		if (eventlist.size() == 1 && eventlist.get(0).getColumns() == null) {
			JSONObject outjson = new JSONObject();
			outjson.put("status", Integer.toString(eventlist.get(0).getStatus()));
			outjson.put("msg", eventlist.get(0).getErrorMessage());
			arrayinfo.put(outjson);
			return 1;
		} else {
			for (TableEvent event : eventlist) {
				VariableBinding[] values = event.getColumns();
				if (values == null)
					continue;
				String oid = values[0].getVariable().toString();
				if (PHYSICAL_MEMORY_OID.equals(oid) || VIRTUAL_MEMORY_OID.equals(oid)) {
					JSONObject outjson = new JSONObject();
					JSONArray detailarray = new JSONArray();
					// unit 存储单元大小
					detailarray.put(values[2].getVariable().toString());
					// size 总存储单元数
					detailarray.put(values[3].getVariable().toString());
					// used 使用存储单元数
					detailarray.put(values[4].getVariable().toString());
					int totalSize = Integer.parseInt(values[3].getVariable().toString());
					int usedSize = Integer.parseInt(values[4].getVariable().toString());
					outjson.put("detail", detailarray);
					outjson.put("status", "0");
					outjson.put("name", values[1].getVariable().toString());
					outjson.put("percent", Long.toString((long) usedSize * 100 / totalSize));
					arrayinfo.put(outjson);
				} else if (DISK_OID.equals(oid)) {
					JSONObject outjson = new JSONObject();
					JSONArray detailarray = new JSONArray();
					// unit 存储单元大小
					detailarray.put(values[2].getVariable().toString());
					// size 总存储单元数
					detailarray.put(values[3].getVariable().toString());
					// used 使用存储单元数
					detailarray.put(values[4].getVariable().toString());
					int totalSize = Integer.parseInt(values[3].getVariable().toString());
					int usedSize = Integer.parseInt(values[4].getVariable().toString());
					outjson.put("detail", detailarray);
					outjson.put("status", "0");

					String strtemp = getChinese(values[1].getVariable().toString());

					if (strtemp.equals("")) {
						strtemp=values[0].getVariable().toString();
					} 
					int nPos = strtemp.indexOf(" Serial");
					if (nPos > 0) {
						outjson.put("name", strtemp.substring(0, nPos).trim());
					} else {
						outjson.put("name", strtemp);
					}
					outjson.put("percent", Long.toString((long) usedSize * 100 / totalSize));
					arrayinfo.put(outjson);
				}
			}
		}
		return 0;
	}

	private int IfInfo(TableUtils tableUtils, Target target, JSONArray arrayinfo) {
		// 获取存储信息
//		String[] IP_OIDS =     
//		    {"1.3.6.1.2.1.4.20.1.1", //ipAdEntAddr  
//		     "1.3.6.1.2.1.4.20.1.2", //ipAdEntIfIndex  
//		     "1.3.6.1.2.1.4.20.1.3"};//ipAdEntNetMask  

		OID[] IF_columnOids = new OID[] {
				// Index
				new OID("1.3.6.1.2.1.2.2.1.1"),
				// descr
				new OID("1.3.6.1.2.1.2.2.1.2"),
				// type
				new OID("1.3.6.1.2.1.2.2.1.3"),
				// speed
				new OID("1.3.6.1.2.1.2.2.1.5"),
				// mac
				new OID("1.3.6.1.2.1.2.2.1.6"),
				// operStatus
				new OID("1.3.6.1.2.1.2.2.1.8"),
				
				// inOctets
				new OID("1.3.6.1.2.1.2.2.1.10"),
				// outOctets
				new OID("1.3.6.1.2.1.2.2.1.16")};
		// If not null, all returned rows have an index in a range
		// (lowerBoundIndex, upperBoundIndex]
		// lowerBoundIndex,upperBoundIndex都为null时返回所有的叶子节点。
		// 必须具体到某个OID,,否则返回的结果不会在(lowerBoundIndex, upperBoundIndex)之间
		@SuppressWarnings("unchecked")
		List<TableEvent> eventlist = tableUtils.getTable(target, IF_columnOids, null, null);
		if (eventlist.size() == 1 && eventlist.get(0).getColumns() == null) {
			JSONObject outjson = new JSONObject();
			outjson.put("status", Integer.toString(eventlist.get(0).getStatus()));
			outjson.put("msg", eventlist.get(0).getErrorMessage());
			arrayinfo.put(outjson);
			return 1;
		} else {
			for (TableEvent event : eventlist) {
				VariableBinding[] values = event.getColumns();
				if (values == null)
					continue;
				JSONObject outjson = new JSONObject();
				JSONArray detailarray = new JSONArray();

				//Index
				detailarray.put(values[0].getVariable().toString());
				//type
				detailarray.put(values[2].getVariable().toString());
				//speed
				detailarray.put(values[3].getVariable().toString());
				//mac
				detailarray.put(values[4].getVariable().toString());
				//operStatus
				detailarray.put(values[5].getVariable().toString());
				
				outjson.put("detail", detailarray);
				outjson.put("status", "0");
				
				String strtemp = getChinese(values[1].getVariable().toString());
				strtemp=strtemp.trim();
				if (strtemp.equals("")) {
					strtemp=values[0].getVariable().toString();
				} 
				detailarray.put(strtemp);
				arrayinfo.put(outjson);
			}
		}
		return 0;
	}
	
	private int ServerInfo(TableUtils tableUtils, Target target, JSONArray arrayinfo) {
		// 获取存储信息
		OID[] columnOids = new OID[] {
				new OID("1.3.6.1.4.1.77.1.2.3.1.1")};
		// If not null, all returned rows have an index in a range
		// (lowerBoundIndex, upperBoundIndex]
		// lowerBoundIndex,upperBoundIndex都为null时返回所有的叶子节点。
		// 必须具体到某个OID,,否则返回的结果不会在(lowerBoundIndex, upperBoundIndex)之间
		@SuppressWarnings("unchecked")
		List<TableEvent> eventlist = tableUtils.getTable(target, columnOids, null, null);
		if (eventlist.size() == 1 && eventlist.get(0).getColumns() == null) {
			JSONObject outjson = new JSONObject();
			outjson.put("status", Integer.toString(eventlist.get(0).getStatus()));
			outjson.put("msg", eventlist.get(0).getErrorMessage());
			arrayinfo.put(outjson);
			return 1;
		} else {
			JSONObject outjson = new JSONObject();
			JSONArray detailarray = new JSONArray();
			int nFind=1;
			outjson.put("name", "services");
			outjson.put("status", "0");
			for (TableEvent event : eventlist) {
				VariableBinding[] values = event.getColumns();
				if (values == null)
					continue;
				String strtemp = getChinese(values[0].getVariable().toString());
				strtemp=strtemp.trim();
				if (strtemp.equals("")) {
					strtemp=values[0].getVariable().toString();
				} 
				if (strtemp.equals("SysLogServer")) {
					nFind = 0;
				}
				detailarray.put(strtemp);
			}
			outjson.put("status", Integer.toString(nFind));
			outjson.put("detail", detailarray);
			arrayinfo.put(outjson);
		}
		return 0;
	}
	
	public int collectInfo(String addr, JSONArray arrayinfo) {
		// 生成目标地址对象
		Address targetAddress = GenericAddress.parse(addr);
		Target target = null;
		if (m_version == SnmpConstants.version3) {
			// 添加用户
			snmp.getUSM().addUser(new OctetString("MD5DES"), new UsmUser(new OctetString("MD5DES"), AuthMD5.ID,
					new OctetString("MD5DESUserAuthPassword"), PrivDES.ID, new OctetString("MD5DESUserPrivPassword")));
			target = new UserTarget();
			// 设置安全级别
			((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
			((UserTarget) target).setSecurityName(new OctetString("MD5DES"));
			target.setVersion(SnmpConstants.version3);
		} else {
			target = new CommunityTarget();
			if (m_version == SnmpConstants.version1) {
				target.setVersion(SnmpConstants.version1);
				((CommunityTarget) target).setCommunity(new OctetString(m_Community));
			} else {
				target.setVersion(SnmpConstants.version2c);
				((CommunityTarget) target).setCommunity(new OctetString(m_Community));
			}

		}
		// 目标对象相关设置
		target.setAddress(targetAddress);
		target.setRetries(m_try);
		target.setTimeout(m_timeout);
		if (!m_syn) {
			// GETNEXT or GETBULK
			TableUtils tableUtils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));
			// only for GETBULK, set max-repetitions, default is 10
			// tableUtils.setMaxNumRowsPerPDU(5);
			int nRet = 0;
			nRet = CPUInfo(tableUtils, target, arrayinfo);
			if (nRet != 0) {
				return nRet;
			}

			nRet = StorageInfo(tableUtils, target, arrayinfo);
			if (nRet != 0) {
				return nRet;
			}
			
			nRet = ServerInfo(tableUtils, target, arrayinfo);
			if (nRet != 0) {
				return nRet;
			}
		}
		return 0;
	}

	public void sendMessage(Boolean syn, final Boolean bro, PDU pdu, String addr) throws IOException {
		// 生成目标地址对象
		Address targetAddress = GenericAddress.parse(addr);
		Target target = null;
		if (m_version == SnmpConstants.version3) {
			// 添加用户
			snmp.getUSM().addUser(new OctetString("MD5DES"), new UsmUser(new OctetString("MD5DES"), AuthMD5.ID,
					new OctetString("MD5DESUserAuthPassword"), PrivDES.ID, new OctetString("MD5DESUserPrivPassword")));
			target = new UserTarget();
			// 设置安全级别
			((UserTarget) target).setSecurityLevel(SecurityLevel.AUTH_PRIV);
			((UserTarget) target).setSecurityName(new OctetString("MD5DES"));
			target.setVersion(SnmpConstants.version3);
		} else {
			target = new CommunityTarget();
			if (m_version == SnmpConstants.version1) {
				target.setVersion(SnmpConstants.version1);
				((CommunityTarget) target).setCommunity(new OctetString("public"));
			} else {
				target.setVersion(SnmpConstants.version2c);
				((CommunityTarget) target).setCommunity(new OctetString("public"));
			}

		}
		// 目标对象相关设置
		target.setAddress(targetAddress);
		target.setRetries(5);
		target.setTimeout(1000);

		if (!syn) {
			// 发送报文 并且接受响应
			ResponseEvent response = snmp.send(pdu, target);
			// 处理响应
			System.out.println("Synchronize(同步) message(消息) from(来自) " + response.getPeerAddress() + "\r\n"
					+ "request(发送的请求):" + response.getRequest() + "\r\n" + "response(返回的响应):" + response.getResponse());
			/**
			 * 输出结果： Synchronize(同步) message(消息) from(来自) 192.168.1.233/161
			 * request(发送的请求):GET[requestID=632977521, errorStatus=Success(0),
			 * errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = Null]]
			 * response(返回的响应):RESPONSE[requestID=632977521,
			 * errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 =
			 * WIN-667H6TS3U37]]
			 * 
			 */
		} else {
			// 设置监听对象
			ResponseListener listener = new ResponseListener() {

				public void onResponse(ResponseEvent event) {
					if (bro.equals(false)) {
						((Snmp) event.getSource()).cancel(event.getRequest(), this);
					}
					// 处理响应
					PDU request = event.getRequest();
					PDU response = event.getResponse();
					System.out.println("Asynchronise(异步) message(消息) from(来自) " + event.getPeerAddress() + "\r\n"
							+ "request(发送的请求):" + request + "\r\n" + "response(返回的响应):" + response);
				}

			};
			// 发送报文
			snmp.send(pdu, target, null, listener);
		}
	}

	public String getCommunity() {
		return m_Community;
	}

	public void setCommunity(String community) {
		m_Community = community;
	}

	public int gettry() {
		return m_try;
	}

	public void settry(int m_try) {
		this.m_try = m_try;
	}

	public int gettimeout() {
		return m_timeout;
	}

	public void settimeout(int m_timeout) {
		this.m_timeout = m_timeout;
	}

	public Boolean getsyn() {
		return m_syn;
	}

	public void setsyn(Boolean m_syn) {
		this.m_syn = m_syn;
	}

}