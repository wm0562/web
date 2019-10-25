package com.vortex.cloud.ums.dto;

import com.vortex.cloud.vfs.data.dto.LoginReturnInfoDto;

public class UmsLoginReturnInfoDto extends LoginReturnInfoDto {
	private String rongLianAccount; // 容联帐号
	private String imToken;// 融云imToken
	private String postName;
	private String partyPostName;
	// 登录类型
	private String loginType;
	/**
	 * 控制访问频次的时间周期枚举-分钟、小时、天等
	 */
	private String period;
	/**
	 * 周期中可以访问的次数限制
	 */
	private Long periodCount;
	/**
	 * ip白名单列表，英文逗号分隔
	 */
	private String whiteList;

	/**
	 * 租户地图配置字符串
	 */
	private String mapDefJson;

	public String getMapDefJson() {
		return mapDefJson;
	}

	public void setMapDefJson(String mapDefJson) {
		this.mapDefJson = mapDefJson;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPartyPostName() {
		return partyPostName;
	}

	public void setPartyPostName(String partyPostName) {
		this.partyPostName = partyPostName;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Long getPeriodCount() {
		return periodCount;
	}

	public void setPeriodCount(Long periodCount) {
		this.periodCount = periodCount;
	}

	public String getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
	}

	public String getRongLianAccount() {
		return rongLianAccount;
	}

	public void setRongLianAccount(String rongLianAccount) {
		this.rongLianAccount = rongLianAccount;
	}

	public String getImToken() {
		return imToken;
	}

	public void setImToken(String imToken) {
		this.imToken = imToken;
	}
}
