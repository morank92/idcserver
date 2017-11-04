package com.baoidc.idcserver.po;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserRole implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6912790405185878900L;
	private int id;
	private String roleName;
	private int[] menuIds;
	private List<RoleMenu> roleMenus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<RoleMenu> getRoleMenus() {
		return roleMenus;
	}
	public void setRoleMenus(List<RoleMenu> roleMenus) {
		this.roleMenus = roleMenus;
	}
	public int[] getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(int[] menuIds) {
		this.menuIds = menuIds;
	}
	
}
