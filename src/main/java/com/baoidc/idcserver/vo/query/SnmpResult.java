package com.baoidc.idcserver.vo.query;

import java.util.List;

public class SnmpResult {
	private String ipStr;
	private double cpu;
	private int ram;
	private int disk;
	
	private List<SnmpData> diskData;//硬盘有可能为多块，每块的数据对象为snmpData
	
	private SnmpData ramData;//内存对象
	
	
	public List<SnmpData> getDiskData() {
		return diskData;
	}
	public void setDiskData(List<SnmpData> diskData) {
		this.diskData = diskData;
	}
	public SnmpData getRamData() {
		return ramData;
	}
	public void setRamData(SnmpData ramData) {
		this.ramData = ramData;
	}
	public String getIpStr() {
		return ipStr;
	}
	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}
	public double getCpu() {
		return cpu;
	}
	public void setCpu(double cpu) {
		this.cpu = cpu;
	}
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}
	public int getDisk() {
		return disk;
	}
	public void setDisk(int disk) {
		this.disk = disk;
	}
	
	
	
}
