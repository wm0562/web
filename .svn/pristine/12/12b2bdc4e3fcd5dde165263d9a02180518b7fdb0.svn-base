package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

import com.vortex.cloud.ums.dto.TenantRoleGroupDto;
import com.vortex.cloud.ums.model.TenantRoleGroup;
import com.vortex.cloud.vfs.data.hibernate.service.PagingAndSortingService;

public interface ITenantRoleGroupService
		extends PagingAndSortingService<TenantRoleGroup, String> {
	/**
	 * 根据id删除所有的
	 * 
	 * @param id
	 * 
	 */
	public abstract void deleteAllById(final String id);

	/**
	 * 获取所有的子节点id
	 * 
	 * @param id
	 * @return
	 * 
	 */
	public abstract List<String> getAllChildrenId(String id);

	/**
	 * 获取所有的父节点
	 * 
	 * @param id
	 * @return
	 * 
	 */
	public abstract List<String> getAllParentId(String id);

	/**
	 * 保存角色组信息
	 * 
	 * @param dto
	 * 
	 */
	public abstract void saveRoleGroup(TenantRoleGroupDto dto);

	/**
	 * 根据id查找记录
	 * 
	 * @param id 主键
	 * @return
	 * 
	 */
	public abstract TenantRoleGroupDto findRoleGroupById(String id);

	/**
	 * 更新角色组
	 * 
	 * @param dto
	 * 
	 */
	public abstract void updateRoleGroup(TenantRoleGroupDto dto);

	/**
	 * 删除角色组
	 * 
	 * @param rgId
	 * 
	 */
	public abstract void deleteRoleGroup(String rgId);

}
