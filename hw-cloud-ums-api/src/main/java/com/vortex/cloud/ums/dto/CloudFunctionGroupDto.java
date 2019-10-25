package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

public class CloudFunctionGroupDto extends BackDeleteModelDto {

	private static final long serialVersionUID = 3342351417812503975L;

	private String cloudSystemName;

	private String groupName; // 所在功能组

	private String code; // 编码
	private String name; // 名称
	private String description; // 描述
	private String parentId; // 父节点id
	private Integer orderIndex; // 顺序号
	private String systemId; // 所属系统id

	// 内置编号：用于层级数据结构的构造（如树）
	private String nodeCode;

	// 子层所有数据记录数，和己编号配置生成子编号
	private Integer childSerialNumber;

	public static final String ROOT_ID = "-1";

	public String getCloudSystemName() {
		return cloudSystemName;
	}

	public void setCloudSystemName(String cloudSystemName) {
		this.cloudSystemName = cloudSystemName;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public Integer getChildSerialNumber() {
		return childSerialNumber;
	}

	public void setChildSerialNumber(Integer childSerialNumber) {
		this.childSerialNumber = childSerialNumber;
	}
}
