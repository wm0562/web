package com.vortex.cloud.ums.dto;

import java.beans.Transient;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;
import com.vortex.cloud.ums.dto.base.BasePoint;
import com.vortex.cloud.vfs.common.lang.StringUtil;

@SuppressWarnings("serial")
public class WorkElementDto extends BackDeleteModelDto {
	/* 部门名称 */
	private String departmentName;
	/* 图元类型名称 */
	private String workElementTypeCode;
	/* 图元类型名称 */
	private String workElementTypeName;
	/* 行政区划名称 */
	private String divisionName;

	private Integer flag;

	private String tenantId; // 租户id

	/** 外形 **/
	private String shape;
	/** 编号 **/
	private String code;
	/** 名称 **/
	private String name;

	/**
	 * 经纬度 xxx,xxx;xxx,xxx (WGS84坐标系)
	 */
	private String params;
	// 偏转后的经纬度(BD09坐标系)
	private String paramsDone;

	/** 面积(米) **/
	private Double area;
	// 长度
	private Double length;
	// 半径
	private Double radius;
	// 颜色
	private String color;

	/** 所属公司 **/
	private String departmentId;

	private List<BasePoint> points = Lists.newArrayList();

	/** 描述 **/
	private String description;

	/** 工作区域类型 ***/
	private String workElementTypeId;

	/**
	 * 所属用户
	 */
	private String userId;
	/**
	 * 行政区划ID
	 */
	private String divisionId;

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getWorkElementTypeCode() {
		return workElementTypeCode;
	}

	public void setWorkElementTypeCode(String workElementTypeCode) {
		this.workElementTypeCode = workElementTypeCode;
	}

	public String getWorkElementTypeName() {
		return workElementTypeName;
	}

	public void setWorkElementTypeName(String workElementTypeName) {
		this.workElementTypeName = workElementTypeName;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getParamsDone() {
		return paramsDone;
	}

	public void setParamsDone(String paramsDone) {
		this.paramsDone = paramsDone;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public List<BasePoint> getPoints() {
		return points;
	}

	public void setPoints(List<BasePoint> points) {
		this.points = points;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWorkElementTypeId() {
		return workElementTypeId;
	}

	public void setWorkElementTypeId(String workElementTypeId) {
		this.workElementTypeId = workElementTypeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	@Transient
	@JsonIgnore
	public List<BasePoint> getTransferPoints() {
		List<BasePoint> pointLists = Lists.newArrayList();
		if (!StringUtil.isNullOrEmpty(params)) {
			String[] lnglats = params.split(";");
			BasePoint point = null;
			for (String s : lnglats) {
				String[] lnglat = s.split(",");
				point = new BasePoint();
				point.setLongitude(Double.valueOf(lnglat[0]));
				point.setLatitude(Double.valueOf(lnglat[1]));
				point.setDone(true);
				point.setLongitudeDone(Double.valueOf(lnglat[0]));
				point.setLatitudeDone(Double.valueOf(lnglat[1]));
				pointLists.add(point);
			}
		}
		return pointLists;
	}
}
