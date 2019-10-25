package com.vortex.cloud.ums.xm.dto;

/**
 * sysOrg表列表查询条件
 * 
 * @author XY
 *
 */
public class SysOrgQueryDto {
	private String name; // 名称
	private String code; // 编码
	private String parentId; // 父节点id
	private Integer pageNum; // 页码-从0开始
	private Integer pageSize; // 每页记录数

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
