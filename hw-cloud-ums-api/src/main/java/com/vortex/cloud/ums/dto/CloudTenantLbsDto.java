package com.vortex.cloud.ums.dto;

public class CloudTenantLbsDto {
	/**
	 * 地图类型编码
	 */
	private String mapType;
	/**
	 * 地图名称
	 */
	private String mapName;
	/**
	 * 是否默认地图
	 */
	private Boolean defaultMap;
	/**
	 * 是否被选中
	 */
	private Boolean checked;
	/**
	 * 坐标系（arcgis）
	 */
	private String coordinate;
	/**
	 * 图层（arcgis）
	 */
	private String coverage;
	/**
	 * 基础信息（arcgis）
	 */
	private String basicData;

	private String wkid;

	/**
	 * 最大层级
	 */
	private String maxZoom;

	/**
	 * 最小层级
	 */
	private String minZoom;

	public String getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(String maxZoom) {
		this.maxZoom = maxZoom;
	}

	public String getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(String minZoom) {
		this.minZoom = minZoom;
	}

	public String getWkid() {
		return wkid;
	}

	public void setWkid(String wkid) {
		this.wkid = wkid;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public Boolean getDefaultMap() {
		return defaultMap;
	}

	public void setDefaultMap(Boolean defaultMap) {
		this.defaultMap = defaultMap;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getBasicData() {
		return basicData;
	}

	public void setBasicData(String basicData) {
		this.basicData = basicData;
	}
}
