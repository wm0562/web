package com.vortex.cloud.ums.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * department和org表的外挂信息表
 * 
 * @author XY
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cloud_org_info")
public class CloudOrgInfo extends BakDeleteModel {
	public static final String STATE_YES = "1";
	public static final String STATE_NO = "0";

	private String orgId; // dept或org表id
	private String orgCid; // 单位终生码
	private String abbr; // 单位简称
	private Integer beginYear; // 启用年
	private Integer endYear; // 结束年
	private String orgType; // 单位类型 EXTEND12
	private String isDzd; // 是否代征点 EXTEND8
	private String isReseau; // 是否网格 EXTEND9
	private String isMgr; // 是否管辖单位 EXTEND4
	private String orgState; // 是否启用

	public String getOrgState() {
		return orgState;
	}

	public void setOrgState(String orgState) {
		this.orgState = orgState;
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

	@Column(name = "abbr", length = 64)
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
