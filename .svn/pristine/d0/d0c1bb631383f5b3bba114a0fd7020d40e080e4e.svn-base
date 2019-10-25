package com.vortex.cloud.ums.dataaccess.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vortex.cloud.ums.dto.TenantUserRoleDto;
import com.vortex.cloud.ums.dto.TenantUserRoleSearchDto;
import com.vortex.cloud.ums.model.TenantUserRole;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

/**
* @ClassName: ITenantUserRoleDao
* @Description: 用户角色dao（不挂在业务系统下）
* @author ZQ shan
* @date 2018年1月23日 上午10:10:51
* @see [相关类/方法]（可选）
* @since [产品/模块版本] （可选）
*/
public interface ITenantUserRoleDao extends HibernateRepository<TenantUserRole, String> {
	Page<TenantUserRoleDto> findPageBySearchDto(Pageable pageable, TenantUserRoleSearchDto searchDto);
	void deleteByUserId(String userId);
	
}
