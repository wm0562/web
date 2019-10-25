package com.vortex.cloud.ums.xm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * 厦门二期部门表结构，该表是客户公司表，其中没有人员
 * 
 * @author XY
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_ORG")
public class SysOrgXm extends BakDeleteModel {
	private String pid; // 父节点id
	private String org_cid; // 终身码
	private String org_code; // 编码
	private String org_name; // 名称
	private String level_code; // 树形编码
	private String abbr; // 简称
	private String org_state; // 发布状态 1-发布，2-未发布
	private Integer begin_year; // 启用年份
	private Integer end_year; // 结束年份
	private String has_child; // 是否有子节点 0-无，1-有
	private String extend12; // 单位类型 EXTEND12
	private String extend8; // 是否代征点 EXTEND8
	private String extend9; // 是否网格 EXTEND9
	private String extend4; // 是否管辖单位 EXTEND4
	private Integer order_no;// 排序号
	private String remark; // 备注
	/**
	 * 区
	 */
	private String districtId;

	/**
	 * 街道
	 */
	private String subDistrictId;

	/**
	 * 社区居委会
	 */
	private String communityId;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 主要联系人
	 */
	private String primaryContact;

	/**
	 * 联系人电话
	 */
	private String contactPhone;
	@Column(name = "remark", length = 300)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOrg_cid() {
		return org_cid;
	}

	public void setOrg_cid(String org_cid) {
		this.org_cid = org_cid;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getLevel_code() {
		return level_code;
	}

	public void setLevel_code(String level_code) {
		this.level_code = level_code;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getOrg_state() {
		return org_state;
	}

	public void setOrg_state(String org_state) {
		this.org_state = org_state;
	}

	public Integer getBegin_year() {
		return begin_year;
	}

	public void setBegin_year(Integer begin_year) {
		this.begin_year = begin_year;
	}

	public Integer getEnd_year() {
		return end_year;
	}

	public void setEnd_year(Integer end_year) {
		this.end_year = end_year;
	}

	public String getHas_child() {
		return has_child;
	}

	public void setHas_child(String has_child) {
		this.has_child = has_child;
	}

	public String getExtend12() {
		return extend12;
	}

	public void setExtend12(String extend12) {
		this.extend12 = extend12;
	}

	public String getExtend8() {
		return extend8;
	}

	public void setExtend8(String extend8) {
		this.extend8 = extend8;
	}

	public String getExtend9() {
		return extend9;
	}

	public void setExtend9(String extend9) {
		this.extend9 = extend9;
	}

	public String getExtend4() {
		return extend4;
	}

	public void setExtend4(String extend4) {
		this.extend4 = extend4;
	}

	public Integer getOrder_no() {
		return order_no;
	}

	public void setOrder_no(Integer order_no) {
		this.order_no = order_no;
	}
	@Column(name = "district_id")
	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	@Column(name = "sub_district_id")
	public String getSubDistrictId() {
		return subDistrictId;
	}

	public void setSubDistrictId(String subDistrictId) {
		this.subDistrictId = subDistrictId;
	}

	@Column(name = "community_id")
	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "primary_contact")
	public String getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}

	@Column(name = "contact_phone")
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
}
