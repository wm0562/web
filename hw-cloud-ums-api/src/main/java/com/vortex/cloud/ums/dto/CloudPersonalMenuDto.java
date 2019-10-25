package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

/**
 * 用户菜单dto
 * 
 * @author ll
 *
 */
public class CloudPersonalMenuDto extends BackDeleteModelDto {
	private static final long serialVersionUID = 1L;
	private String userId; // 用户id
	private String menuId; // 菜单id
	private Integer orderIndex; // 排序号

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
}
