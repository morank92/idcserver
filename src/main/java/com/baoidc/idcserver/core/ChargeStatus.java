package com.baoidc.idcserver.core;

public enum ChargeStatus {
	
	CREATED(0),FINISHED(1);
	
	private int statusValue;
	
	ChargeStatus(int statusValue){
		this.statusValue = statusValue;
	}
	
	public int getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(int statusValue) {
		this.statusValue = statusValue;
	}

	public static void main(String[] args) {
		System.out.println(ChargeStatus.CREATED.getStatusValue());
	}

}
