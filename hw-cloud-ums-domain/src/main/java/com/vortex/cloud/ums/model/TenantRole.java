package com.vortex.cloud.ums.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
* @ClassName: TenantRole
* @Description: 租户角色（不挂在业务系统下）
* @author ZQ shan
* @date 2018年1月23日 上午9:30:51
* @see [相关类/方法]（可选）
* @since [产品/模块版本] （可选）
*/
@Entity
@Table(name = "cloud_tenant_role")
public class TenantRole extends BakDeleteModel {
	private static final long serialVersionUID = 1L;

	private String code; // 编码
	private String name; // 名称
	private String groupId; // 角色组id
	private Integer orderIndex; // 排序号
	private String description; // 描述
	private String tenantId; // 租户id


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
}
