package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

import com.vortex.cloud.ums.dto.TenantRoleDto;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.vfs.data.hibernate.service.PagingAndSortingService;

public interface ITenantRoleService extends PagingAndSortingService<TenantRole, String> {
	/**
	 * 保存角色
	 * 
	 * @param dto
	 * @return
	 * 
	 */
	public String saveRole(TenantRoleDto dto);

	/**
	 * 更新角色
	 * 
	 * @param dto
	 * 
	 */
	public void updateRole(TenantRoleDto dto);

	/**
	 * 删除角色
	 * 
	 * @param roleId
	 * 
	 */
	public void deleteRole(String roleId);

	/**
	 * 判断code是否重复，在同一个租户下面，不能重复
	 * 
	 * @param roleId
	 * @param newCode
	 * @return
	 * 
	 */
	public boolean isRoleCodeExists(String roleId, String newCode, String tenantId);

	/**
	 * 根据id，得到角色信息，转存入dto中返回
	 * 
	 * @param roleId
	 * @return
	 * 
	 */
	public TenantRoleDto getRoleInfoById(String roleId);

	/**
	 * 根据角色code获取角色记录。注意：这种角色记录为系同级别的角色，非租户级别的，由数据库初始化产生。
	 * 
	 * @param code
	 * @return
	 */
	TenantRoleDto getRoleByCode(String code);

	/**
	 * 删除1~N条记录
	 * 
	 * @param idList
	 */
	public void deletes(List<String> idList);

	/**
	 * 获取绑定该角色的人员
	 * 
	 * @param tenantId
	 * @param roleCode
	 * @return
	 */
	public List<String> getUserIdsByRole(String tenantId, String roleCode);
}
