package com.baoidc.idcserver.vo.query;

public class EchartsQueryVO {
	
	private int userId;
	private String ipStr;
	private String protocol;
	private String unit;
	private String stime;//页面传过来的时间
	private String etime;
	
	private String tableName;//查询的表名
	
	private String startGenerate;//页面传过来修正后的时间
	private String endGenerate;//页面传过来修正后的时间
	private String tableType;//页面传过来修正后的时间
	
	//所要查询的字段
	//流量对比
	private String inputFlow;
	private String attFlow;
	private String outFlow;
	
	//连接数
	private String connType;//连接数的类型
	private String unitType;//数据库字段，
	
	//topN
	private String topnUnit;//攻击topN单位类型；
	private String dataType;//攻击topN数据类型；
	private String topnResultType;//攻击topN数据类型；
	
	
	
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getTopnResultType() {
		return topnResultType;
	}
	public void setTopnResultType(String topnResultType) {
		this.topnResultType = topnResultType;
	}
	public String getTopnUnit() {
		return topnUnit;
	}
	public void setTopnUnit(String topnUnit) {
		this.topnUnit = topnUnit;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getConnType() {
		return connType;
	}
	public void setConnType(String connType) {
		this.connType = connType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getInputFlow() {
		return inputFlow;
	}
	public void setInputFlow(String inputFlow) {
		this.inputFlow = inputFlow;
	}
	public String getAttFlow() {
		return attFlow;
	}
	public void setAttFlow(String attFlow) {
		this.attFlow = attFlow;
	}
	public String getOutFlow() {
		return outFlow;
	}
	public void setOutFlow(String outFlow) {
		this.outFlow = outFlow;
	}
	public String getStartGenerate() {
		return startGenerate;
	}
	public void setStartGenerate(String startGenerate) {
		this.startGenerate = startGenerate;
	}
	public String getEndGenerate() {
		return endGenerate;
	}
	public void setEndGenerate(String endGenerate) {
		this.endGenerate = endGenerate;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getIpStr() {
		return ipStr;
	}
	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
	}
	

}
