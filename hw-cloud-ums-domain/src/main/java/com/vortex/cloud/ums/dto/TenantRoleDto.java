package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.model.TenantRole;

public class TenantRoleDto extends TenantRole {
	private static final long serialVersionUID = 1L;
	
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
