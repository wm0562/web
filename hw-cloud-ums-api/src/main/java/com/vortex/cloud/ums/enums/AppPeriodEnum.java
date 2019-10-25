package com.vortex.cloud.ums.enums;

import com.vortex.cloud.vfs.common.lang.StringUtil;

/**
 * 第三方app访问频率枚举
 * 
 * @author XY
 *
 */
public enum AppPeriodEnum {
	DAY("DAY", "每天"), HOUR("HOUR", "每小时"), MINUTE("MINUTE", "每分钟");

	private final String key;
	private final String value;

	private AppPeriodEnum(String key, String value) {
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
		for (AppPeriodEnum e : AppPeriodEnum.values()) {
			if (e.getKey().equals(key)) {
				return e.getValue();
			}
		}
		return null;
	}

	public static String getKeyByValue(String value) {
		if (!StringUtil.isNullOrEmpty(value)) {
			for (AppPeriodEnum e : AppPeriodEnum.values()) {
				if (e.getValue().equals(value)) {
					return e.getKey();
				}
			}
		}
		return null;
	}
}
