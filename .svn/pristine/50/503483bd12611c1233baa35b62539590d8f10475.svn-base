package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

/**
 * 租户上面开启的云系统列表
 * 
 * @author lishijun
 *
 */
public class TenantSystemRelationDto extends BackDeleteModelDto {

	private static final long serialVersionUID = 1L;

	private String code; // 系统编号
	private String name; // 名称

	private Integer status; // 系统状态0:可用

	private String tenantId; // 租户id
	private String cloudSystemId; // 云上面的系统id
	private String enabled; // 系统是否开通，开通为1，未开通为0
	private String hasResource; // 是否复制过资源给租户，1复制过，0未复制
	// 系统是否开通
	public static final String ENABLE = "1"; // 启用系统
	public static final String DISABLE = "0"; // 禁用系统

	// 是否复制过资源给租户
	public static final String YES = "1"; // 复制过
	public static final String NOT = "0"; // 未复制

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getCloudSystemId() {
		return cloudSystemId;
	}

	public void setCloudSystemId(String cloudSystemId) {
		this.cloudSystemId = cloudSystemId;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getHasResource() {
		return hasResource;
	}

	public void setHasResource(String hasResource) {
		this.hasResource = hasResource;
	}
}
