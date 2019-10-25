package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.vortex.cloud.ums.dto.TenantFunctionRoleDto;
import com.vortex.cloud.ums.model.TenantFunctionRole;
import com.vortex.cloud.ums.util.orm.Page;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
/**
* @ClassName: ITenantFunctionRoleDao
* @Description: 功能和角色的对应dao（不挂在业务系统下）
* @author ZQ shan
* @date 2018年1月23日 上午10:09:54
* @see [相关类/方法]（可选）
* @since [产品/模块版本] （可选）
*/
public interface ITenantFunctionRoleDao extends HibernateRepository<TenantFunctionRole, String> {
	/**
	 * 查找角色下绑定的云系统功能列表的分页
	 * @param roleId
	 * @param pageable
	 * @return
	 */
	Page<TenantFunctionRoleDto> getPageByRole(String roleId, Pageable pageable);

	/**
	 * 查找角色下绑定的云系统功能列表
	 * @param roleId
	 * @return
	 */
	List<TenantFunctionRoleDto> getListByRole(String roleId);
}
