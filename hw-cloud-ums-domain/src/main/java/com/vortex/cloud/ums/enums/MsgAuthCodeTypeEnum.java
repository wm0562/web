package com.vortex.cloud.ums.enums;

import com.vortex.cloud.vfs.common.lang.StringUtil;

/**
 * @ClassName: MsgAuthCodeTypeEnum
 * @Description: 门户用户验证码类型
 * @author ZQ shan
 * @date 2018年1月29日 下午3:05:30
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public enum MsgAuthCodeTypeEnum {

	REGISTER("REGISTER", "注册"), // 注册

	FORGET("FORGET", "忘记密码"); // 忘记密码
	String key;
	String value;

	private MsgAuthCodeTypeEnum(String key, String value) {
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
		String val = null;
		if (StringUtil.isNullOrEmpty(key)) {
			return val;
		}
		for (MsgAuthCodeTypeEnum e : MsgAuthCodeTypeEnum.values()) {
			if (e.key.equals(key)) {
				val = e.value;
				break;
			}
		}

		return val;
	}
}
