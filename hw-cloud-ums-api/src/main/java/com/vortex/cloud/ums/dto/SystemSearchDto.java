package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

/**
 * 租户配置业务系统管理页面的搜索条件
 * 
 * @author lishijun
 *
 */
public class SystemSearchDto extends BackDeleteModelDto {
	private static final long serialVersionUID = 1L;
	private String tenantId; // 租户id
	private String systemCode; // 系统编码
	private String systemName; // 系统名称
	private String website; // 站点（包含ip端口等）

	private String mapType; // 地图类型
	private String mapStr; // 地图配置字符串
	// 以下为该系统打开地图控件时的默认位置
	private Double longitude; // 经度
	private Double latitude; // 纬度
	private Double longitudeDone; // 偏转后的经度
	private Double latitudeDone; // 偏转后的纬度
	private Integer systemType; // 系统类型
	private String welcomePage; // 欢迎页
	private Integer orderIndex; // 排序号

	public static final Integer SYSTEM_TYPE_CLOUD = 1; // 云服务系统
	public static final Integer SYSTEM_TYPE_BUSINESS = 2; // 业务系统

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public String getMapStr() {
		return mapStr;
	}

	public void setMapStr(String mapStr) {
		this.mapStr = mapStr;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitudeDone() {
		return longitudeDone;
	}

	public void setLongitudeDone(Double longitudeDone) {
		this.longitudeDone = longitudeDone;
	}

	public Double getLatitudeDone() {
		return latitudeDone;
	}

	public void setLatitudeDone(Double latitudeDone) {
		this.latitudeDone = latitudeDone;
	}

	public Integer getSystemType() {
		return systemType;
	}

	public void setSystemType(Integer systemType) {
		this.systemType = systemType;
	}

	public String getWelcomePage() {
		return welcomePage;
	}

	public void setWelcomePage(String welcomePage) {
		this.welcomePage = welcomePage;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
}
