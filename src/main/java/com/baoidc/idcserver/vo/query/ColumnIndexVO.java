package com.baoidc.idcserver.vo.query;

import java.io.Serializable;

public class ColumnIndexVO implements Serializable {
	
	private String url;
	private int[] columnIndex;
	
	
	public int[] getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int[] columnIndex) {
		this.columnIndex = columnIndex;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
