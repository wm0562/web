package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vortex.cloud.ums.dto.TenantUserRoleDto;
import com.vortex.cloud.ums.dto.TenantUserRoleSearchDto;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.model.TenantUserRole;
import com.vortex.cloud.vfs.data.hibernate.service.PagingAndSortingService;
/**
* @ClassName: ITenantUserRoleService
* @Description: 
* @author ZQ shan
* @date 2018年1月23日 上午10:23:24
* @see [相关类/方法]（可选）
* @since [产品/模块版本] （可选）
*/
public interface ITenantUserRoleService extends PagingAndSortingService<TenantUserRole, String> {

	/**
	 * 为指定用户配置角色
	 * 
	 * @param userId
	 * @param roleIdArr
	 */
	void addRoles(String userId, String[] roleIdArr);

	/**
	 * 根据用户id，得到用户所拥有的角色列表
	 * 
	 * @param userId
	 * @return
	 */
	List<TenantRole> getRolesByUserId(String userId);

	/**
	 * 删除用户上面的所有角色
	 * 
	 * @param userId
	 */
	void deleteByUserId(String userId);
	
	
	Page<TenantUserRoleDto> findPageBySearchDto(Pageable pageable, TenantUserRoleSearchDto searchDto);
}
