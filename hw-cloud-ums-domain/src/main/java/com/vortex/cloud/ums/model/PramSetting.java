package com.vortex.cloud.ums.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;



/**
 * 代码值
 * 
 * @author XY
 *
 */
@SuppressWarnings("all")
@Entity
@Table(name = "cloud_parameter_setting")
public class PramSetting extends BakDeleteModel {
	private String parmCode; // 代码值
	private String parmName; // 代码显示名称
	private String typeId; // 代码类型id
	private Integer orderIndex; // 顺序号
	

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	@Column(name = "parmCode", length = 32, nullable = false)
	public String getParmCode() {
		return parmCode;
	}

	public void setParmCode(String parmCode) {
		this.parmCode = parmCode;
	}

	@Column(name = "parmName", length = 255, nullable = false)
	public String getParmName() {
		return parmName;
	}

	public void setParmName(String parmName) {
		this.parmName = parmName;
	}

	@Column(name = "typeId", length = 32, nullable = false)
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
}
