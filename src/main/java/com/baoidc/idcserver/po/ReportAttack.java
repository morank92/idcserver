package com.baoidc.idcserver.po;

import java.io.Serializable;

import com.baoidc.idcserver.core.DataConvertor;


public class ReportAttack implements Serializable{
	
	private String reportId;
	private String generateTime;
	private String userName;
	private String domainName;
	private String zoneIp;
	private String attackStartTime;
	private String attackEndTime;
	private double attackDuration;//持续时长
	private String attackType;
	private double attackmvpps;
	private double attackmvkbps;
	private double attackpvpps;
	private double attackpvkbps;
	private double dropPackets;
	private double attacktotalpps;
	private double attacktotalkbps;
	private double attacktotalkb;//总比特数
	private double currConn;
	private double newConn;
	private double attackTopn;//构建topn的y轴值
	
	
	public double getAttackTopn() {
		return attackTopn;
	}
	public void setAttackTopn(double attackTopn) {
		this.attackTopn = attackTopn;
	}
	public double getAttackDuration() {
		return attackDuration;
	}
	public void setAttackDuration(double attackDuration) {
		this.attackDuration = attackDuration;
	}
	public double getAttacktotalkb() {
		return attacktotalkb;
	}
	public void setAttacktotalkb(double attacktotalkb) {
		this.attacktotalkb = attacktotalkb;
	}
	public double getAttacktotalpps() {
		return attacktotalpps;
	}
	public void setAttacktotalpps(double attacktotalpps) {
		this.attacktotalpps = attacktotalpps;
	}
	public String getGenerateTime() {
		return generateTime;
	}
	public void setGenerateTime(String generateTime) {
		this.generateTime = generateTime;
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
	public String getAttackStartTime() {
		return attackStartTime;
	}
	public void setAttackStartTime(String attackStartTime) {
		this.attackStartTime = attackStartTime;
	}
	public String getAttackEndTime() {
		return attackEndTime;
	}
	public void setAttackEndTime(String attackEndTime) {
		this.attackEndTime = attackEndTime;
	}
	public String getAttackType() {
		return attackType;
	}
	public void setAttackType(String attackType) {//需要转化为可视的类型
		String myType = DataConvertor.numType2MyType(attackType);
		this.attackType = myType;
	}
	public double getAttackmvpps() {
		return attackmvpps;
	}
	public void setAttackmvpps(double attackmvpps) {
		this.attackmvpps = attackmvpps;
	}
	public double getAttackmvkbps() {
		return attackmvkbps;
	}
	public void setAttackmvkbps(double attackmvkbps) {
		this.attackmvkbps = attackmvkbps;
	}
	public double getAttackpvpps() {
		return attackpvpps;
	}
	public void setAttackpvpps(double attackpvpps) {
		this.attackpvpps = attackpvpps;
	}
	public double getAttackpvkbps() {
		return attackpvkbps;
	}
	public void setAttackpvkbps(double attackpvkbps) {
		this.attackpvkbps = attackpvkbps;
	}
	public double getDropPackets() {
		return dropPackets;
	}
	public void setDropPackets(double dropPackets) {
		this.dropPackets = dropPackets;
	}
	public double getAttacktotalkbps() {
		return attacktotalkbps;
	}
	public void setAttacktotalkbps(double attacktotalkbps) {
		this.attacktotalkbps = attacktotalkbps;
	}
	public double getCurrConn() {
		return currConn;
	}
	public void setCurrConn(double currConn) {
		this.currConn = currConn;
	}
	public double getNewConn() {
		return newConn;
	}
	public void setNewConn(double newConn) {
		this.newConn = newConn;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
}
