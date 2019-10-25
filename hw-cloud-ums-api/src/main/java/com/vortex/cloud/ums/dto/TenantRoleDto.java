package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

public class TenantRoleDto extends BackDeleteModelDto {
	private static final long serialVersionUID = 1L;

	private String groupName;
	private String code; // 编码
	private String name; // 名称
	private String groupId; // 角色组id
	private Integer orderIndex; // 排序号
	private String description; // 描述
	private String tenantId; // 租户id

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
