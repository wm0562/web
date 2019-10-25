package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.model.CloudPortalUser;

public class CloudPortalUserDto extends CloudPortalUser {
	private static final long serialVersionUID = 1L;
	private String tenantCode;

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}
}
