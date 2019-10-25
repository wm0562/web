package com.vortex.cloud.ums.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
* @ClassName: TenantFunctionRole
* @Description: 功能和角色的对应表（不挂在业务系统下）
* @author ZQ shan
* @date 2018年1月23日 上午9:32:54
* @see [相关类/方法]（可选）
* @since [产品/模块版本] （可选）
*/
@Entity
@Table(name = "cloud_tenant_function_role")
public class TenantFunctionRole extends BakDeleteModel {
	private static final long serialVersionUID = 1L;
	private String functionId; // 功能id
	private String roleId; // 角色id

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


}
