package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.model.TenantUserRole;

public class TenantUserRoleDto extends TenantUserRole {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String roleName;		// 角色的名称
	
	private String roleGroupId;
	private String roleGroupName;	// 角色所属角色组的名称

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleGroupId() {
		return roleGroupId;
	}

	public void setRoleGroupId(String roleGroupId) {
		this.roleGroupId = roleGroupId;
	}

	public String getRoleGroupName() {
		return roleGroupName;
	}

	public void setRoleGroupName(String roleGroupName) {
		this.roleGroupName = roleGroupName;
	}
}
