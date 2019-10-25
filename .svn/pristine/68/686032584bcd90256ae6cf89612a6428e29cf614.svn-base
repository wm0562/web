/*   
 * Copyright (C), 2005-2014, 苏州伏泰信息科技有限公司
 */
package com.vortex.cloud.ums.dto;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

/**
 * @author LiShijun
 * @date 2016年5月23日 上午10:11:53
 * @Description 菜单管理 History <author> <time> <desc>
 */
public class CloudMenuDto extends BackDeleteModelDto {

	private static final long serialVersionUID = 1L;

	private String parentName; // 上级菜单Name
	private String isHiddenText;
	private String isControlledText;
	private String functionName;
	private boolean leafNode; // 是否为叶子节点

	private String systemId; // 云系统id
	private String code; // 编码
	private String name; // 名称
	private Integer orderIndex; // 排序号
	private String description; // 描述
	private String parentId; // 父节点id
	private String photoIds; // json格式的字符串
	private Integer isHidden = 0; // 是否隐藏，默认0显示，1隐藏
	private String functionId; // 绑定的功能码
	private Integer isControlled = 1; // 是否受控制，默认1-受控，0-不受控；不受控的菜单，所有人都可以访问
	private Integer isWelcomeMenu = 0; // 是否欢迎页面，默认0-否，1-是
	// 内置编号：用于层级数据结构的构造（如树）
	private String nodeCode;

	// 子层所有数据记录数，和己编号配置生成子编号
	private Integer childSerialNumer;
	// 图标
	private String iconFont;

	public static final Integer HIDDEN_YES = 1;
	public static final Integer HIDDEN_NOT = 0;

	public static final Integer CONTROLLED_YES = 1;
	public static final Integer CONTROLLED_NOT = 0;

	public static final Integer IS_WELCOME_MENU_YES = 1;
	public static final Integer IS_WELCOME_MENU_NOT = 0;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getIsHiddenText() {
		return isHiddenText;
	}

	public void setIsHiddenText(String isHiddenText) {
		this.isHiddenText = isHiddenText;
	}

	public String getIsControlledText() {
		return isControlledText;
	}

	public void setIsControlledText(String isControlledText) {
		this.isControlledText = isControlledText;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public boolean isLeafNode() {
		return leafNode;
	}

	public void setLeafNode(boolean leafNode) {
		this.leafNode = leafNode;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
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

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPhotoIds() {
		return photoIds;
	}

	public void setPhotoIds(String photoIds) {
		this.photoIds = photoIds;
	}

	public Integer getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Integer isHidden) {
		this.isHidden = isHidden;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public Integer getIsControlled() {
		return isControlled;
	}

	public void setIsControlled(Integer isControlled) {
		this.isControlled = isControlled;
	}

	public Integer getIsWelcomeMenu() {
		return isWelcomeMenu;
	}

	public void setIsWelcomeMenu(Integer isWelcomeMenu) {
		this.isWelcomeMenu = isWelcomeMenu;
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

	public String getIconFont() {
		return iconFont;
	}

	public void setIconFont(String iconFont) {
		this.iconFont = iconFont;
	}
}
