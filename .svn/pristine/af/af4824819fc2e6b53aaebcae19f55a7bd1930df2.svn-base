package com.vortex.cloud.ums.enums;

/**
 * @ClassName: MsgAuthCodeErrorEnum
 * @Description: 门户用户验证码错误编码
 * @author ZQ shan
 * @date 2018年1月29日 下午3:05:30
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public enum MsgAuthCodeErrorEnum {

	RE_PHONE_EXISTS(101, "手机号已存在"), // 注册时，手机号已存在
	RE_PHONE_ISSEND(102, "该手机号有效时间内已发送过短信"), // 注册时，该手机号有效时间内已发送过短信
	FO_PHONE_NOTEXISTS(111, "手机号不存在"),// 忘记密码，手机号不存在
	FO_PHONE_ISSEND(112, "该手机号有效时间内已发送过短信"); // 忘记密码，该手机号有效时间内已发送过短信
	Integer key;
	String value;

	private MsgAuthCodeErrorEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static String getValueByKey(Integer key) {
		String val = null;
		if (null == key) {
			return val;
		}
		for (MsgAuthCodeErrorEnum e : MsgAuthCodeErrorEnum.values()) {
			if (e.key == key) {
				val = e.value;
				break;
			}
		}

		return val;
	}
}
