package com.vortex.cloud.ums.dto;

public class LngLatResponseDto {
	/* 形状 */
	private String shapeType;
	/* 原始经纬度 */
	private String wgs84;
	/* 百度经纬度 */
	private String bd09;
	/* 高德经纬度 */
	private String gcj02;

	public String getShapeType() {
		return shapeType;
	}

	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}

	public String getWgs84() {
		return wgs84;
	}

	public void setWgs84(String wgs84) {
		this.wgs84 = wgs84;
	}

	public String getBd09() {
		return bd09;
	}

	public void setBd09(String bd09) {
		this.bd09 = bd09;
	}

	public String getGcj02() {
		return gcj02;
	}

	public void setGcj02(String gcj02) {
		this.gcj02 = gcj02;
	}
}
