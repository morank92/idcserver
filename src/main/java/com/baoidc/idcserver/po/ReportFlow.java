package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.Date;

/**
 * 流量报表
 * @author Administrator
 *
 */
public class ReportFlow implements Serializable{
	private String generateTime;
	private String userName;
	private String domainName;
	private String zoneIp;
	private double totalinpps;
	private double totalinkbps;
	private double totaloutpps;
	private double totaloutkbps;
	private double totalattackpps;
	private double totalattackkbps;
	private double tcpinpps;
	private double tcpoutpps;
	private double tcpattackpps;
	private double tcpinkbps;
	private double tcpoutkbps;
	private double tcpattackkbps;
	private double tcpfraginpps;
	private double tcpfragoutpps;
	private double tcpfragattackpps;
	private double tcpfraginkbps;
	private double tcpfragoutkbps;
	private double tcpfragattackkbps;
	private double udpinpps;
	private double udpoutpps;
	private double udpattackpps;
	private double udpinkbps;
	private double udpoutkbps;
	private double udpattackkbps;
	private double udpfraginpps;
	private double udpfragoutpps;
	private double udpfragattackpps;
	private double udpfraginkbps;
	private double udpfragoutkbps;
	private double udpfragattackkbps;
	private double icmpinpps;
	private double icmpoutpps;
	private double icmpattackpps;
	private double icmpinkbps;
	private double icmpoutkbps;
	private double icmpattackkbps;
	private double httpinpps;
	private double httpoutpps;
	private double httpattackpps;
	private double httpinkbps;
	private double httpoutkbps;
	private double httpattackkbps;
	private double httpinqps;
	private double httpoutqps;
	private double httpattackqps;
	private double httpsinpps;
	private double httpsoutpps;
	private double httpsattackpps;
	private double httpsinkbps;
	private double httpsoutkbps;
	private double httpsattackkbps;
	private double dnsqinpps;
	private double dnsqoutpps;
	private double dnsqattackpps;
	private double dnsqinkbps;
	private double dnsqoutkbps;
	private double dnsqattackkbps;
	private double dnsrinpps;
	private double dnsroutpps;
	private double dnsrattackpps;
	private double dnsrinkbps;
	private double dnsroutkbps;
	private double dnsrattackkbps;
	private double sipinpps;
	private double sipoutpps;
	private double sipattackpps;
	private double sipinkbps;
	private double sipoutkbps;
	private double sipattackkbps;
	
	private double syninpps;
	private double synoutpps;
	private double synattackpps;
	private double synackinpps;
	private double synackoutpps;
	private double synackattackpps;
	private double ackinpps;
	private double ackoutpps;
	private double ackattackpps;
	private double finrstinpps;
	private double finrstoutpps;
	private double finrstattackpps;
	
	//从数据中获取的任意数据，需要在前端页面展示的字段
	private double inFlow;
	private double outFlow;
	private double attFlow;
	
