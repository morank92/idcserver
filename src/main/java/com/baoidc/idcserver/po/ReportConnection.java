package com.baoidc.idcserver.po;

import java.io.Serializable;

public class ReportConnection implements Serializable{
	private String generateTime;
	private String userName;
	private String domainName;
	private String zoneIp;
	private double increaseConn;
	private double concurConn;
	private double tcpIncreaseConn;
	private double tcpConcurConn;
	private double udpIncreaseConn;
	private double udpConcurConn;
	private double icmpIncreaseConn;
	private double icmpConcurConn;
	private double otherIncreaseConn;
	private double otherConcurConn;
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
	public double getIncreaseConn() {
		return increaseConn;
	}
	public void setIncreaseConn(double increaseConn) {
		this.increaseConn = increaseConn;
	}
	public double getConcurConn() {
		return concurConn;
	}
	public void setConcurConn(double concurConn) {
		this.concurConn = concurConn;
	}
	public double getTcpIncreaseConn() {
		return tcpIncreaseConn;
	}
	public void setTcpIncreaseConn(double tcpIncreaseConn) {
		this.tcpIncreaseConn = tcpIncreaseConn;
	}
	public double getTcpConcurConn() {
		return tcpConcurConn;
	}
	public void setTcpConcurConn(double tcpConcurConn) {
		this.tcpConcurConn = tcpConcurConn;
	}
	public double getUdpIncreaseConn() {
		return udpIncreaseConn;
	}
	public void setUdpIncreaseConn(double udpIncreaseConn) {
		this.udpIncreaseConn = udpIncreaseConn;
	}
	public double getUdpConcurConn() {
		return udpConcurConn;
	}
	public void setUdpConcurConn(double udpConcurConn) {
		this.udpConcurConn = udpConcurConn;
	}
	public double getIcmpIncreaseConn() {
		return icmpIncreaseConn;
	}
	public void setIcmpIncreaseConn(double icmpIncreaseConn) {
		this.icmpIncreaseConn = icmpIncreaseConn;
	}
	public double getIcmpConcurConn() {
		return icmpConcurConn;
	}
	public void setIcmpConcurConn(double icmpConcurConn) {
		this.icmpConcurConn = icmpConcurConn;
	}
	public double getOtherIncreaseConn() {
		return otherIncreaseConn;
	}
	public void setOtherIncreaseConn(double otherIncreaseConn) {
		this.otherIncreaseConn = otherIncreaseConn;
	}
	public double getOtherConcurConn() {
		return otherConcurConn;
	}
	public void setOtherConcurConn(double otherConcurConn) {
		this.otherConcurConn = otherConcurConn;
	}

}
