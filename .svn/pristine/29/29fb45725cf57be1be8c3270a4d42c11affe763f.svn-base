package com.vortex.cloud.ums.xm.dto;

import com.vortex.cloud.ums.model.CloudDepartment;
import com.vortex.cloud.ums.model.CloudOrganization;
import com.vortex.cloud.ums.xm.model.SysOrgXm;

public class SysOrgTreeDto {
	private String parentId; // 上级id
	private String type; // // 1-dept，2-org，其他-公司（参考OrgTypeEnum）
	private String id; // 记录主键ID
	private String name; // 机构名称
	private String code; // 机构代码
	private String divisionId; // 行政区划id
	private String divisionType; // 1-5行政区划(省市区街道社区)
	private String divisionName; // 行政区划名称
	private String hasChild; // 是否目录

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getDivisionType() {
		return divisionType;
	}

	public void setDivisionType(String divisionType) {
		this.divisionType = divisionType;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SysOrgTreeDto transFromDept(CloudDepartment entity) {
		// 记录主键ID
		this.setId(entity.getId());
		// 上级id
		this.setParentId("0");
		// 1-dept，2-org，其他-公司（参考OrgTypeEnum）
		this.setType("1");
		// 机构名称
		this.setName(entity.getDepName());
		// 机构代码
		this.setCode(entity.getDepCode());
		// 行政区划id
		this.setDivisionId(entity.getDivisionId());
		return this;
	}

	public SysOrgTreeDto transFromOrg(CloudOrganization entity) {
		// 记录主键ID
		this.setId(entity.getId());
		// 上级id
		this.setParentId(entity.getParentId());
		// 1-dept，2-org，其他-公司（参考OrgTypeEnum）
		this.setType("2");
		// 机构名称
		this.setName(entity.getOrgName());
		// 机构代码
		this.setCode(entity.getOrgCode());
		// 行政区划id
		this.setDivisionId(entity.getDivisionId());
		return this;
	}

	public SysOrgTreeDto transFromSysOrg(SysOrgXm entity) {
		// 记录主键ID
		this.setId(entity.getId());
		// 上级id
		this.setParentId(entity.getPid());
		// 1-dept，2-org，其他-公司（参考OrgTypeEnum）
		this.setType(entity.getExtend12());
		// 机构名称
		this.setName(entity.getOrg_name());
		// 机构代码
		this.setCode(entity.getOrg_code());

		this.setHasChild(entity.getHas_child());
		return this;
	}
}
