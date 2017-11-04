package com.baoidc.idcserver.core;

/**
 * 工单状态枚举类
 * @author Administrator
 *
 */
public enum WorkOrderStatus {
	
	NEW(0),DEALED(1);
	
	private int statusVal;
	
	WorkOrderStatus(int statusVal){
		this.statusVal = statusVal;
	}

	public int getStatusVal() {
		return statusVal;
	}

	public void setStatusVal(int statusVal) {
		this.statusVal = statusVal;
	}

}
