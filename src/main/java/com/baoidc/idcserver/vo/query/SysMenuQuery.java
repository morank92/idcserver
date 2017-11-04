package com.baoidc.idcserver.vo.query;

public class SysMenuQuery {
	
	private int parentId; //父菜单Id
	private int roleId; //角色Id
	
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
