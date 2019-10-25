package com.vortex.cloud.ums.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * @ClassName: TenantUserRole
 * @Description: 用户角色关系（不挂在业务系统下）
 * @author ZQ shan
 * @date 2018年1月23日 上午10:10:23
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Entity
@Table(name = "cloud_tenant_user_role")
public class TenantUserRole extends BakDeleteModel {
	private static final long serialVersionUID = 1L;

	private String userId; // 用户id
	private String roleId; // 角色id

	@Column(name = "userId", length = 32, nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "roleId", length = 32, nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
