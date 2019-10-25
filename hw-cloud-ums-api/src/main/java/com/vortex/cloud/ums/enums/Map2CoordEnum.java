package com.vortex.cloud.ums.enums;

import com.vortex.cloud.vfs.common.lang.StringUtil;

/**
 * 根据地图类型，得到坐标系
 * 
 * @author XY
 *
 */
public enum Map2CoordEnum {
	AMAP("amap", "gcj02"), BMAP("bmap", "bd09"), TMAP("tmap", "wgs84");

	private final String key;
	private final String value;

	private Map2CoordEnum(String key, String value) {
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
