package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SysMenu implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4650809917772841429L;
	
	private int id;
	private String text;
	private String sref;
	private String icon;
	private String translate;
	private SysMenu parentMenu;
	private int status;//0:启用；1:未启用
	private int seq; //菜单顺序
	private int type;//0:菜单;1:功能点
	private int roleId; //角色Id
	private List<SysMenu> nodes;
	private int parentId;
	private UserRole userRole;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSref() {
		return sref;
	}
	public void setSref(String sref) {
		this.sref = sref;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
	public int getStatus() {
		return status;
	}
	public SysMenu getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(SysMenu parentMenu) {
		this.parentMenu = parentMenu;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public List<SysMenu> getNodes() {
		return nodes;
	}
	public void setNodes(List<SysMenu> nodes) {
		this.nodes = nodes;
	}

}
