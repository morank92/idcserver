package com.baoidc.idcserver.core;

/**
 * 时间类型枚举类
 * @author Administrator
 *
 */
public enum TimeType {
	
	YEAR(0),MONTH(1),DAY(2),HOUR(3),SECOND(4),MINUTE(5);
	
	private int val;
	
	TimeType(int val){
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}
	
	public static void main(String[] args) {
		System.out.println(TimeType.YEAR.getVal());
	}

}
