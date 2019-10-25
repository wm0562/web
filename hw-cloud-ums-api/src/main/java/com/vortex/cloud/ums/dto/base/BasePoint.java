package com.vortex.cloud.ums.dto.base;

public class BasePoint extends BaseModelDto {
	private static final long serialVersionUID = 1L;
	/** 经度 **/
	private Double longitude;
	/** 纬度 **/
	private Double latitude;

	/** 是否偏转 **/
	private Boolean done;
	/** 偏转经度 **/
	private Double longitudeDone;
	/** 偏转纬度 **/
	private Double latitudeDone;

	/** 排序 **/
	private Integer orderIndex;

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

	public Boolean getDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
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

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
}
