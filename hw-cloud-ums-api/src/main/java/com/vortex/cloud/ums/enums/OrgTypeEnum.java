package com.vortex.cloud.ums.enums;

/**
 * cloud_org_info表的orgType字段的枚举
 * 
 * @author XY
 *
 */
public enum OrgTypeEnum {
	ORG_CK("26", "保洁公司"), ORG_SCK("20", "社区保洁"), ORG_WCK("25", "物业保洁"), ORG_SHW("05", "市环卫"), ORG_JD("10", "街道"), ORG_MGR("27", "管理单位"), ORG_DZD("35", "代征点"), ORG_SC("50", "水厂"), ORG_QU("06", "区");

	String key;
	String value;

	private OrgTypeEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
