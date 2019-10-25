package com.vortex.cloud.ums.dto.tenantgroup;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

public class CloudTenantRelationDto extends BackDeleteModelDto {
	private static final long serialVersionUID = 1L;
	private String mainTenantId; // 主租户（集团租户id）
	private String viceTenantId; // 副租户（子公司租户id）

	public String getMainTenantId() {
		return mainTenantId;
	}

	public void setMainTenantId(String mainTenantId) {
		this.mainTenantId = mainTenantId;
	}

	public String getViceTenantId() {
		return viceTenantId;
	}

	public void setViceTenantId(String viceTenantId) {
		this.viceTenantId = viceTenantId;
	}
}
