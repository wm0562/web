package com.vortex.cloud.ums.enums;

import com.vortex.cloud.vfs.common.lang.StringUtil;

/**
 * 登录类型
 * 
 * @author XY
 *
 */
public enum LoginTypeEnum {
	VORTEX_USER("VORTEX_USER", "伏泰用户"), THIRD_PARTY_APP("THIRD_PARTY_APP", "第三方app"), PORTAL_USER("PORTAL_USER", "门户用户");

	private final String key;
	private final String value;

	private LoginTypeEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static String getValueByKey(String key) {
		for (LoginTypeEnum e : LoginTypeEnum.values()) {
			if (e.getKey().equals(key)) {
				return e.getValue();
			}
		}
		return null;
	}

	public static String getKeyByValue(String value) {
		if (!StringUtil.isNullOrEmpty(value)) {
			for (LoginTypeEnum e : LoginTypeEnum.values()) {
				if (e.getValue().equals(value)) {
					return e.getKey();
				}
			}
		}
		return null;
	}
}
