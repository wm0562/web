package com.vortex.cloud.ums.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * 租户资源表
 * 
 * @author XY
 *
 */
@Entity
@Table(name = CloudTenantResource.TABLE_NAME)
public class CloudTenantResource extends BakDeleteModel {
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "cloud_tenant_resource";
	private String tenantId; // 租户id
	private String tenantCode; // 租户code
	private String roleCode; // 角色code
	private String roleId; // 角色id
	private String resourceTypeCode; // 资源类型code
	private String resourceId; // 资源id
	private String resourceCode; // 资源code
	private String remark; // 备注

	@Column(name = "resourceCode", length = 32)
	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	@Column(name = "tenantCode", length = 32)
	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	@Column(name = "roleCode", length = 32)
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	@Column(name = "remark", length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "tenantId", length = 32, nullable = false)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Column(name = "roleId", length = 32, nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "resourceTypeCode", length = 64, nullable = false)
	public String getResourceTypeCode() {
		return resourceTypeCode;
	}

	public void setResourceTypeCode(String resourceTypeCode) {
		this.resourceTypeCode = resourceTypeCode;
	}

	@Column(name = "resourceId", length = 32, nullable = false)
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
