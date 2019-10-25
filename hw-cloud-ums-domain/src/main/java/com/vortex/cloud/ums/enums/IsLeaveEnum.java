package com.vortex.cloud.ums.enums;

/**
 * 在职状态枚举
 * 
 * @author XY
 *
 */
public enum IsLeaveEnum {
	ZAIZHI("0", "在职"), LIZHI("1", "离职"), TUIXIU("2", "退休");
	String key;
	String value;

	private IsLeaveEnum(String key, String value) {
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