	public double getInFlow() {
		return inFlow;
	}
	public void setInFlow(double inFlow) {
		this.inFlow = inFlow;
	}
	public double getOutFlow() {
		return outFlow;
	}
	public void setOutFlow(double outFlow) {
		this.outFlow = outFlow;
	}
	public double getAttFlow() {
		return attFlow;
	}
	public void setAttFlow(double attFlow) {
		this.attFlow = attFlow;
	}
	public double getSyninpps() {
		return syninpps;
	}
	public void setSyninpps(double syninpps) {
		this.syninpps = syninpps;
	}
	public double getSynoutpps() {
		return synoutpps;
	}
	public void setSynoutpps(double synoutpps) {
		this.synoutpps = synoutpps;
	}
	public double getSynattackpps() {
		return synattackpps;
	}
	public void setSynattackpps(double synattackpps) {
		this.synattackpps = synattackpps;
	}
	public double getSynackinpps() {
		return synackinpps;
	}
	public void setSynackinpps(double synackinpps) {
		this.synackinpps = synackinpps;
	}
	public double getSynackoutpps() {
		return synackoutpps;
	}
	public void setSynackoutpps(double synackoutpps) {
		this.synackoutpps = synackoutpps;
	}
	public double getSynackattackpps() {
		return synackattackpps;
	}
	public void setSynackattackpps(double synackattackpps) {
		this.synackattackpps = synackattackpps;
	}
	public double getAckinpps() {
		return ackinpps;
	}
	public void setAckinpps(double ackinpps) {
		this.ackinpps = ackinpps;
	}
	public double getAckoutpps() {
		return ackoutpps;
	}
	public void setAckoutpps(double ackoutpps) {
		this.ackoutpps = ackoutpps;
	}
	public double getAckattackpps() {
		return ackattackpps;
	}
	public void setAckattackpps(double ackattackpps) {
		this.ackattackpps = ackattackpps;
	}
	public double getFinrstinpps() {
		return finrstinpps;
	}
	public void setFinrstinpps(double finrstinpps) {
		this.finrstinpps = finrstinpps;
	}
	public double getFinrstoutpps() {
		return finrstoutpps;
	}
	public void setFinrstoutpps(double finrstoutpps) {
		this.finrstoutpps = finrstoutpps;
	}
	public double getFinrstattackpps() {
		return finrstattackpps;
	}
	public void setFinrstattackpps(double finrstattackpps) {
		this.finrstattackpps = finrstattackpps;
	}
	public String getGenerateTime() {
		return generateTime;
	}
	public void setGenerateTime(String generateTime) {
		this.generateTime = generateTime.substring(0, 19);
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getZoneIp() {
		return zoneIp;
	}
	public void setZoneIp(String zoneIp) {
		this.zoneIp = zoneIp;
	}
	public double getTotalinpps() {
		return totalinpps;
	}
	public void setTotalinpps(double totalinpps) {
		this.totalinpps = totalinpps;
	}
	public double getTotalinkbps() {
		return totalinkbps;
	}
	public void setTotalinkbps(double totalinkbps) {
		this.totalinkbps = totalinkbps;
	}
	public double getTotaloutpps() {
		return totaloutpps;
	}
	public void setTotaloutpps(double totaloutpps) {
		this.totaloutpps = totaloutpps;
	}
	public double getTotaloutkbps() {
		return totaloutkbps;
	}
	public void setTotaloutkbps(double totaloutkbps) {
		this.totaloutkbps = totaloutkbps;
	}
	public double getTotalattackpps() {
		return totalattackpps;
	}
	public void setTotalattackpps(double totalattackpps) {
		this.totalattackpps = totalattackpps;
	}
	public double getTotalattackkbps() {
		return totalattackkbps;
	}
	public void setTotalattackkbps(double totalattackkbps) {
		this.totalattackkbps = totalattackkbps;
	}
	public double getTcpinpps() {
		return tcpinpps;
	}
	public void setTcpinpps(double tcpinpps) {
		this.tcpinpps = tcpinpps;
	}
	public double getTcpoutpps() {
		return tcpoutpps;
	}
	public void setTcpoutpps(double tcpoutpps) {
		this.tcpoutpps = tcpoutpps;
	}
	public double getTcpattackpps() {
		return tcpattackpps;
	}
	public void setTcpattackpps(double tcpattackpps) {
		this.tcpattackpps = tcpattackpps;
	}
	public double getTcpinkbps() {
		return tcpinkbps;
	}
	public void setTcpinkbps(double tcpinkbps) {
		this.tcpinkbps = tcpinkbps;
	}
	public double getTcpoutkbps() {
		return tcpoutkbps;
	}
	public void setTcpoutkbps(double tcpoutkbps) {
		this.tcpoutkbps = tcpoutkbps;
	}
	public double getTcpattackkbps() {
		return tcpattackkbps;
	}
	public void setTcpattackkbps(double tcpattackkbps) {
		this.tcpattackkbps = tcpattackkbps;
	}
	public double getTcpfraginpps() {
		return tcpfraginpps;
	}
	public void setTcpfraginpps(double tcpfraginpps) {
		this.tcpfraginpps = tcpfraginpps;
	}
	public double getTcpfragoutpps() {
		return tcpfragoutpps;
	}
	public void setTcpfragoutpps(double tcpfragoutpps) {
		this.tcpfragoutpps = tcpfragoutpps;
	}
	public double getTcpfragattackpps() {
		return tcpfragattackpps;
	}
	public void setTcpfragattackpps(double tcpfragattackpps) {
		this.tcpfragattackpps = tcpfragattackpps;
	}
	public double getTcpfraginkbps() {
		return tcpfraginkbps;
	}
	public void setTcpfraginkbps(double tcpfraginkbps) {
		this.tcpfraginkbps = tcpfraginkbps;
	}
	public double getTcpfragoutkbps() {
		return tcpfragoutkbps;
	}
	public void setTcpfragoutkbps(double tcpfragoutkbps) {
		this.tcpfragoutkbps = tcpfragoutkbps;
	}
	public double getTcpfragattackkbps() {
		return tcpfragattackkbps;
	}
	public void setTcpfragattackkbps(double tcpfragattackkbps) {
		this.tcpfragattackkbps = tcpfragattackkbps;
	}
	public double getUdpinpps() {
		return udpinpps;
	}
	public void setUdpinpps(double udpinpps) {
		this.udpinpps = udpinpps;
	}
	public double getUdpoutpps() {
		return udpoutpps;
	}
	public void setUdpoutpps(double udpoutpps) {
		this.udpoutpps = udpoutpps;
	}
	public double getUdpattackpps() {
		return udpattackpps;
	}
	public void setUdpattackpps(double udpattackpps) {
		this.udpattackpps = udpattackpps;
	}
	public double getUdpinkbps() {
		return udpinkbps;
	}
	public void setUdpinkbps(double udpinkbps) {
		this.udpinkbps = udpinkbps;
	}
	public double getUdpoutkbps() {
		return udpoutkbps;
	}
	public void setUdpoutkbps(double udpoutkbps) {
		this.udpoutkbps = udpoutkbps;
	}
	public double getUdpattackkbps() {
		return udpattackkbps;
	}
	public void setUdpattackkbps(double udpattackkbps) {
		this.udpattackkbps = udpattackkbps;
	}
	public double getUdpfraginpps() {
		return udpfraginpps;
	}
	public void setUdpfraginpps(double udpfraginpps) {
		this.udpfraginpps = udpfraginpps;
	}
	public double getUdpfragoutpps() {
		return udpfragoutpps;
	}
	public void setUdpfragoutpps(double udpfragoutpps) {
		this.udpfragoutpps = udpfragoutpps;
	}
	public double getUdpfragattackpps() {
		return udpfragattackpps;
	}
	public void setUdpfragattackpps(double udpfragattackpps) {
		this.udpfragattackpps = udpfragattackpps;
	}
	public double getUdpfraginkbps() {
		return udpfraginkbps;
	}
	public void setUdpfraginkbps(double udpfraginkbps) {
		this.udpfraginkbps = udpfraginkbps;
	}
	public double getUdpfragoutkbps() {
		return udpfragoutkbps;
	}
	public void setUdpfragoutkbps(double udpfragoutkbps) {
		this.udpfragoutkbps = udpfragoutkbps;
	}
	public double getUdpfragattackkbps() {
		return udpfragattackkbps;
	}
	public void setUdpfragattackkbps(double udpfragattackkbps) {
		this.udpfragattackkbps = udpfragattackkbps;
	}
	public double getIcmpinpps() {
		return icmpinpps;
	}
	public void setIcmpinpps(double icmpinpps) {
		this.icmpinpps = icmpinpps;
	}
	public double getIcmpoutpps() {
		return icmpoutpps;
	}
	public void setIcmpoutpps(double icmpoutpps) {
		this.icmpoutpps = icmpoutpps;
	}
	public double getIcmpattackpps() {
		return icmpattackpps;
	}
	public void setIcmpattackpps(double icmpattackpps) {
		this.icmpattackpps = icmpattackpps;
	}
	public double getIcmpinkbps() {
		return icmpinkbps;
	}
	public void setIcmpinkbps(double icmpinkbps) {
		this.icmpinkbps = icmpinkbps;
	}
	public double getIcmpoutkbps() {
		return icmpoutkbps;
	}
	public void setIcmpoutkbps(double icmpoutkbps) {
		this.icmpoutkbps = icmpoutkbps;
	}
	public double getIcmpattackkbps() {
		return icmpattackkbps;
	}
	public void setIcmpattackkbps(double icmpattackkbps) {
		this.icmpattackkbps = icmpattackkbps;
	}
	public double getHttpinpps() {
		return httpinpps;
	}
	public void setHttpinpps(double httpinpps) {
		this.httpinpps = httpinpps;
	}
	public double getHttpoutpps() {
		return httpoutpps;
	}
	public void setHttpoutpps(double httpoutpps) {
		this.httpoutpps = httpoutpps;
	}
	public double getHttpattackpps() {
		return httpattackpps;
	}
	public void setHttpattackpps(double httpattackpps) {
		this.httpattackpps = httpattackpps;
	}
	public double getHttpinkbps() {
		return httpinkbps;
	}
	public void setHttpinkbps(double httpinkbps) {
		this.httpinkbps = httpinkbps;
	}
	public double getHttpoutkbps() {
		return httpoutkbps;
	}
	public void setHttpoutkbps(double httpoutkbps) {
		this.httpoutkbps = httpoutkbps;
	}
	public double getHttpattackkbps() {
		return httpattackkbps;
	}
	public void setHttpattackkbps(double httpattackkbps) {
		this.httpattackkbps = httpattackkbps;
	}
	public double getHttpinqps() {
		return httpinqps;
	}
	public void setHttpinqps(double httpinqps) {
		this.httpinqps = httpinqps;
	}
	public double getHttpoutqps() {
		return httpoutqps;
	}
	public void setHttpoutqps(double httpoutqps) {
		this.httpoutqps = httpoutqps;
	}
	public double getHttpattackqps() {
		return httpattackqps;
	}
	public void setHttpattackqps(double httpattackqps) {
		this.httpattackqps = httpattackqps;
	}
	public double getHttpsinpps() {
		return httpsinpps;
	}
	public void setHttpsinpps(double httpsinpps) {
		this.httpsinpps = httpsinpps;
	}
	public double getHttpsoutpps() {
		return httpsoutpps;
	}
	public void setHttpsoutpps(double httpsoutpps) {
		this.httpsoutpps = httpsoutpps;
	}
	public double getHttpsattackpps() {
		return httpsattackpps;
	}
	public void setHttpsattackpps(double httpsattackpps) {
		this.httpsattackpps = httpsattackpps;
	}
	public double getHttpsinkbps() {
		return httpsinkbps;
	}
	public void setHttpsinkbps(double httpsinkbps) {
		this.httpsinkbps = httpsinkbps;
	}
	public double getHttpsoutkbps() {
		return httpsoutkbps;
	}
	public void setHttpsoutkbps(double httpsoutkbps) {
		this.httpsoutkbps = httpsoutkbps;
	}
	public double getHttpsattackkbps() {
		return httpsattackkbps;
	}
	public void setHttpsattackkbps(double httpsattackkbps) {
		this.httpsattackkbps = httpsattackkbps;
	}
	public double getDnsqinpps() {
		return dnsqinpps;
	}
	public void setDnsqinpps(double dnsqinpps) {
		this.dnsqinpps = dnsqinpps;
	}
	public double getDnsqoutpps() {
		return dnsqoutpps;
	}
	public void setDnsqoutpps(double dnsqoutpps) {
		this.dnsqoutpps = dnsqoutpps;
	}
	public double getDnsqattackpps() {
		return dnsqattackpps;
	}
	public void setDnsqattackpps(double dnsqattackpps) {
		this.dnsqattackpps = dnsqattackpps;
	}
	public double getDnsqinkbps() {
		return dnsqinkbps;
	}
	public void setDnsqinkbps(double dnsqinkbps) {
		this.dnsqinkbps = dnsqinkbps;
	}
	public double getDnsqoutkbps() {
		return dnsqoutkbps;
	}
	public void setDnsqoutkbps(double dnsqoutkbps) {
		this.dnsqoutkbps = dnsqoutkbps;
	}
	public double getDnsqattackkbps() {
		return dnsqattackkbps;
	}
	public void setDnsqattackkbps(double dnsqattackkbps) {
		this.dnsqattackkbps = dnsqattackkbps;
	}
	public double getDnsrinpps() {
		return dnsrinpps;
	}
	public void setDnsrinpps(double dnsrinpps) {
		this.dnsrinpps = dnsrinpps;
	}
	public double getDnsroutpps() {
		return dnsroutpps;
	}
	public void setDnsroutpps(double dnsroutpps) {
		this.dnsroutpps = dnsroutpps;
	}
	public double getDnsrattackpps() {
		return dnsrattackpps;
	}
	public void setDnsrattackpps(double dnsrattackpps) {
		this.dnsrattackpps = dnsrattackpps;
	}
	public double getDnsrinkbps() {
		return dnsrinkbps;
	}
	public void setDnsrinkbps(double dnsrinkbps) {
		this.dnsrinkbps = dnsrinkbps;
	}
	public double getDnsroutkbps() {
		return dnsroutkbps;
	}
	public void setDnsroutkbps(double dnsroutkbps) {
		this.dnsroutkbps = dnsroutkbps;
	}
	public double getDnsrattackkbps() {
		return dnsrattackkbps;
	}
	public void setDnsrattackkbps(double dnsrattackkbps) {
		this.dnsrattackkbps = dnsrattackkbps;
	}
	public double getSipinpps() {
		return sipinpps;
	}
	public void setSipinpps(double sipinpps) {
		this.sipinpps = sipinpps;
	}
	public double getSipoutpps() {
		return sipoutpps;
	}
	public void setSipoutpps(double sipoutpps) {
		this.sipoutpps = sipoutpps;
	}
	public double getSipattackpps() {
		return sipattackpps;
	}
	public void setSipattackpps(double sipattackpps) {
		this.sipattackpps = sipattackpps;
	}
	public double getSipinkbps() {
		return sipinkbps;
	}
	public void setSipinkbps(double sipinkbps) {
		this.sipinkbps = sipinkbps;
	}
	public double getSipoutkbps() {
		return sipoutkbps;
	}
	public void setSipoutkbps(double sipoutkbps) {
		this.sipoutkbps = sipoutkbps;
	}
	public double getSipattackkbps() {
		return sipattackkbps;
	}
	public void setSipattackkbps(double sipattackkbps) {
		this.sipattackkbps = sipattackkbps;
	}

}
