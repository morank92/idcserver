package com.baoidc.idcserver.po;

import java.io.Serializable;

public class DeviceSourceParam implements Serializable{

	private int id;
	private int sourceType;
	private String sourceParam;
	private int sourceParamStep;
	private int sourceParamMin;
	private int sourceParamMax;
	private double price;
	private int tagId;
	private String tagValue;
	private String tagName;
	private String encrypt;
	private double saleNum;//对应时长上的折扣系数
	
	
	public double getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(double saleNum) {
		this.saleNum = saleNum;
	}
	public int getSourceParamStep() {
		return sourceParamStep;
	}
	public void setSourceParamStep(int sourceParamStep) {
		this.sourceParamStep = sourceParamStep;
	}
	public String getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	public int getSourceParamMin() {
		return sourceParamMin;
	}
	public void setSourceParamMin(int sourceParamMin) {
		this.sourceParamMin = sourceParamMin;
	}
	public int getSourceParamMax() {
		return sourceParamMax;
	}
	public void setSourceParamMax(int sourceParamMax) {
		this.sourceParamMax = sourceParamMax;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourceParam() {
		return sourceParam;
	}
	public void setSourceParam(String sourceParam) {
		this.sourceParam = sourceParam;
	}
	
}
