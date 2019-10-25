package com.vortex.cloud.ums.dto;

public class OrgInfoDto {
	private String tenantId; // 租户id
	private String head; // 负责人
	private String headMobile; // 负责人电话
	private String description; // 描述
	private String lngLats; // 经纬度
	private String address; // 地址
	private String email; // 邮箱
	private String name; // 名称
	private String code; // 编码
	private String parentId; // 父节点id
	private String parentName; // 父节点名称
	private String flag; // 1- dept,2-org
	private Integer orderIndex; // 排序号

	// 以下是orgInfo表字段
	private String orgInfoId; // cloudOrgInfo表的id
	private String orgId; // dept或org表id
	private String orgCid; // 单位终生码
	private String abbr; // 单位简称
	private Integer beginYear; // 启用年
	private Integer endYear; // 结束年
	private String orgType; // 单位类型 EXTEND12
	private String isDzd; // 是否代征点 EXTEND8
	private String isReseau; // 是否网格 EXTEND9
	private String isMgr; // 是否管辖单位 EXTEND4

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getHeadMobile() {
		return headMobile;
	}

	public void setHeadMobile(String headMobile) {
		this.headMobile = headMobile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLngLats() {
		return lngLats;
	}

	public void setLngLats(String lngLats) {
		this.lngLats = lngLats;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getOrgInfoId() {
		return orgInfoId;
	}

	public void setOrgInfoId(String orgInfoId) {
		this.orgInfoId = orgInfoId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgCid() {
		return orgCid;
	}

	public void setOrgCid(String orgCid) {
		this.orgCid = orgCid;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public Integer getBeginYear() {
		return beginYear;
	}

	public void setBeginYear(Integer beginYear) {
		this.beginYear = beginYear;
	}

	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getIsDzd() {
		return isDzd;
	}

	public void setIsDzd(String isDzd) {
		this.isDzd = isDzd;
	}

	public String getIsReseau() {
		return isReseau;
	}

	public void setIsReseau(String isReseau) {
		this.isReseau = isReseau;
	}

	public String getIsMgr() {
		return isMgr;
	}

	public void setIsMgr(String isMgr) {
		this.isMgr = isMgr;
	}
}
