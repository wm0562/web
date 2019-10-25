package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

/**
 * 登录日志dto
 * 
 * @author ll
 *
 */
public class CloudLoginLogDto extends BackDeleteModelDto {
	private static final long serialVersionUID = -3989404715735946722L;

	private String userName;
	private String name;
	private String ip;
	private String operation = "登录";

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
