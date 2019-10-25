package com.vortex.cloud.ums.dto;

import java.util.ArrayList;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

public class TenantDivisionDto extends BackDeleteModelDto {

	private static final long serialVersionUID = 1L;

	private String parentName; // 上级区域
	private String levelText; // 行政级别的文本描述

	private Double defLongitude; // 建议的默认经度：取父节点的值
	private Double defLatitude; // 建议的默认纬度：取父节点的值

	private Double longitude; // 经度
	private Double latitude; // 纬度

	private String containsRoot;// 是否包含root节点 ，1包含，0不包含
	public static final String CONTAIN_ROOT_NO = "0";
	public static final String CONTAIN_ROOT_YES = "1";

	/**
	 * 行政区划的级别：省
	 */
	public static final int LEVEL_PROVINCE = 1;
	/**
	 * 行政区划的级别：市
	 */
	public static final int LEVEL_CITY = LEVEL_PROVINCE + 1;
	/**
	 * 行政区划的级别：区/县
	 */
	public static final int LEVEL_DISTRICT = LEVEL_CITY + 1;
	/**
	 * 行政区划的级别：乡镇/街道
	 */
	public static final int LEVEL_VILLAGE = LEVEL_DISTRICT + 1;
	/**
	 * 行政区划的级别：居委会
	 */
	public static final int LEVEL_RUSTIC = LEVEL_VILLAGE + 1;

	public static final Integer ENABLED_YES = 1;
	public static final Integer ENABLED_NOT = 0;

	public static final String ROOT_YES = "1";
	public static final String ROOT_NOT = "0";

	private String tenantId; // 租户Id

	// 通用编号
	private String commonCode;
	// 区划名称
	private String name;
	// 简称
	private String abbr;
	// 行政级别
	private Integer level;
	// 上级区划
	private String parentId;

	private String lngLats; // 行政区划中心点

	// 生效日期
	private String startTime;
	// 失效日期
	private String endTime;
	// 是否有效1 ： 是，0 ：否；
	private Integer enabled = 1;

	private String isRoot; // 是否根节点，1：是，0：否

	// 内置编号：用于层级数据结构的构造（如树）
	private String nodeCode;

	// 子层所有数据记录数，和己编号配置生成子编号
	private Integer childSerialNumer;
	// 行政区划范围，经纬度用,分隔，点之间用;分割
	private String scope;
	// 顺序号
	private Integer orderIndex;

	public String getContainsRoot() {
		return containsRoot;
	}

	public void setContainsRoot(String containsRoot) {
		this.containsRoot = containsRoot;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getLevelText() {
		return levelText;
	}

	public void setLevelText(String levelText) {
		this.levelText = levelText;
	}

	public Double getDefLongitude() {
		return defLongitude;
	}

	public void setDefLongitude(Double defLongitude) {
		this.defLongitude = defLongitude;
	}

	public Double getDefLatitude() {
		return defLatitude;
	}

	public void setDefLatitude(Double defLatitude) {
		this.defLatitude = defLatitude;
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

	public static class BatchUpdate {
		/** 主键 uuid **/
		private String id;
		// 通用编号
		private String commonCode;
		// 区划名称
		private String name;
		// 简称
		private String abbr;

		public BatchUpdate() {

		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCommonCode() {
			return commonCode;
		}

		public void setCommonCode(String commonCode) {
			this.commonCode = commonCode;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAbbr() {
			return abbr;
		}

		public void setAbbr(String abbr) {
			this.abbr = abbr;
		}
	}

	public static class BatchUpdateList extends ArrayList<BatchUpdate> {
		private static final long serialVersionUID = -8862714475178790945L;

		public BatchUpdateList() {
			super();
		}
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getCommonCode() {
		return commonCode;
	}

	public void setCommonCode(String commonCode) {
		this.commonCode = commonCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getLngLats() {
		return lngLats;
	}

	public void setLngLats(String lngLats) {
		this.lngLats = lngLats;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public Integer getChildSerialNumer() {
		return childSerialNumer;
	}

	public void setChildSerialNumer(Integer childSerialNumer) {
		this.childSerialNumer = childSerialNumer;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
}
