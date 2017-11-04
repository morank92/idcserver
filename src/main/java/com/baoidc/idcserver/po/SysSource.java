package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SysSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5371456323005396304L;
	
	private int id;
	private String sourceName;
	private String sourceIdentity;
	private String sourceOpts;
	private List<Operation> optList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getSourceIdentity() {
		return sourceIdentity;
	}
	public void setSourceIdentity(String sourceIdentity) {
		this.sourceIdentity = sourceIdentity;
	}
	public String getSourceOpts() {
		return sourceOpts;
	}
	public void setSourceOpts(String sourceOpts) {
		this.sourceOpts = sourceOpts;
	}
	public List<Operation> getOptList() {
		return optList;
	}
	public void setOptList(List<Operation> optList) {
		this.optList = optList;
	}

}
