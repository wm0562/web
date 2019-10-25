package com.vortex.cloud.ums.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

@Entity
@Table(name = CloudTenantRelation.TABLE_NAME)
public class CloudTenantRelation extends BakDeleteModel {
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "CLOUD_TENANT_RELATION";
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
