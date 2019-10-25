package com.vortex.cloud.ums.dto.tenantgroup;

public class TenantInfoDto {
	private String tenantCode; // 租户Code
	private String tenantId; // 租户id
	private String tenantName; // 租户名称

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
}
