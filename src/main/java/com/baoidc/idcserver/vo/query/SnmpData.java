package com.baoidc.idcserver.vo.query;

public class SnmpData {
	double usage;//使用率,
	double total;//总大小
	double used;//使用值
	double remain;//剩余值
	
	
	public double getUsed() {
		return used;
	}
	public void setUsed(double used) {
		this.used = used;
	}
	public double getUsage() {
		return usage;
	}
	public void setUsage(double usage) {
		this.usage = usage;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getRemain() {
		return remain;
	}
	public void setRemain(double remain) {
		this.remain = remain;
	}
	
	
	
}
