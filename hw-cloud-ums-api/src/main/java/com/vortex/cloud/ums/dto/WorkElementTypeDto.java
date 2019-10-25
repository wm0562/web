package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

public class WorkElementTypeDto extends BackDeleteModelDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1715791319782177460L;
	/**
	 * 所属公司名称
	 */
	private String departmentName;

	private String tenantId; // 租户id

	private String code;// 编号

	private String name;// 名称

	/** 外形 point：点，line:线，polygon：多边形，rectangle：矩形，circle：圆 **/
	private String shape;

	private String info;
	/** 所属公司 **/
	private String departmentId;

	private Integer orderIndex;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
}
