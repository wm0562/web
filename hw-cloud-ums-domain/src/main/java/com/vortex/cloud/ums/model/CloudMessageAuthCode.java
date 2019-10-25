package com.vortex.cloud.ums.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * 手机短信验证码
 * 
 * @author XY
 *
 */
@Entity
@Table(name = "cloud_message_authcode")
public class CloudMessageAuthCode extends BakDeleteModel {
	private static final long serialVersionUID = 1L;

	private String phone; // 电话号码
	private String authCode; // 验证码（6位随机数字）
	// 类型（注册，忘记密码）
	private String msgType;
	private String tenantId;
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
